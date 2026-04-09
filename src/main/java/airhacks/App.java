package airhacks;

import java.io.IOException;

import airhacks.zb.cleanup.control.Cleaner;
import airhacks.zb.compiler.control.Compiler;
import airhacks.zb.discovery.control.JavaFiles;
import airhacks.zb.hints.boundary.UserHint;
import airhacks.zb.hook.control.PostBuildHook;
import airhacks.zb.log.control.Log;
import airhacks.zb.packer.control.Packer;
import airhacks.zb.prereqs.control.Directories;
import airhacks.zb.stopwatch.control.StopWatch;

/**
 *
 * @author airhacks.com
 */
public interface App {

    String VERSION = "zb v2026.04.09.01";    


    static void build(AppArguments arguments) throws IOException {
        var sourceDirectory = arguments.sourcesDirectory();
        var classesDirectory = arguments.classesDirectory();
        
        var javaFiles = JavaFiles.findFrom(sourceDirectory);
        var mainClass = JavaFiles.findMainClass(javaFiles);
        
        UserHint.showHint(sourceDirectory, javaFiles, mainClass);
        Directories.createIfNotExists(classesDirectory);
        var compilationSuccess = Compiler.compile(javaFiles, classesDirectory);
        if (!compilationSuccess) {
            Log.warning("⚠️  compilation failed");
            return;
        }
        Log.user("🔍 compiled %d files".formatted(javaFiles.size()));

        var resourcesDirectory = arguments.resourcesDirectory();
        var relativeMainClass = mainClass.map(p -> sourceDirectory.relativize(p));
        var jarDirectory = arguments.jarDirectory();
        var jarFileName = arguments.jarFileName();

        Packer.createJAR(classesDirectory, resourcesDirectory, jarDirectory, jarFileName,relativeMainClass);
        if (arguments.isClassesDirTemporary()) {
            Cleaner.cleanClasses(classesDirectory);
        }
        PostBuildHook.runIfConfigured(sourceDirectory, jarDirectory, jarFileName);
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
