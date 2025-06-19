package airhacks.zb.prereqs.control;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import airhacks.zb.log.boundary.Log;

public interface Directories {

    static Path createIfNotExists(Path directory) {
        try {
            Files.createDirectories(directory);
        } catch (IOException e) {
            Log.error("Failed to create directory:  ",e);
        }
        return directory;
    }
}
