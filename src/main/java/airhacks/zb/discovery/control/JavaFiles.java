package airhacks.zb.discovery.control;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public interface JavaFiles {

    static List<Path> findFrom(Path rootDir) throws IOException {
        try (var paths = Files.walk(rootDir)) {
            return paths
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".java"))
                    .toList();
        }

    }
}
