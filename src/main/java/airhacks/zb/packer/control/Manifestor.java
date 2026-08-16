package airhacks.zb.packer.control;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.jar.Manifest;

public interface Manifestor {


    static Manifest manifest(String mainClass, Optional<String> version) {
        return manifest(mainClass, version, List.of());
    }

    static Manifest manifest(String mainClass, Optional<String> version, List<String> classPath) {
        var manifest = new Manifest();
        var attributes = manifest.getMainAttributes();
        attributes.putValue("Manifest-Version", "1.0");
        attributes.putValue("Main-Class", mainClass);
        version.ifPresent(v -> attributes.putValue("Implementation-Version", v));
        if (!classPath.isEmpty()) {
            attributes.putValue("Class-Path", String.join(" ", classPath));
        }
        return manifest;
    }

    /// Class-Path entries are URLs relative to the JAR's own directory, '/'-separated.
    static List<String> relativeToJar(Path jarDirectory, List<Path> classpath) {
        var jarDir = jarDirectory.toAbsolutePath().normalize();
        return classpath.stream()
                .map(entry -> jarDir.relativize(entry.toAbsolutePath().normalize()).toString())
                .map(entry -> entry.replace(File.separatorChar, '/'))
                .toList();
    }

}
