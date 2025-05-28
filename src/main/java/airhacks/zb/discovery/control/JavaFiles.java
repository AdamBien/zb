package airhacks.zb.discovery.control;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import airhacks.zb.log.boundary.Log;

public interface JavaFiles {

    static List<Path> findFrom(Path rootDir) throws IOException {
        try (var paths = Files.walk(rootDir)) {
            return paths
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".java"))
                    .toList();
        } catch (IOException e) {
            Log.error("❌ Failed to read directory: " + rootDir.toAbsolutePath(), e);
            throw new IOException("Unable to access directory: " + rootDir + ". " + e.getMessage(), e);
        }
    }

    static List<Path> findMain(List<Path> javaPaths) {
        return javaPaths
                .stream()
                .filter(JavaFiles::containsMainMethod)
                .toList();
    }

    static Optional<Path> findMainClass(List<Path> javaPaths) {
        var mainClasses = findMain(javaPaths);
        
        if (mainClasses.isEmpty()) {
            Log.warning("No main class found");
            return Optional.empty();
        }
        if (mainClasses.size() > 1) {
            Log.warning("Multiple main classes found");
            System.exit(0);
        }
        return Optional.of(mainClasses.getFirst());

    }

    static boolean containsMainMethod(Path path) {
        return contentContains(path, "void main(");
    }

    static boolean contentContains(Path path, String content) {
        try {
            return Files.readString(path)
            .lines()
            .filter(line -> line.contains(content))
            .filter(line -> !line.startsWith("//"))
            .filter(line -> !line.startsWith("/*"))
            .filter(line -> !line.startsWith("/**"))
            .filter(line -> !line.contains("\""))
            .findAny()
            .isPresent();
        } catch (IOException e) {
            Log.error("Failed to read file: " + path, e);
            return false;
        }
    }
}
