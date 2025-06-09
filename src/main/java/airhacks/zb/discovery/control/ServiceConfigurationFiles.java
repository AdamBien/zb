package airhacks.zb.discovery.control;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public interface ServiceConfigurationFiles {
    
    /**
     * Finds the service configuration file (https://docs.oracle.com/javase/8/docs/technotes/guides/jar/jar.html#The_META-INF_directory) in the source directory.
     * @param rootDirectory the root of the source directory
     * @return the path to the service configuration file, if exists
     */
    public static List<Path> findServiceConfigurationFiles(Path rootDirectory) {
        try (var paths = Files.walk(rootDirectory)) {
            return paths
                    .filter(ServiceConfigurationFiles::isServiceConfigurationFile)
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static boolean isServiceConfigurationFile(Path path) {
        var servicesDirectory = Path.of("META-INF","services");
        return Files.isRegularFile(path) && path.toString().contains(servicesDirectory.toString());
    }
}
