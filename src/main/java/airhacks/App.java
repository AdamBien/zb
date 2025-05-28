package airhacks;
import java.io.IOException;
import java.nio.file.Path;

import airhacks.zb.compiler.control.Compiler;
import airhacks.zb.discovery.control.JavaFiles;
import airhacks.zb.log.boundary.Log;
import airhacks.zb.packer.control.Packer;
import static airhacks.App.Defaults.*;


/**
 *
 * @author airhacks.com
 */
interface App {

    String VERSION = "zb v2025.05.28.1";

    enum Defaults {
        SOURCE_DIR("src/main/java"),
        CLASSES_DIR("target/test/classes"),
        JAR_DIR("target/test/jar");

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
                Path.of(args.length > 0 ? args[0] : SOURCE_DIR.asString()),
                Path.of(args.length > 1 ? args[1] : CLASSES_DIR.asString()),
                Path.of(args.length > 2 ? args[2] : JAR_DIR.asString()),
                args.length > 3 ? args[3] : "zb.jar"
            );
        }

        public void userInfo() { Log.user("🔍 source: " + sourceDirectory + ", classes: " + classesDirectory + ", JAR dir: " + jarDirectory + ", JAR file: " + jarFileName); }
            
    }

    static void build(Arguments arguments) throws IOException {
        var sourceDirectory = arguments.sourceDirectory();
        var javaFiles = JavaFiles.findFrom(sourceDirectory);
        Compiler.compile(javaFiles, arguments.classesDirectory());
        var mainClass = JavaFiles.findMainClass(javaFiles);

        Log.debug("main class: " + mainClass);
        var relativeMainClass = mainClass.map(p-> sourceDirectory.relativize(p));

        Packer.archive(arguments.classesDirectory(), arguments.jarDirectory(), arguments.jarFileName(), relativeMainClass);
    }
    
    static void main(String... args) throws IOException {
        Log.user("🚀 " + VERSION + " - fast and pure Java 🛠️");
        var arguments = Arguments.from(args);
        arguments.userInfo();
        build(arguments);
    }
}
