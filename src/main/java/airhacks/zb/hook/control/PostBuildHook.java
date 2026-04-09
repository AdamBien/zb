package airhacks.zb.hook.control;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import airhacks.zb.configuration.control.Configuration;
import airhacks.zb.log.control.Log;

public interface PostBuildHook {

    String NONE = "<none>";

    static Optional<String> configuredHook() {
        var hook = Configuration.POST_BUILD_HOOK.get(NONE);
        if (NONE.equals(hook) || hook.isBlank()) {
            return Optional.empty();
        }
        return Optional.of(hook);
    }

    static void execute(String command, Path sourceDir, Path jarDir, String jarFileName) {
        Log.user("🪝 executing post-build hook: %s".formatted(command));
        try {
            var processBuilder = new ProcessBuilder("sh", "-c", command)
                    .inheritIO()
                    .directory(Path.of(".").toFile());
            var env = processBuilder.environment();
            env.put("ZB_JAR_PATH", jarDir.resolve(jarFileName).toString());
            env.put("ZB_SOURCE_DIR", sourceDir.toString());
            env.put("ZB_JAR_DIR", jarDir.toString());
            env.put("ZB_JAR_FILE_NAME", jarFileName);
            var process = processBuilder.start();
            var exitCode = process.waitFor();
            if (exitCode != 0) {
                Log.warning("⚠️  post-build hook exited with code %d".formatted(exitCode));
            } else {
                Log.user("🪝 post-build hook completed successfully");
            }
        } catch (IOException e) {
            Log.warning("⚠️  failed to execute post-build hook: %s".formatted(e.getMessage()));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Log.warning("⚠️  post-build hook was interrupted");
        }
    }

    static void runIfConfigured(Path sourceDir, Path jarDir, String jarFileName) {
        configuredHook().ifPresent(hook -> execute(hook, sourceDir, jarDir, jarFileName));
    }
}
