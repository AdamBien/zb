package airhacks;

import static airhacks.zb.configuration.control.Configuration.CLASSES_DIR;
import static airhacks.zb.configuration.control.Configuration.JAR_DIR;
import static airhacks.zb.configuration.control.Configuration.JAR_FILE_NAME;

import java.nio.file.Path;
import java.util.Optional;

import airhacks.zb.configuration.control.Configuration;
import airhacks.zb.discovery.control.ResourceLocator;
import airhacks.zb.discovery.control.SourceLocator;
import airhacks.zb.log.boundary.Log;

public record AppArguments(Path sourcesDirectory, Optional<Path> resourcesDirectory, Path classesDirectory,
        Path jarDirectory, String jarFileName) {
    public enum Defaults {
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

    static AppArguments from(String... args) {
        return new AppArguments(
                args.length > 0 ? Path.of(args[0]) : SourceLocator.detectSourceDirectory().orElseThrow(),
                ResourceLocator.detectResourcesDirectory(),
                Path.of(args.length > 1 ? args[1] : Defaults.CLASSES_DIR.asString()),
                Path.of(args.length > 2 ? args[2] : Configuration.JAR_DIR.get(Defaults.JAR_DIR.asString())),
                args.length > 3 ? args[3] : Configuration.JAR_FILE_NAME.get(Defaults.JAR_FILE_NAME));
    }

    public void userInfo() {
        if (resourcesDirectory.isPresent()) {
            Log.user("🔍 sources: %s, resources: %s, JAR dir: %s, JAR file: %s".formatted(sourcesDirectory,
                    resourcesDirectory.get(), jarDirectory, jarFileName));
        } else {
            Log.user(
                    "🔍 sources: %s, JAR dir: %s, JAR file: %s".formatted(sourcesDirectory, jarDirectory, jarFileName));
        }
    }

}