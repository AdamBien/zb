package airhacks.zb.discovery.control;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;


public class JavaFilesTest {

    @Test
    void findMain() {
        var src = SourceLocator.detectSourceDirectory().orElseThrow();
        var javaFiles = JavaFiles.findFrom(src);
        var mainClasses = JavaFiles.findMain(javaFiles);
        System.out.println(mainClasses);
        assertThat(mainClasses).hasSize(1);
        assertThat(mainClasses.getFirst()).endsWith(Path.of("airhacks","App.java"));

    }
    
}
