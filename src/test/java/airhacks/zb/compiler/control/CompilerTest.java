package airhacks.zb.compiler.control;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;

import airhacks.zb.discovery.control.JavaFiles;

public class CompilerTest {

    public static void main(String... args) throws IOException {
        compile();
        compileWithClasspath();
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

    static void compileWithClasspath() throws IOException {
        var libSources = Files.createTempDirectory("zb-cp-lib-src");
        var libClasses = Files.createTempDirectory("zb-cp-lib-classes");
        var appSources = Files.createTempDirectory("zb-cp-app-src");
        var appClasses = Files.createTempDirectory("zb-cp-app-classes");
        try {
            var greeter = libSources.resolve("Greeter.java");
            Files.writeString(greeter, "public class Greeter { public static String greet() { return \"hello\"; } }");
            if (!Compiler.compile(List.of(greeter), libClasses))
                throw new AssertionError("expected library compilation to succeed");

            var app = appSources.resolve("UsesGreeter.java");
            Files.writeString(app, "public class UsesGreeter { String greeting() { return Greeter.greet(); } }");
            if (Compiler.compile(List.of(app), appClasses))
                throw new AssertionError("expected compilation without classpath to fail");
            if (!Compiler.compile(List.of(app), appClasses, List.of(libClasses)))
                throw new AssertionError("expected compilation with classpath to succeed");
        } finally {
            deleteRecursively(libSources);
            deleteRecursively(libClasses);
            deleteRecursively(appSources);
            deleteRecursively(appClasses);
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
