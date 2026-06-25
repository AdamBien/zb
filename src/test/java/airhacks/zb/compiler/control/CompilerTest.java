package airhacks.zb.compiler.control;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

import airhacks.zb.discovery.control.JavaFiles;

public class CompilerTest {

    public static void main(String... args) throws IOException {
        compile();
        System.out.println("CompilerTest passed");
    }

    static void compile() throws IOException {
        var input = Path.of("src/main/java");
        var output = Files.createTempDirectory("zb-compile-test");
        try {
            var javaFiles = JavaFiles.findFrom(input);
            var result = Compiler.compile(javaFiles, output);
            if (!result)
                throw new AssertionError("expected compilation to succeed");
        } finally {
            deleteRecursively(output);
        }
    }

    static void deleteRecursively(Path directory) throws IOException {
        try (var paths = Files.walk(directory)) {
            paths.sorted(Comparator.reverseOrder())
                 .forEach(path -> {
                     try {
                         Files.deleteIfExists(path);
                     } catch (IOException e) {
                         // ignore cleanup errors
                     }
                 });
        }
    }
}
