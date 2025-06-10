package airhacks.zb.discovery.control;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public interface ResourceLocator {

    List<String> candidates = List.of("src/main/resources",  ".");

    static Optional<Path> detectResourcesDirectory() {
        var currentDir = Path.of(".");
        return detectResourcesDirectory(currentDir);
    }

    static Optional<Path> detectResourcesDirectory(Path rootDir) {
        return candidates
                .stream()
                .map(rootDir::resolve)
                .filter(Files::exists)
                .findFirst();
    }
    
}
