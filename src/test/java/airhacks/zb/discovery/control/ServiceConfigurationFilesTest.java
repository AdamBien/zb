package airhacks.zb.discovery.control;

import java.nio.file.Path;

public class ServiceConfigurationFilesTest {

    public static void main(String... args) {
        findingServiceConfigurationFile();
        System.out.println("ServiceConfigurationFilesTest passed");
    }

    static void findingServiceConfigurationFile() {
        var rootDirectory = Path.of("src/test/resources");
        var serviceConfigurationFiles = ServiceConfigurationFiles.findServiceConfigurationFiles(rootDirectory);
        if (serviceConfigurationFiles.size() != 1)
            throw new AssertionError("expected 1 service configuration file but got " + serviceConfigurationFiles.size());
        var expected = Path.of("META-INF", "services", "hello");
        var actual = serviceConfigurationFiles.get(0);
        if (!actual.endsWith(expected))
            throw new AssertionError("expected path ending with " + expected + " but got " + actual);
    }
}
