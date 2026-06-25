package airhacks.zb.discovery.control;

import java.nio.file.Path;

public class JavaFilesTest {

    public static void main(String... args) {
        findMain();
        pathMatchesClassName();
        System.out.println("JavaFilesTest passed");
    }

    static void findMain() {
        var src = SourceLocator.detectSourceDirectory().orElseThrow();
        var javaFiles = JavaFiles.findFrom(src);
        var mainClasses = JavaFiles.findMain(javaFiles);
        System.out.println(mainClasses);
        if (mainClasses.size() != 1)
            throw new AssertionError("expected 1 main class but got " + mainClasses.size());
        if (!mainClasses.getFirst().endsWith(Path.of("airhacks", "App.java")))
            throw new AssertionError("expected airhacks/App.java but got " + mainClasses.getFirst());
    }

    static void pathMatchesClassName() {
        var path = Path.of("src", "main", "java", "com", "example", "App.java");
        if (!JavaFiles.pathMatchesClassName(path, "com.example.App"))
            throw new AssertionError("expected match for com.example.App");
        if (!JavaFiles.pathMatchesClassName(path, "App"))
            throw new AssertionError("expected match for App");
        if (JavaFiles.pathMatchesClassName(path, "com.other.App"))
            throw new AssertionError("expected no match for com.other.App");
    }
}
