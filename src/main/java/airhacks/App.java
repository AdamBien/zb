package airhacks;
import java.io.IOException;
import java.nio.file.Path;

import airhacks.zb.compiler.control.Compiler;
import airhacks.zb.discovery.control.JavaFiles;
import airhacks.zb.log.boundary.Log;
import airhacks.zb.packer.control.Packer;


/**
 *
 * @author airhacks.com
 */
interface App {

    String VERSION = "zb v2025.05.25.2";

    record Arguments(Path sourceDirectory, Path classesDirectory, Path jarDirectory, String jarFileName) {
        static Arguments from(String... args) {
            return new Arguments(
                Path.of(args.length > 0 ? args[0] : "src/main/java"),
                Path.of(args.length > 1 ? args[1] : "target/test/classes"),
                Path.of(args.length > 2 ? args[2] : "target/test/jar"),
                args.length > 3 ? args[3] : "test.jar"
            );
        }

        public void userInfo() { Log.user("🔍 source: " + sourceDirectory + ", classes: " + classesDirectory + ", JAR dir: " + jarDirectory + ", JAR file: " + jarFileName); }
            
    }

    static void build(Arguments arguments) throws IOException {
        var javaFiles = JavaFiles.findFrom(arguments.sourceDirectory());
        Compiler.compile(javaFiles, arguments.classesDirectory());
        var mainClass = JavaFiles.findMainClass(javaFiles);
        Log.debug("main class: " + mainClass);
        Packer.archive(arguments.classesDirectory(), arguments.jarDirectory(), arguments.jarFileName(), mainClass);
    }
    
    static void main(String... args) throws IOException {
        Log.user("🚀 " + VERSION + " - fast and pure Java 🛠️");
        var arguments = Arguments.from(args);
        arguments.userInfo();
        build(arguments);
    }
}
