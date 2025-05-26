package airhacks.zb.compiler.control;

import java.io.IOException;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

import airhacks.zb.discovery.control.JavaFiles;

public class CompilerTest {
    
    @Test
    void compile() throws IOException {
        var input = Path.of("src/main/java");
        var output = Path.of("target/test/classes");
        var javaFiles = JavaFiles.findFrom(input);
        var result = Compiler.compile(javaFiles,output);
        assertThat(result).isTrue();
    }
}
