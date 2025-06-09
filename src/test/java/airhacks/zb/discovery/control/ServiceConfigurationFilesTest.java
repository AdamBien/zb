package airhacks.zb.discovery.control;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ServiceConfigurationFilesTest {

    @Test
    public void findindServiceConfigurationFile() {
        var rootDirectory = Path.of("src/test/resources");
        var serviceConfigurationFile = ServiceConfigurationFiles.findServiceConfigurationFiles(rootDirectory);
        assertThat(serviceConfigurationFile).hasSize(1);
        var expected = Path.of("META-INF","services","hello");
        assertThat(serviceConfigurationFile.get(0)).endsWith(expected);
    }
}
