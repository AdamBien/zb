package airhacks;

import static airhacks.App.Defaults.CLASSES_DIR;
import static airhacks.App.Defaults.JAR_DIR;
import static airhacks.App.Defaults.JAR_FILE_NAME;

import java.io.IOException;
import java.nio.file.Path;

import airhacks.zb.cleanup.control.Cleaner;
import airhacks.zb.compiler.control.Compiler;
import airhacks.zb.discovery.control.JavaFiles;
import airhacks.zb.discovery.control.SourceLocator;
import airhacks.zb.hints.boundary.UserHint;
import airhacks.zb.log.boundary.Log;
import airhacks.zb.packer.control.Packer;
import airhacks.zb.stopwatch.control.StopWatch;

/**
 *
 * @author airhacks.com
 */
public interface App {

    String VERSION = "zb v2025.06.04.1";

    enum Defaults {
        CLASSES_DIR("zbo/classes"),
        JAR_DIR("zbo/");

        public static final String JAR_FILE_NAME = "app.jar";

        private final String path;

        Defaults(String path) {
            this.path = path;
        }

        public String asString() {
            return path;
        }

        public Path asPath() {
            return Path.of(this.path);
        }

    }

    record Arguments(Path sourceDirectory, Path classesDirectory, Path jarDirectory, String jarFileName) {
        static Arguments from(String... args) {
            return new Arguments(
                    args.length > 0 ? Path.of(args[0]) : SourceLocator.detectSourceDirectory().orElseThrow(),
                    Path.of(args.length > 1 ? args[1] : CLASSES_DIR.asString()),
                    Path.of(args.length > 2 ? args[2] : JAR_DIR.asString()),
                    args.length > 3 ? args[3] : JAR_FILE_NAME);
        }

        public void userInfo() {
            Log.user("🔍 source: %s , JAR dir: %s, JAR file: %s".formatted(sourceDirectory,jarDirectory,jarFileName));
        }

    }

    static void build(Arguments arguments) throws IOException {
        var sourceDirectory = arguments.sourceDirectory();
        var javaFiles = JavaFiles.findFrom(sourceDirectory);
        var mainClass = JavaFiles.findMainClass(javaFiles);
        var classesDirectory = arguments.classesDirectory();
        UserHint.showHint(sourceDirectory, javaFiles, mainClass);

        Compiler.compile(javaFiles, classesDirectory);

        var relativeMainClass = mainClass.map(p -> sourceDirectory.relativize(p));

        Packer.archive(classesDirectory, arguments.jarDirectory(), arguments.jarFileName(),
                relativeMainClass);
        Cleaner.cleanClasses(classesDirectory);
    }

    static void main(String... args) throws IOException {
        Log.user("🚀 " + VERSION + " - fast and pure Java 🛠️");
        var arguments = Arguments.from(args);
        arguments.userInfo();
        var stopWatch = StopWatch.start();
        build(arguments);
        stopWatch.stop();
    }
}
