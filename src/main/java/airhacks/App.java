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

    static void main(String... args) throws IOException {
        var sourceDirectory = Path.of("src/main/java");
        var classesDirectory =  Path.of("target/test/classes");
        var jarDirectory = Path.of("target/test/jar");
        var jarFileName = "test.jar";
        Compiler.compile(sourceDirectory,classesDirectory);
        Packer.archive(classesDirectory, jarDirectory, jarFileName);
    }
}
