package airhacks.zb.discovery.control;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public interface SourceLocator {

    List<String> candidates = List.of("src/main/java", "src/", ".");

    static Optional<Path> detectSourceDirectory() {
        var currentDir = Path.of(".");
        return detectSourceDirectory(currentDir);
    }

    static Optional<Path> detectSourceDirectory(Path rootDir) {
        return candidates
                .stream()
                .map(rootDir::resolve)
                .filter(SourceLocator::directoryExists)
                .findFirst();
    }

    static boolean directoryExists(Path directory) {
        return Files.exists(directory);
    }


}
