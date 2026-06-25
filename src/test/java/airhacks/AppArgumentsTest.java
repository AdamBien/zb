package airhacks;

import java.nio.file.Files;
import java.nio.file.Path;

import airhacks.zb.discovery.control.SourceLocator;

public class AppArgumentsTest {

    public static void main(String... args) throws Exception {
        argumentsWithDefaultValues();
        argumentsWithCustomValues();
        argumentsWithPartialCustomValues();
        defaultArgumentsCreateTempDirectory();
        explicitlyConfiguredClassesDirectoryIsUsedAsIs();
        tempDirMarkerInCommandLineCreatesTempDirectory();
        System.out.println("AppArgumentsTest passed");
    }

    static void argumentsWithDefaultValues() throws Exception {
        var arguments = AppArguments.from();

        eq(SourceLocator.detectSourceDirectory().orElseThrow(), arguments.sourcesDirectory());
        // When using default <temp.dir>, a temporary directory is created
        truthy(!arguments.classesDirectory().toString().equals(AppArguments.TEMP_DIR_MARKER),
                "classesDirectory should not be the temp dir marker");
        eq(AppArguments.Defaults.JAR_DIR.asPath(), arguments.jarDirectory());
        eq(AppArguments.Defaults.JAR_FILE_NAME, arguments.jarFileName());

        Files.deleteIfExists(arguments.classesDirectory());
    }

    static void argumentsWithCustomValues() {
        var arguments = AppArguments.from(
                "custom/src",
                "custom/classes",
                "custom/jar",
                "custom.jar");

        eq(Path.of("custom/src"), arguments.sourcesDirectory());
        eq(Path.of("custom/classes"), arguments.classesDirectory());
        eq(Path.of("custom/jar"), arguments.jarDirectory());
        eq("custom.jar", arguments.jarFileName());
    }

    static void argumentsWithPartialCustomValues() throws Exception {
        var arguments = AppArguments.from("custom/src");

        eq(Path.of("custom/src"), arguments.sourcesDirectory());
        eq(AppArguments.Defaults.JAR_DIR.asPath(), arguments.jarDirectory());
        eq(AppArguments.Defaults.JAR_FILE_NAME, arguments.jarFileName());

        Files.deleteIfExists(arguments.classesDirectory());
    }

    static void defaultArgumentsCreateTempDirectory() throws Exception {
        var arguments = AppArguments.from();

        truthy(arguments.classesDirectory() != null, "classesDirectory should not be null");
        truthy(Files.exists(arguments.classesDirectory()), "classesDirectory should exist");
        truthy(Files.isDirectory(arguments.classesDirectory()), "classesDirectory should be a directory");
        truthy(arguments.classesDirectory().toString().contains("zb-classes-"),
                "classesDirectory name should contain zb-classes-");

        Files.deleteIfExists(arguments.classesDirectory());
    }

    static void explicitlyConfiguredClassesDirectoryIsUsedAsIs() {
        var explicitClassesDir = "classes-dir";
        var arguments = AppArguments.from("src/main/java", explicitClassesDir);

        eq(Path.of(explicitClassesDir), arguments.classesDirectory());
        eq(explicitClassesDir, arguments.classesDirectory().toString());
    }

    static void tempDirMarkerInCommandLineCreatesTempDirectory() throws Exception {
        var arguments = AppArguments.from("src/main/java", AppArguments.TEMP_DIR_MARKER);

        truthy(arguments.classesDirectory() != null, "classesDirectory should not be null");
        truthy(Files.exists(arguments.classesDirectory()), "classesDirectory should exist");
        truthy(Files.isDirectory(arguments.classesDirectory()), "classesDirectory should be a directory");
        truthy(arguments.classesDirectory().toString().contains("zb-classes-"),
                "classesDirectory name should contain zb-classes-");

        Files.deleteIfExists(arguments.classesDirectory());
    }

    static void eq(Object expected, Object actual) {
        if (!java.util.Objects.equals(expected, actual))
            throw new AssertionError("expected [%s] but got [%s]".formatted(expected, actual));
    }

    static void truthy(boolean condition, String message) {
        if (!condition)
            throw new AssertionError(message);
    }
}
