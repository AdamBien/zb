package airhacks.zb.discovery.control;

import java.io.IOException;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class JavaFilesTest {

    @Test
    void findMain() throws IOException{
        var src = Path.of("src/main/java");
        var javaFiles = JavaFiles.findFrom(src);
        var mainClasses = JavaFiles.findMain(javaFiles);
        System.out.println(mainClasses);
        assertThat(mainClasses).hasSize(1);
        assertThat(mainClasses.getFirst()).endsWith(Path.of("airhacks","App.java"));

    }
    
}
