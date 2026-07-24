package airhacks;

import java.io.IOException;

import airhacks.zb.build.boundary.Build;
import airhacks.zb.hook.control.PostBuildHook;
import airhacks.zb.log.control.Log;
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

    static void main(String... args) throws IOException {
        Log.user("🚀 " + VERSION + " - fast and pure Java 🛠️");
        var arguments = AppArguments.from(args);
        arguments.userInfo();
        var stopWatch = StopWatch.start();
        var success = Build.perform(arguments);
        stopWatch.stop();
        if (success) {
            PostBuildHook.runIfConfigured(arguments.sourcesDirectory(), arguments.jarDirectory(), arguments.jarFileName());
        }
    }
}
