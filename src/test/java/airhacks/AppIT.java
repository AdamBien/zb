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

public class AppIT {

    public static void main(String... args) throws IOException {
        createZBJar();
        servicesConfigurationFileIsIncluded();
        temporaryClassesDirectoryIsDeleted();
        explicitClassesDirectoryIsNotDeleted();
        System.out.println("AppIT passed");
    }

    static Path jarFile() {
        return Path.of(AppArguments.Defaults.JAR_DIR.asString(), AppArguments.Defaults.JAR_FILE_NAME);
    }

    static void createZBJar() throws IOException {
        App.main();
        var manifest = loadManifest(jarFile());
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
                false);
        App.build(arguments);
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
                true);

        if (!Files.exists(tempDir))
            throw new AssertionError("temp dir should exist before build");
        App.build(arguments);
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
                false);

        if (!Files.exists(explicitDir))
            throw new AssertionError("explicit dir should exist before build");
        App.build(arguments);
        if (!Files.exists(explicitDir))
            throw new AssertionError("explicit classes directory should not be deleted");

        deleteRecursively(explicitDir);
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
