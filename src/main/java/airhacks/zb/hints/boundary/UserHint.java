package airhacks.zb.hints.boundary;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import airhacks.zb.log.boundary.Log;

public interface UserHint {

    static void showHint(Path sourceDirectory, List<Path> javaFiles, Optional<Path> mainClass) {
        
        if (!Files.exists(sourceDirectory)) {
            sourceDirectoryNotFound(sourceDirectory);
        }
        if (javaFiles.isEmpty()) {
            noJavaFilesFound(sourceDirectory);
        }
        if (mainClass.isEmpty()) {
            noMainClassFound();
        }
    }

    static void sourceDirectoryNotFound(Path sourceDirectory) {
        Log.error("❌ Source directory not found: " + sourceDirectory.toAbsolutePath(), null);
        Log.user("💡 Please ensure the source directory exists or specify a different path.");
        Log.user("   Usage: java -jar zb.jar [source-dir] [classes-dir] [jar-dir] [jar-name]");
        Log.user("   Example: java -jar zb.jar src/main/java target/classes target myapp.jar");
    }

    static void noJavaFilesFound(Path sourceDirectory) {
        Log.warning("⚠️  No Java files found in: " + sourceDirectory.toAbsolutePath());
        Log.user("💡 Please check if the directory contains .java files.");
    }

    static void directoryAccessError(Path directory, Exception e) {
        Log.error("❌ Failed to read directory: " + directory.toAbsolutePath(), e);
        System.exit(0);
    }

    static void noMainClassFound() {
        Log.warning("⚠️  No main class found in the Java files.");
        Log.user("💡 Ensure at least one class has a 'public static void main(String[] args)' method.");
    }

    static void multipleMainClassesFound() {
        Log.warning("⚠️  Multiple main classes found.");
        Log.user("💡 The build tool found multiple classes with main methods.");
        Log.user("   Consider having only one main class in your project.");
    }

    static void error(String message, Exception e) {
        Log.error("❌ " + message, e);
    }
} 