package airhacks.zb.hook.control;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import airhacks.zb.configuration.control.Configuration;
import airhacks.zb.log.control.Log;

public interface PostBuildHook {

    String NONE = "<none>";

    /// Suppresses the hook to break recursion. A hook like `zunit` runs the test
    /// suite, whose integration tests call `App.build`, which would fire the hook
    /// again — unbounded recursion. Set as an env var on every hook-spawned child
    /// (inherited by all descendants), or as a system property by in-process
    /// callers (tests) that must build without re-triggering the hook.
    String SKIP_MARKER = "ZB_IN_POST_BUILD_HOOK";

    static boolean hookSuppressed() {
        return Boolean.parseBoolean(System.getenv(SKIP_MARKER)) || Boolean.getBoolean(SKIP_MARKER);
    }

    static Optional<String> configuredHook() {
        var hook = Configuration.POST_BUILD_HOOK.get(NONE);
        if (NONE.equals(hook) || hook.isBlank()) {
            return Optional.empty();
        }
        return Optional.of(hook);
    }

    static boolean isWindows() {
        return System.getProperty("os.name", "").toLowerCase(Locale.ROOT).contains("win");
    }

    static List<String> shellCommand(String command) {
        return isWindows()
                ? List.of("cmd.exe", "/c", command)
                : List.of("sh", "-c", command);
    }

    static void execute(String command, Path sourceDir, Path jarDir, String jarFileName) {
        Log.user("🪝 executing post-build hook: %s".formatted(command));
        try {
            var osDependentCommand = shellCommand(command);
            var processBuilder = new ProcessBuilder(osDependentCommand)
                    .inheritIO()
                    .directory(Path.of(".").toFile());
            var env = processBuilder.environment();
            env.put(SKIP_MARKER, "true");
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
        if (hookSuppressed()) {
            return;
        }
        configuredHook().ifPresent(hook -> execute(hook, sourceDir, jarDir, jarFileName));
    }
}
