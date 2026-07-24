package airhacks.zb.build.boundary;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import airhacks.AppArguments;
import airhacks.zb.cleanup.control.Cleaner;
import airhacks.zb.compiler.control.Compiler;
import airhacks.zb.configuration.control.Configuration;
import airhacks.zb.discovery.control.JavaFiles;
import airhacks.zb.hints.boundary.UserHint;
import airhacks.zb.log.control.Log;
import airhacks.zb.packer.control.Packer;
import airhacks.zb.prereqs.control.Directories;

public interface Build {

    static boolean perform(AppArguments arguments) throws IOException {
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

        Packer.createJAR(Path.of("."), classesDirectory, resourcesDirectory, jarDirectory, jarFileName, relativeMainClass);
        if (arguments.isClassesDirTemporary()) {
            Cleaner.cleanClasses(classesDirectory);
        }
        return true;
    }
}
