package airhacks;

import java.nio.file.Path;
import java.util.Optional;

import airhacks.zb.configuration.control.Configuration;
import airhacks.zb.discovery.control.ResourceLocator;
import airhacks.zb.discovery.control.SourceLocator;
import airhacks.zb.log.boundary.Log;

public record AppArguments(Path sourcesDirectory, Optional<Path> resourcesDirectory, Path classesDirectory,
        Path jarDirectory, String jarFileName, boolean isClassesDirTemporary) {
    
    public static final String TEMP_DIR_MARKER = "[temp.dir]";
    public enum Defaults {
        CLASSES_DIR(TEMP_DIR_MARKER),
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
        var classesDir = args.length > 1 ? args[1] : Configuration.CLASSES_DIR.get(Defaults.CLASSES_DIR.asString());
        var isTemporary = TEMP_DIR_MARKER.equals(classesDir);
        var classesPath = isTemporary ? createTempDirectory() : Path.of(classesDir);
        
        return new AppArguments(
                args.length > 0 ? Path.of(args[0]) : SourceLocator.detectSourceDirectory().orElseThrow(),
                ResourceLocator.detectResourcesDirectory(),
                classesPath,
                Path.of(args.length > 2 ? args[2] : Configuration.JAR_DIR.get(Defaults.JAR_DIR.asString())),
                args.length > 3 ? args[3] : Configuration.JAR_FILE_NAME.get(Defaults.JAR_FILE_NAME),
                isTemporary);
    }
    
    static Path createTempDirectory() {
        try {
            var tempDir = java.nio.file.Files.createTempDirectory("zb-classes-");
            Log.user("📁 creating temporary directory: %s".formatted(tempDir));
            return tempDir;
        } catch (java.io.IOException e) {
            throw new RuntimeException("Failed to create temporary directory", e);
        }
    }

    public void userInfo() {
        if (resourcesDirectory.isPresent()) {
            Log.user("🔍 sources: %s, resources: %s, classes: %s, JAR dir: %s, JAR file: %s".formatted(sourcesDirectory,
                    resourcesDirectory.get(), classesDirectory, jarDirectory, jarFileName));
        } else {
            Log.user(
                    "🔍 sources: %s, classes: %s, JAR dir: %s, JAR file: %s".formatted(sourcesDirectory, classesDirectory, jarDirectory, jarFileName));
        }
    }

}