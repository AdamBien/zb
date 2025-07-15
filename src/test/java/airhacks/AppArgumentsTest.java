package airhacks;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import airhacks.zb.discovery.control.SourceLocator;


public class AppArgumentsTest {


    @Test
    void argumentsWithDefaultValues() {
        var arguments = AppArguments.from();
        
        assertThat(arguments.sourcesDirectory()).isEqualTo(SourceLocator.detectSourceDirectory().orElseThrow());
        // When using default [temp.dir], a temporary directory is created
        assertThat(arguments.classesDirectory().toString()).isNotEqualTo(AppArguments.TEMP_DIR_MARKER);
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
    
    @Test
    void temporaryDirectoryIsCreatedWhenTempDirMarkerIsUsed() {
        var tempDir = AppArguments.createTempDirectory();
        
        assertThat(tempDir).isNotNull();
        assertThat(Files.exists(tempDir)).isTrue();
        assertThat(Files.isDirectory(tempDir)).isTrue();
        assertThat(tempDir.toString()).contains("zb-classes-");
        
        // Clean up
        try {
            Files.deleteIfExists(tempDir);
        } catch (Exception e) {
            // Ignore cleanup errors in test
        }
    }
    
    @Test
    void defaultArgumentsCreateTempDirectory() {
        var arguments = AppArguments.from();
        
        assertThat(arguments.classesDirectory()).isNotNull();
        assertThat(Files.exists(arguments.classesDirectory())).isTrue();
        assertThat(Files.isDirectory(arguments.classesDirectory())).isTrue();
        assertThat(arguments.classesDirectory().toString()).contains("zb-classes-");
        
        // Clean up
        try {
            Files.deleteIfExists(arguments.classesDirectory());
        } catch (Exception e) {
            // Ignore cleanup errors in test
        }
    }
    
    @Test
    void explicitlyConfiguredClassesDirectoryIsUsedAsIs() {
        var explicitClassesDir = "classes-dir";
        var arguments = AppArguments.from("src/main/java", explicitClassesDir);
        
        assertThat(arguments.classesDirectory()).isEqualTo(Path.of(explicitClassesDir));
        assertThat(arguments.classesDirectory().toString()).isEqualTo(explicitClassesDir);
    }
    
    @Test
    void tempDirMarkerInCommandLineCreatesTempDirectory() {
        var arguments = AppArguments.from("src/main/java", "[temp.dir]");
        
        assertThat(arguments.classesDirectory()).isNotNull();
        assertThat(Files.exists(arguments.classesDirectory())).isTrue();
        assertThat(Files.isDirectory(arguments.classesDirectory())).isTrue();
        assertThat(arguments.classesDirectory().toString()).contains("zb-classes-");
        
        // Clean up
        try {
            Files.deleteIfExists(arguments.classesDirectory());
        } catch (Exception e) {
            // Ignore cleanup errors in test
        }
    }

}
