package airhacks.zb.compiler.control;

import java.io.IOException;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

public class CompilerTest {
    
    @Test
    void compile() throws IOException {
        var input = Path.of("src/main/java");
        var output = Path.of("target/test/classes");
        var result = Compiler.compile(input,output);
        assertThat(result).isTrue();
    }
}
