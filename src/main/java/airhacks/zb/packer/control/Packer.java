package airhacks.zb.packer.control;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.jar.JarOutputStream;

public interface Packer {

    static void archive(Path rootClassesDirectory, Path rootJARDirectory, String jarFileName) {
        var jarFile = rootJARDirectory.resolve(jarFileName);
        try (var fos = Files.newOutputStream(jarFile);
                var jos = new JarOutputStream(fos)) {
            try (var paths = Files.walk(rootClassesDirectory)) {
                paths.filter(path -> path.toString().endsWith(".class"))
                        .forEach(path -> addEntry(rootClassesDirectory, jos, path));
            }
        } catch (java.io.IOException e) {
            throw new RuntimeException("Failed to create JAR file", e);
        }
    }

    static void addEntry(Path rootClassesDirectory, JarOutputStream jarOutputStream, Path path) {
        try {
            var relativePath = rootClassesDirectory.relativize(path);
            var entry = new java.util.jar.JarEntry(relativePath.toString());
            jarOutputStream.putNextEntry(entry);
            jarOutputStream.write(Files.readAllBytes(path));
            jarOutputStream.closeEntry();
        } catch (java.io.IOException e) {
            throw new RuntimeException("Failed to add file to JAR: " + path, e);
        }
    }
}
