package airhacks;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import airhacks.zb.packer.control.JarLoader;

class AppTest {

    @Test
    void argumentsWithPartialCustomValues() {
        var arguments = AppArguments.from("custom/src");
        
        assertThat(arguments.sourcesDirectory()).isEqualTo(Path.of("custom/src"));
        assertThat(arguments.classesDirectory()).isEqualTo(AppArguments.Defaults.CLASSES_DIR.asPath());
        assertThat(arguments.jarDirectory()).isEqualTo(AppArguments.Defaults.JAR_DIR.asPath());
        assertThat(arguments.jarFileName()).isEqualTo(AppArguments.Defaults.JAR_FILE_NAME);
    }


    @Test
    void createZBJar() throws IOException {
        App.main();
        var manifest = JarLoader.loadManifest(Path.of(AppArguments.Defaults.JAR_DIR.asString(),AppArguments.Defaults.JAR_FILE_NAME));
        assertThat(manifest).isNotNull();
        assertThat(manifest.getMainAttributes().getValue("Main-Class")).isEqualTo("airhacks.App");
    }

    @Test
    void servicesConfigurationFileIsIncluded() throws IOException {
        var arguments = new AppArguments(
            Path.of("src/main/java"),
            Optional.of(Path.of("src/test/resources")),
            AppArguments.Defaults.CLASSES_DIR.asPath(),
            AppArguments.Defaults.JAR_DIR.asPath(),
            AppArguments.Defaults.JAR_FILE_NAME
        );
        App.build(arguments);
        var metaINF = JarLoader.loadMetaInfServices(Path.of(AppArguments.Defaults.JAR_DIR.asString(),AppArguments.Defaults.JAR_FILE_NAME));
        assertThat(metaINF).hasSize(1);
        var entry = metaINF.getFirst();
        assertThat(entry.name()).isEqualTo("META-INF/services/hello");
        assertThat(entry.content()).isEqualTo("duke");
    }


} 