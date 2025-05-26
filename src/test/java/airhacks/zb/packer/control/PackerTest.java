package airhacks.zb.packer.control;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.jar.JarOutputStream;

import org.junit.jupiter.api.Test;

public class PackerTest {
    @Test
    void addManifest() throws IOException {
        var mainClass = Path.of("src","main","java","airhacks","App.java");
        var outputDir = Path.of("target","test");
        var jarFile = Path.of("target","test.jar");
        try(var jos = new JarOutputStream(Files.newOutputStream(jarFile,StandardOpenOption.CREATE))){
            Packer.addManifest(outputDir,jos,mainClass);
        }
    }

    @Test
    void removeRootDirectory() {
        var rootPath = Path.of("src","main","java");
        var classFile = Path.of("src","main","java","airhacks","App.java");
        var fqn = Packer.removeRootDirectory(rootPath, classFile);
        assertThat(fqn).isEqualTo(Path.of("airhacks","App.java"));
    }

    @Test
    void pathToJavaPackage() {
        var path = Path.of("airhacks","App");
        var javaPackage = Packer.pathToJavaPackage(path);
        assertThat(javaPackage).isEqualTo("airhacks.App");
    }
}
