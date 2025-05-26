package airhacks.zb.packer.control;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Optional;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

public interface Packer {


    static void archive(Path rootClassesDirectory, Path rootJARDirectory, String jarFileName,Optional<Path> mainClass) throws IOException {
        var jarFile = rootJARDirectory.resolve(jarFileName);
        Files.createDirectories(rootJARDirectory);
        try (var fos = Files.newOutputStream(jarFile,StandardOpenOption.CREATE);
                var jos = new JarOutputStream(fos)) {
            try (var paths = Files.walk(rootClassesDirectory)) {
                paths.filter(path -> path.toString().endsWith(".class"))
                        .forEach(path -> addEntry(rootClassesDirectory, jos, path));
            }
           mainClass.ifPresent(mc -> addManifest(rootClassesDirectory, jos, mc));
        }
    }

    static void addManifest(Path rootClassesDirectory, JarOutputStream jos,Path mainClass)  {
        var fqn = removeRootDirectory(rootClassesDirectory, mainClass);
        var javaPackage =  pathToJavaPackage(fqn);
        var fullyQualifiedClassName = javaPackage.replace(".java", "");
        var manifest = Manifestor.manifest(fullyQualifiedClassName);
        try {
            var entry = new JarEntry("META-INF/MANIFEST.MF");
            jos.putNextEntry(entry);
            manifest.write(jos);
            jos.closeEntry();
        } catch (IOException e) {
            throw new RuntimeException("Failed to add manifest to JAR: ", e);
        }
    }

    static String pathToJavaPackage(Path path){
        return path.toString().replace(File.separator, ".");
    }

    static Path removeRootDirectory(Path rootPath,Path classFile) {
        return rootPath.relativize(classFile);
    }

    static void addEntry(Path rootClassesDirectory, JarOutputStream jos, Path relativePath, byte[] content)  {
            try {
                var path = rootClassesDirectory.relativize(relativePath);
                var entry = new JarEntry(path.toString());
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
            addEntry(rootClassesDirectory, jarOutputStream, relativePath, content);
        } catch (IOException e) {
            throw new RuntimeException("Failed to add file to JAR: " + path, e);
        }

    }
}
