package airhacks;

import java.io.IOException;
import java.nio.file.Path;

import airhacks.zb.cleanup.control.Cleaner;
import airhacks.zb.compiler.control.Compiler;
import airhacks.zb.discovery.control.JavaFiles;
import airhacks.zb.hints.boundary.UserHint;
import airhacks.zb.log.boundary.Log;
import airhacks.zb.packer.control.Packer;
import airhacks.zb.prereqs.control.Directories;
import airhacks.zb.stopwatch.control.StopWatch;

/**
 *
 * @author airhacks.com
 */
public interface App {

    String VERSION = "zb v2025.06.19.1";    

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


    static void build(AppArguments arguments) throws IOException {
        var sourceDirectory = arguments.sourcesDirectory();
        var classesDirectory = arguments.classesDirectory();
        
        var javaFiles = JavaFiles.findFrom(sourceDirectory);
        var mainClass = JavaFiles.findMainClass(javaFiles);
        
        UserHint.showHint(sourceDirectory, javaFiles, mainClass);
        Directories.createIfNotExists(classesDirectory);
        Compiler.compile(javaFiles, classesDirectory);
        Log.user("🔍 compiled %d files".formatted(javaFiles.size()));
        
        var resourcesDirectory = arguments.resourcesDirectory();
        var relativeMainClass = mainClass.map(p -> sourceDirectory.relativize(p));
        var jarDirectory = arguments.jarDirectory();
        var jarFileName = arguments.jarFileName();

        Packer.createJAR(classesDirectory, resourcesDirectory, jarDirectory, jarFileName,relativeMainClass);
        Cleaner.cleanClasses(classesDirectory);
    }

    static void main(String... args) throws IOException {
        Log.user("🚀 " + VERSION + " - fast and pure Java 🛠️");
        var arguments = AppArguments.from(args);
        arguments.userInfo();
        var stopWatch = StopWatch.start();
        build(arguments);
        stopWatch.stop();
    }
}
