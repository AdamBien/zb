package airhacks;

import java.io.IOException;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

import airhacks.zb.packer.control.JarLoader;
import static airhacks.App.Defaults.*;

class AppTest {

    @Test
    void argumentsWithDefaultValues() {
        var arguments = App.Arguments.from();
        
        assertThat(arguments.sourceDirectory()).isEqualTo(SOURCE_DIR.asPath());
        assertThat(arguments.classesDirectory()).isEqualTo(CLASSES_DIR.asPath());
        assertThat(arguments.jarDirectory()).isEqualTo(JAR_DIR.asPath());
        assertThat(arguments.jarFileName()).isEqualTo("zb.jar");
    }

    @Test
    void argumentsWithCustomValues() {
        var arguments = App.Arguments.from(
            "custom/src",
            "custom/classes",
            "custom/jar",
            "custom.jar"
        );
        
        assertThat(arguments.sourceDirectory()).isEqualTo(Path.of("custom/src"));
        assertThat(arguments.classesDirectory()).isEqualTo(Path.of("custom/classes"));
        assertThat(arguments.jarDirectory()).isEqualTo(Path.of("custom/jar"));
        assertThat(arguments.jarFileName()).isEqualTo("custom.jar");
    }

    @Test
    void argumentsWithPartialCustomValues() {
        var arguments = App.Arguments.from("custom/src");
        
        assertThat(arguments.sourceDirectory()).isEqualTo(Path.of("custom/src"));
        assertThat(arguments.classesDirectory()).isEqualTo(CLASSES_DIR.asPath());
        assertThat(arguments.jarDirectory()).isEqualTo(JAR_DIR.asPath());
        assertThat(arguments.jarFileName()).isEqualTo("zb.jar");
    }


    @Test
    void createZBJar() throws IOException {
        App.main();

        var manifest = JarLoader.loadManifest(Path.of(JAR_DIR.asString(),"zb.jar"));
        assertThat(manifest).isNotNull();
        assertThat(manifest.getMainAttributes().getValue("Main-Class")).isEqualTo("airhacks.App");
    }
} 