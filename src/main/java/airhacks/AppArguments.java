package airhacks;

import java.nio.file.Path;
import java.util.Optional;

import airhacks.zb.discovery.control.ResourceLocator;
import airhacks.zb.discovery.control.SourceLocator;
import airhacks.zb.log.boundary.Log;

import static airhacks.App.Defaults.CLASSES_DIR;
import static airhacks.App.Defaults.JAR_DIR;
import static airhacks.App.Defaults.JAR_FILE_NAME;


record AppArguments(Path sourcesDirectory, Optional<Path> resourcesDirectory, Path classesDirectory, Path jarDirectory, String jarFileName) {
    static AppArguments from(String... args) {
        return new AppArguments(
                args.length > 0 ? Path.of(args[0]) : SourceLocator.detectSourceDirectory().orElseThrow(),
                ResourceLocator.detectResourcesDirectory(),
                Path.of(args.length > 1 ? args[1] : CLASSES_DIR.asString()),
                Path.of(args.length > 2 ? args[2] : JAR_DIR.asString()),
                args.length > 3 ? args[3] : JAR_FILE_NAME);
    }

    public void userInfo() {
        if(resourcesDirectory.isPresent()) {
            Log.user("🔍 sources: %s, resources: %s, JAR dir: %s, JAR file: %s".formatted(sourcesDirectory,resourcesDirectory.get(),jarDirectory,jarFileName));
        } else {
            Log.user("🔍 sources: %s, JAR dir: %s, JAR file: %s".formatted(sourcesDirectory,jarDirectory,jarFileName));
        }
    }

}