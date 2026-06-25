package airhacks.zb.packer.control;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Optional;
import java.util.jar.JarOutputStream;

public class PackerTest {

    public static void main(String... args) throws IOException {
        addManifest();
        pathToJavaPackage();
        readVersionFromResourcesWhenRootMissing();
        readVersionFromProjectRootPreferredOverResources();
        readVersionFromProjectRootWhenNoResources();
        readVersionWhenAbsent();
        manifestIncludesImplementationVersion();
        manifestOmitsImplementationVersionWhenAbsent();
        System.out.println("PackerTest passed");
    }

    static void addManifest() throws IOException {
        var mainClass = Path.of("src", "main", "java", "airhacks", "App.java");
        var jarFile = Files.createTempFile("zb-test", ".jar");
        try (var jos = new JarOutputStream(Files.newOutputStream(jarFile, StandardOpenOption.CREATE))) {
            Packer.addManifest(jarFile.getParent(), jos, mainClass, Optional.empty());
        }
        Files.deleteIfExists(jarFile);
    }

    static void pathToJavaPackage() {
        var path = Path.of("airhacks", "App");
        var javaPackage = Packer.pathToJavaPackage(path);
        eq("airhacks.App", javaPackage);
    }

    static void readVersionFromResourcesWhenRootMissing() throws IOException {
        var projectRoot = Files.createTempDirectory("zb-root");
        var resources = Files.createTempDirectory("zb-resources");
        Files.writeString(resources.resolve("version.txt"), "2026.04.26.01\n");
        var version = Packer.readVersion(projectRoot, Optional.of(resources));
        contains(version, "2026.04.26.01");
    }

    static void readVersionFromProjectRootPreferredOverResources() throws IOException {
        var projectRoot = Files.createTempDirectory("zb-root");
        var resources = Files.createTempDirectory("zb-resources");
        Files.writeString(projectRoot.resolve("version.txt"), "2026.05.26.01\n");
        Files.writeString(resources.resolve("version.txt"), "2026.04.26.01\n");
        var version = Packer.readVersion(projectRoot, Optional.of(resources));
        contains(version, "2026.05.26.01");
    }

    static void readVersionFromProjectRootWhenNoResources() throws IOException {
        var projectRoot = Files.createTempDirectory("zb-root");
        Files.writeString(projectRoot.resolve("version.txt"), "2026.05.26.01\n");
        var version = Packer.readVersion(projectRoot, Optional.empty());
        contains(version, "2026.05.26.01");
    }

    static void readVersionWhenAbsent() throws IOException {
        var projectRoot = Files.createTempDirectory("zb-root");
        var resources = Files.createTempDirectory("zb-resources");
        var version = Packer.readVersion(projectRoot, Optional.of(resources));
        if (version.isPresent())
            throw new AssertionError("expected empty version but got " + version.get());
    }

    static void manifestIncludesImplementationVersion() {
        var manifest = Manifestor.manifest("airhacks.App", Optional.of("2026.04.26.01"));
        var attribute = manifest.getMainAttributes().getValue("Implementation-Version");
        eq("2026.04.26.01", attribute);
    }

    static void manifestOmitsImplementationVersionWhenAbsent() {
        var manifest = Manifestor.manifest("airhacks.App", Optional.empty());
        var attribute = manifest.getMainAttributes().getValue("Implementation-Version");
        if (attribute != null)
            throw new AssertionError("expected no Implementation-Version but got " + attribute);
    }

    static void eq(Object expected, Object actual) {
        if (!java.util.Objects.equals(expected, actual))
            throw new AssertionError("expected [%s] but got [%s]".formatted(expected, actual));
    }

    static void contains(Optional<String> version, String expected) {
        if (version.isEmpty())
            throw new AssertionError("expected version containing [%s] but was empty".formatted(expected));
        if (!version.get().contains(expected))
            throw new AssertionError("expected [%s] to contain [%s]".formatted(version.get(), expected));
    }
}
