package airhacks.zb.discovery.control;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class SourceLocationTest {

    @Test
    public void sourcesInTheRootDirectory() {
        var sourceDirectory = SourceLocator.detectSourceDirectory(Path.of("."));
        assertThat(sourceDirectory).isPresent();
        assertThat(sourceDirectory.get().toString()).endsWith("src/main/java");
    }

}
