package airhacks;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import airhacks.App.Defaults;
import airhacks.zb.discovery.control.SourceLocator;

import static airhacks.App.Defaults.CLASSES_DIR;
import static airhacks.App.Defaults.JAR_DIR;
import static org.assertj.core.api.Assertions.assertThat;


public class AppArgumentsTest {


    @Test
    void argumentsWithDefaultValues() {
        var arguments = AppArguments.from();
        
        assertThat(arguments.sourcesDirectory()).isEqualTo(SourceLocator.detectSourceDirectory().orElseThrow());
        assertThat(arguments.classesDirectory()).isEqualTo(CLASSES_DIR.asPath());
        assertThat(arguments.jarDirectory()).isEqualTo(JAR_DIR.asPath());
        assertThat(arguments.jarFileName()).isEqualTo(Defaults.JAR_FILE_NAME);
    }

    @Test
    void argumentsWithCustomValues() {
        var arguments = AppArguments.from(
            "custom/src",
            "custom/classes",
            "custom/jar",
            "custom.jar"
        );
        
        assertThat(arguments.sourcesDirectory()).isEqualTo(Path.of("custom/src"));
        assertThat(arguments.classesDirectory()).isEqualTo(Path.of("custom/classes"));
        assertThat(arguments.jarDirectory()).isEqualTo(Path.of("custom/jar"));
        assertThat(arguments.jarFileName()).isEqualTo("custom.jar");
    }

}
