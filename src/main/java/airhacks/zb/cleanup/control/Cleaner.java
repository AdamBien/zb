package airhacks.zb.cleanup.control;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public interface Cleaner {

    static void cleanClasses(Path classesDirectory) {
        try {
            Files.walk(classesDirectory)
                    .sorted((a, b) -> b.compareTo(a))
                    .forEach(path -> {
                        try {
                            Files.delete(path);
                        } catch (IOException e) {
                            throw new RuntimeException("Failed to delete: " + path, e);
                        }
                    });
        } catch (IOException e) {
            throw new RuntimeException("Failed to clean directory: " + classesDirectory, e);
        }
    }
}
