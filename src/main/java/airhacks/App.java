package airhacks;
import java.io.IOException;
import java.nio.file.Path;

import airhacks.zb.compiler.control.Compiler;
import airhacks.zb.packer.control.Packer;


/**
 *
 * @author airhacks.com
 */
interface App {

    record Arguments(Path sourceDirectory, Path classesDirectory, Path jarDirectory, String jarFileName) {
        static Arguments from(String... args) {
            return new Arguments(
                Path.of(args.length > 0 ? args[0] : "src/main/java"),
                Path.of(args.length > 1 ? args[1] : "target/test/classes"),
                Path.of(args.length > 2 ? args[2] : "target/test/jar"),
                args.length > 3 ? args[3] : "test.jar"
            );
        }
    }
    
    static void main(String... args) throws IOException {
        var arguments = Arguments.from(args);
        Compiler.compile(arguments.sourceDirectory(), arguments.classesDirectory());
        Packer.archive(arguments.classesDirectory(), arguments.jarDirectory(), arguments.jarFileName());
    }
}
