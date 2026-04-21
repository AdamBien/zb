package airhacks.zb.discovery.control;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import airhacks.zb.hints.boundary.UserHint;
import airhacks.zb.log.control.Log;

public interface JavaFiles {

    static List<Path> findFrom(Path rootDir) {
        try (var paths = Files.walk(rootDir)) {
            return paths
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".java"))
                    .toList();
        } catch (IOException e) {
            Log.error("❌ Failed to read directory: " + rootDir.toAbsolutePath(), e);
            UserHint.directoryAccessError(rootDir, e);
            return List.of();
        }
    }

    static List<Path> findMain(List<Path> javaPaths) {
        return javaPaths
                .stream()
                .filter(JavaFiles::containsMainMethod)
                .toList();
    }

    static Optional<Path> findMainClass(List<Path> javaPaths, Optional<String> configuredMainClass) {
        var mainClasses = findMain(javaPaths);

        if (mainClasses.isEmpty()) {
            Log.warning("No main class found");
            return Optional.empty();
        }
        if (mainClasses.size() > 1) {
            if (configuredMainClass.isEmpty()) {
                Log.warning("Multiple main classes found: " + mainClasses);
                Log.user("💡 Configure 'main.class' in .zb to select one, e.g. main.class=com.example.App");
                System.exit(0);
            }
            var className = configuredMainClass.get();
            var match = mainClasses.stream()
                    .filter(path -> pathMatchesClassName(path, className))
                    .findFirst();
            if (match.isEmpty()) {
                Log.warning("Configured main class '%s' not found among: %s".formatted(className, mainClasses));
                System.exit(0);
            }
            return match;
        }
        return Optional.of(mainClasses.getFirst());

    }

    static boolean pathMatchesClassName(Path path, String className) {
        var pathString = path.toString().replace("/", ".").replace("\\", ".");
        return pathString.endsWith(className + ".java");
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
