package airhacks;

import java.io.IOException;
import java.nio.file.Path;

import java.util.Optional;

import airhacks.zb.cleanup.control.Cleaner;
import airhacks.zb.compiler.control.Compiler;
import airhacks.zb.configuration.control.Configuration;
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

    String VERSION = "zb v" + readVersion();

    static String readVersion() {
        try (var in = App.class.getResourceAsStream("/version.txt")) {
            if (in == null) {
                return "unknown";
            }
            return new String(in.readAllBytes()).trim();
        } catch (IOException e) {
            return "unknown";
        }
    }


    static boolean build(AppArguments arguments) throws IOException {
        var sourceDirectory = arguments.sourcesDirectory();
        var classesDirectory = arguments.classesDirectory();

        var javaFiles = JavaFiles.findFrom(sourceDirectory);
        var configuredMainClass = Optional.ofNullable(Configuration.MAIN_CLASS.get(null));
        var mainClass = JavaFiles.findMainClass(javaFiles, configuredMainClass);

        UserHint.showHint(sourceDirectory, javaFiles, mainClass);
        Directories.createIfNotExists(classesDirectory);
        var compilationSuccess = Compiler.compile(javaFiles, classesDirectory);
        if (!compilationSuccess) {
            Log.warning("⚠️  compilation failed");
            return false;
        }
        Log.user("🔍 compiled %d files".formatted(javaFiles.size()));

        var resourcesDirectory = arguments.resourcesDirectory();
        var relativeMainClass = mainClass.map(p -> sourceDirectory.relativize(p));
        var jarDirectory = arguments.jarDirectory();
        var jarFileName = arguments.jarFileName();

        Packer.createJAR(Path.of("."), classesDirectory, resourcesDirectory, jarDirectory, jarFileName,relativeMainClass);
        if (arguments.isClassesDirTemporary()) {
            Cleaner.cleanClasses(classesDirectory);
        }
        return true;
    }

    static void main(String... args) throws IOException {
        Log.user("🚀 " + VERSION + " - fast and pure Java 🛠️");
        var arguments = AppArguments.from(args);
        arguments.userInfo();
        var stopWatch = StopWatch.start();
        var success = build(arguments);
        stopWatch.stop();
        if (success) {
            PostBuildHook.runIfConfigured(arguments.sourcesDirectory(), arguments.jarDirectory(), arguments.jarFileName());
        }
    }
}
