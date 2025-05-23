package airhacks.zb.compiler.control;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

public class CompilerTest {
    @Test
    void compile() throws IOException {
        var result = Compiler.compile(Path.of("."));
        assertThat(result).isTrue();
    }
}
