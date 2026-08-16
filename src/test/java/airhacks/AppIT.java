package airhacks;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import airhacks.zb.build.boundary.Build;
import airhacks.zb.compiler.control.Compiler;
import airhacks.zb.configuration.control.Configuration;
import airhacks.zb.hook.control.PostBuildHook;
import airhacks.zb.packer.control.Packer;

public class AppIT {

    public static void main(String... args) throws Exception {
        // AppIT exercises the build itself; the post-build hook (e.g. zunit) would
        // recursively re-run this suite and clobber the JAR under assertion.
        System.setProperty(PostBuildHook.SKIP_MARKER, "true");
        createZBJar();
        servicesConfigurationFileIsIncluded();
        temporaryClassesDirectoryIsDeleted();
        explicitClassesDirectoryIsNotDeleted();
        buildWithClasspath();
        System.out.println("AppIT passed");
    }

    static Path jarFile() {
        return Path.of(AppArguments.Defaults.JAR_DIR.asString(), AppArguments.Defaults.JAR_FILE_NAME);
    }

    /// `App.main` honors `.zb`, so the produced jar path must come from the same
    /// configuration — not the hardcoded defaults, which diverge once `.zb`
    /// overrides `jar.dir` or `jar.file.name`.
    static Path configuredJarFile() {
        var jarDir = Configuration.JAR_DIR.get(AppArguments.Defaults.JAR_DIR.asString());
        var jarFileName = Configuration.JAR_FILE_NAME.get(AppArguments.Defaults.JAR_FILE_NAME);
        return Path.of(jarDir, jarFileName);
    }

    static void createZBJar() throws IOException {
        App.main();
        var manifest = loadManifest(configuredJarFile());
        if (manifest == null)
            throw new AssertionError("expected a manifest");
        var mainClass = manifest.getMainAttributes().getValue("Main-Class");
        if (!"airhacks.App".equals(mainClass))
            throw new AssertionError("expected Main-Class airhacks.App but got " + mainClass);
    }

    static void servicesConfigurationFileIsIncluded() throws IOException {
        var arguments = new AppArguments(
                Path.of("src/main/java"),
                Optional.of(Path.of("src/test/resources")),
                Path.of("zbo/test-classes"),
                AppArguments.Defaults.JAR_DIR.asPath(),
                AppArguments.Defaults.JAR_FILE_NAME,
                false,
                List.of());
        Build.perform(arguments);
        var metaINF = loadMetaInfServices(jarFile());
        if (metaINF.size() != 1)
            throw new AssertionError("expected 1 META-INF/services entry but got " + metaINF.size());
        var entry = metaINF.getFirst();
        if (!"META-INF/services/hello".equals(entry.name()))
            throw new AssertionError("expected META-INF/services/hello but got " + entry.name());
        if (!"duke".equals(entry.content()))
            throw new AssertionError("expected content duke but got " + entry.content());
    }

    static void temporaryClassesDirectoryIsDeleted() throws IOException {
        var tempDir = Files.createTempDirectory("test-zb-classes-");
        var arguments = new AppArguments(
                Path.of("src/main/java"),
                Optional.empty(),
                tempDir,
                AppArguments.Defaults.JAR_DIR.asPath(),
                AppArguments.Defaults.JAR_FILE_NAME,
                true,
                List.of());

        if (!Files.exists(tempDir))
            throw new AssertionError("temp dir should exist before build");
        Build.perform(arguments);
        if (Files.exists(tempDir))
            throw new AssertionError("temporary classes directory should have been deleted");
    }

    static void explicitClassesDirectoryIsNotDeleted() throws IOException {
        var explicitDir = Path.of("zbo/explicit-test-classes");
        Files.createDirectories(explicitDir);

        var arguments = new AppArguments(
                Path.of("src/main/java"),
                Optional.empty(),
                explicitDir,
                AppArguments.Defaults.JAR_DIR.asPath(),
                AppArguments.Defaults.JAR_FILE_NAME,
                false,
                List.of());

        if (!Files.exists(explicitDir))
            throw new AssertionError("explicit dir should exist before build");
        Build.perform(arguments);
        if (!Files.exists(explicitDir))
            throw new AssertionError("explicit classes directory should not be deleted");

        deleteRecursively(explicitDir);
    }

    /// End-to-end: a library JAR built on the fly, referenced via `classpath`,
    /// resolvable at compile time (javac `--class-path`) and at runtime
    /// (`java -jar` via the manifest `Class-Path`).
    static void buildWithClasspath() throws Exception {
        var project = Files.createTempDirectory("zb-cp-project");
        var libSources = project.resolve("lib-src");
        var libClasses = project.resolve("lib-classes");
        var lib = project.resolve("lib");
        var sources = project.resolve("src");
        var classes = project.resolve("classes");
        var jarDir = project.resolve("zbo");
        Files.createDirectories(libSources);
        Files.createDirectories(sources);

        var greeter = libSources.resolve("Greeter.java");
        Files.writeString(greeter, "public class Greeter { public static String greet() { return \"greetings from lib\"; } }");
        if (!Compiler.compile(List.of(greeter), libClasses))
            throw new AssertionError("expected library compilation to succeed");
        Packer.createJAR(project, libClasses, Optional.empty(), lib, "greeter.jar", Optional.empty());

        Files.writeString(sources.resolve("CpApp.java"),
                "public class CpApp { public static void main(String... args) { System.out.println(Greeter.greet()); } }");

        var arguments = new AppArguments(sources, Optional.empty(), classes, jarDir, "cp-app.jar", false,
                List.of(lib.resolve("greeter.jar")));
        if (!Build.perform(arguments))
            throw new AssertionError("expected build with classpath to succeed");

        var manifest = loadManifest(jarDir.resolve("cp-app.jar"));
        var classPath = manifest.getMainAttributes().getValue("Class-Path");
        if (!"../lib/greeter.jar".equals(classPath))
            throw new AssertionError("expected Class-Path ../lib/greeter.jar but got " + classPath);

        var process = new ProcessBuilder("java", "-jar", jarDir.resolve("cp-app.jar").toString())
                .redirectErrorStream(true)
                .start();
        var output = new String(process.getInputStream().readAllBytes());
        var exitCode = process.waitFor();
        if (exitCode != 0)
            throw new AssertionError("expected exit code 0 but got %d, output: %s".formatted(exitCode, output));
        if (!output.contains("greetings from lib"))
            throw new AssertionError("expected output from lib but got: " + output);

        deleteRecursively(project);
    }

    static Manifest loadManifest(Path jarFile) throws IOException {
        try (var jar = new JarFile(jarFile.toFile())) {
            return jar.getManifest();
        }
    }

    record JarEntryWithContent(String name, String content) {
        static JarEntryWithContent of(JarFile jar, JarEntry entry) {
            try (var is = jar.getInputStream(entry)) {
                return new JarEntryWithContent(entry.getName(), new String(is.readAllBytes()));
            } catch (IOException e) {
                throw new RuntimeException("Failed to read entry: " + entry.getName(), e);
            }
        }
    }

    static List<JarEntryWithContent> loadMetaInfServices(Path jarFile) throws IOException {
        try (var jar = new JarFile(jarFile.toFile())) {
            return jar.stream()
                    .filter(entry -> entry.getName().startsWith("META-INF/services"))
                    .map(entry -> JarEntryWithContent.of(jar, entry))
                    .toList();
        }
    }

    static void deleteRecursively(Path directory) throws IOException {
        try (var paths = Files.walk(directory)) {
            paths.sorted(Comparator.reverseOrder())
                 .forEach(path -> {
                     try {
                         Files.deleteIfExists(path);
                     } catch (IOException e) {
                         // ignore cleanup errors
                     }
                 });
        }
    }
}
