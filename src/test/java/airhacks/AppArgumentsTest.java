package airhacks;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import airhacks.zb.discovery.control.SourceLocator;


public class AppArgumentsTest {


    @Test
    void argumentsWithDefaultValues() {
        var arguments = AppArguments.from();
        
        assertThat(arguments.sourcesDirectory()).isEqualTo(SourceLocator.detectSourceDirectory().orElseThrow());
        assertThat(arguments.classesDirectory()).isEqualTo(AppArguments.Defaults.CLASSES_DIR.asPath());
        assertThat(arguments.jarDirectory()).isEqualTo(AppArguments.Defaults.JAR_DIR.asPath());
        assertThat(arguments.jarFileName()).isEqualTo(AppArguments.Defaults.JAR_FILE_NAME);
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
