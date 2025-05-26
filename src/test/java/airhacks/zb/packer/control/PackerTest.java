package airhacks.zb.packer.control;

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
}
