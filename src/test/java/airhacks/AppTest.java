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
            Path.of("zbo/test-classes"),
            AppArguments.Defaults.JAR_DIR.asPath(),
            AppArguments.Defaults.JAR_FILE_NAME,
            false
        );
        App.build(arguments);
        var metaINF = JarLoader.loadMetaInfServices(Path.of(AppArguments.Defaults.JAR_DIR.asString(),AppArguments.Defaults.JAR_FILE_NAME));
        assertThat(metaINF).hasSize(1);
        var entry = metaINF.getFirst();
        assertThat(entry.name()).isEqualTo("META-INF/services/hello");
        assertThat(entry.content()).isEqualTo("duke");
    }
    
    @Test
    void temporaryClassesDirectoryIsDeleted() throws IOException {
        var tempDir = java.nio.file.Files.createTempDirectory("test-zb-classes-");
        var arguments = new AppArguments(
            Path.of("src/main/java"),
            Optional.empty(),
            tempDir,
            AppArguments.Defaults.JAR_DIR.asPath(),
            AppArguments.Defaults.JAR_FILE_NAME,
            true
        );
        
        assertThat(java.nio.file.Files.exists(tempDir)).isTrue();
        App.build(arguments);
        assertThat(java.nio.file.Files.exists(tempDir)).isFalse();
    }
    
    @Test
    void explicitClassesDirectoryIsNotDeleted() throws IOException {
        var explicitDir = Path.of("zbo/explicit-test-classes");
        java.nio.file.Files.createDirectories(explicitDir);
        
        var arguments = new AppArguments(
            Path.of("src/main/java"),
            Optional.empty(),
            explicitDir,
            AppArguments.Defaults.JAR_DIR.asPath(),
            AppArguments.Defaults.JAR_FILE_NAME,
            false
        );
        
        assertThat(java.nio.file.Files.exists(explicitDir)).isTrue();
        App.build(arguments);
        assertThat(java.nio.file.Files.exists(explicitDir)).isTrue();
        
        // Clean up manually since it won't be deleted automatically
        java.nio.file.Files.walk(explicitDir)
            .sorted(java.util.Comparator.reverseOrder())
            .forEach(path -> {
                try {
                    java.nio.file.Files.deleteIfExists(path);
                } catch (IOException e) {
                    // Ignore
                }
            });
    }


} 