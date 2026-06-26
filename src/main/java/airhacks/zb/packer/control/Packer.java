package airhacks.zb.packer.control;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Optional;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

/**
 * https://docs.oracle.com/javase/8/docs/technotes/guides/jar/jar.html
 * JAR file creator
 */
public interface Packer {

    String VERSION_FILE = "version.txt";

    static void createJAR(Path projectRoot, Path rootClassesDirectory, Optional<Path> rootResourcesDirectory, Path rootJARDirectory, String jarFileName, Optional<Path> mainClass) throws IOException {
        var jarFile = rootJARDirectory.resolve(jarFileName);
        Files.createDirectories(rootJARDirectory);
        Files.deleteIfExists(jarFile);

        var versionFile = locateVersionFile(projectRoot, rootResourcesDirectory);
        var version = versionFile.flatMap(Packer::readVersionContent);

        try (var fos = Files.newOutputStream(jarFile, StandardOpenOption.CREATE_NEW);
             var jos = new JarOutputStream(fos)) {

            mainClass.ifPresent(mc -> addManifest(rootClassesDirectory, jos, mc, version));

            try (var paths = Files.walk(rootClassesDirectory)) {
                paths.filter(path -> path.toString().endsWith(".class"))
                     .forEach(path -> addEntry(rootClassesDirectory, jos, path));
            }
            if(rootResourcesDirectory.isPresent()) {
                var dir = rootResourcesDirectory.get();
                try (var paths = Files.walk(dir)) {
                    paths.filter(Files::isRegularFile)
                         .filter(path -> !path.equals(dir.resolve(VERSION_FILE)))
                         .forEach(path -> addEntry(dir, jos, path));
                }
            }
            versionFile.ifPresent(file -> addVersionEntry(jos, file));
        }
    }

    static Optional<Path> locateVersionFile(Path projectRoot, Optional<Path> resourcesDirectory) {
        var rootCandidate = projectRoot.resolve(VERSION_FILE);
        if (Files.isRegularFile(rootCandidate)) {
            return Optional.of(rootCandidate);
        }
        return resourcesDirectory
                .map(dir -> dir.resolve(VERSION_FILE))
                .filter(Files::isRegularFile);
    }

    static Optional<String> readVersion(Path projectRoot, Optional<Path> resourcesDirectory) {
        return locateVersionFile(projectRoot, resourcesDirectory).flatMap(Packer::readVersionContent);
    }

    static Optional<String> readVersionContent(Path versionFile) {
        try {
            return Optional.of(Files.readString(versionFile).trim());
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    static void addVersionEntry(JarOutputStream jos, Path versionFile) {
        try {
            addEntry(jos, Path.of(VERSION_FILE), Files.readAllBytes(versionFile));
        } catch (IOException e) {
            throw new RuntimeException("Failed to add version.txt to JAR: " + versionFile, e);
        }
    }

    static void addManifest(Path rootClassesDirectory, JarOutputStream jos, Path mainClass, Optional<String> version) {
        var javaPackage = pathToJavaPackage(mainClass);
        var fullyQualifiedClassName = javaPackage.replace(".java", "");
        var manifest = Manifestor.manifest(fullyQualifiedClassName, version);
        try {
            var entry = new JarEntry("META-INF/MANIFEST.MF");
            jos.putNextEntry(entry);
            manifest.write(jos);
            jos.closeEntry();
        } catch (IOException e) {
            throw new RuntimeException("Failed to add manifest to JAR: ", e);
        }
    }

    static String pathToJavaPackage(Path path) {
        return path.toString().replace(File.separator, ".");
    }

    static void addEntry(JarOutputStream jos, Path relativePath, byte[] content) {
        try {
            // JAR/ZIP entry names must use '/' regardless of the host OS separator
            var slashedPath = relativePath.toString().replace(File.separatorChar, '/');
            var entry = new JarEntry(slashedPath);
            jos.putNextEntry(entry);
            jos.write(content);
            jos.closeEntry();
        } catch (IOException e) {
            throw new RuntimeException("Failed to add file to JAR: " + relativePath, e);
        }
    }

    static void addEntry(Path rootClassesDirectory, JarOutputStream jarOutputStream, Path path) {
        var relativePath = rootClassesDirectory.relativize(path);
        try {
            var content = Files.readAllBytes(path);
            addEntry(jarOutputStream, relativePath, content);
        } catch (IOException e) {
            throw new RuntimeException("Failed to add file to JAR: " + path, e);
        }
    }
}
