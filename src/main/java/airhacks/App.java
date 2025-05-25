package airhacks;
import java.io.IOException;
import java.nio.file.Path;

import airhacks.zb.compiler.control.Compiler;
import airhacks.zb.log.boundary.Log;
import airhacks.zb.packer.control.Packer;


/**
 *
 * @author airhacks.com
 */
interface App {

    String VERSION = "zb v2025.05.25.1";

    record Arguments(Path sourceDirectory, Path classesDirectory, Path jarDirectory, String jarFileName) {
        static Arguments from(String... args) {
            return new Arguments(
                Path.of(args.length > 0 ? args[0] : "src/main/java"),
                Path.of(args.length > 1 ? args[1] : "target/test/classes"),
                Path.of(args.length > 2 ? args[2] : "target/test/jar"),
                args.length > 3 ? args[3] : "test.jar"
            );
        }

        public void userInfo() { Log.user("Source: " + sourceDirectory + ", Classes: " + classesDirectory + ", JAR Dir: " + jarDirectory + ", JAR: " + jarFileName); }
            
    }
    
    static void main(String... args) throws IOException {
        Log.user("🚀 " + VERSION + " - fast and pure Java 🛠️");
        var arguments = Arguments.from(args);
        arguments.userInfo();
        Compiler.compile(arguments.sourceDirectory(), arguments.classesDirectory());
        Packer.archive(arguments.classesDirectory(), arguments.jarDirectory(), arguments.jarFileName());
    }
}
