package airhacks.zb.discovery.control;

import java.nio.file.Path;

public class SourceLocationTest {

    public static void main(String... args) {
        sourcesInTheRootDirectory();
        System.out.println("SourceLocationTest passed");
    }

    static void sourcesInTheRootDirectory() {
        var sourceDirectory = SourceLocator.detectSourceDirectory(Path.of("."));
        if (sourceDirectory.isEmpty())
            throw new AssertionError("expected source directory to be present");
        var actual = sourceDirectory.get().toString();
        if (!actual.endsWith("src/main/java"))
            throw new AssertionError("expected path ending with src/main/java but got " + actual);
    }
}
