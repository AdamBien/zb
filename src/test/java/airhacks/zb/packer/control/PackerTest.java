package airhacks.zb.packer.control;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Optional;
import java.util.jar.JarOutputStream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class PackerTest {
    @Test
    void addManifest() throws IOException {
        var mainClass = Path.of("src","main","java","airhacks","App.java");
        var outputDir = Path.of("target","test");
        var jarFile = Path.of("target","test.jar");
        try(var jos = new JarOutputStream(Files.newOutputStream(jarFile,StandardOpenOption.CREATE))){
            Packer.addManifest(outputDir,jos,mainClass,Optional.empty());
        }
    }

    @Test
    void pathToJavaPackage() {
        var path = Path.of("airhacks","App");
        var javaPackage = Packer.pathToJavaPackage(path);
        assertThat(javaPackage).isEqualTo("airhacks.App");
    }

    @Test
    void readVersionWhenPresent(@TempDir Path resources) throws IOException {
        Files.writeString(resources.resolve("version.txt"), "2026.04.26.01\n");
        var version = Packer.readVersion(resources);
        assertThat(version).contains("2026.04.26.01");
    }

    @Test
    void readVersionWhenAbsent(@TempDir Path resources) {
        var version = Packer.readVersion(resources);
        assertThat(version).isEmpty();
    }

    @Test
    void manifestIncludesImplementationVersion() {
        var manifest = Manifestor.manifest("airhacks.App", Optional.of("2026.04.26.01"));
        var attribute = manifest.getMainAttributes().getValue("Implementation-Version");
        assertThat(attribute).isEqualTo("2026.04.26.01");
    }

    @Test
    void manifestOmitsImplementationVersionWhenAbsent() {
        var manifest = Manifestor.manifest("airhacks.App", Optional.empty());
        var attribute = manifest.getMainAttributes().getValue("Implementation-Version");
        assertThat(attribute).isNull();
    }
}
