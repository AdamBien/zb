package airhacks.zb.cleanup.control;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

import airhacks.zb.hints.boundary.UserHint;

public interface Cleaner {

    static void cleanClasses(Path classesDirectory) {
        try (var stream = Files.walk(classesDirectory)) {
                    stream.sorted(Comparator.reverseOrder())
                    .forEach(Cleaner::deleteFile);
        } catch (IOException e) {
            UserHint.error("Failed to clean classes directory: " + classesDirectory, e);
        }
    }

    static void deleteFile(Path path) {
        try {
            Files.delete(path);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete: " + path, e);
        }
    }
}
