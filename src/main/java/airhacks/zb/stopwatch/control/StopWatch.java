package airhacks.zb.stopwatch.control;

import java.time.Duration;
import java.time.Instant;

import airhacks.zb.log.boundary.Log;

public record StopWatch(Instant begin) {

    public static StopWatch start() {
        return new StopWatch(Instant.now());
    }

    public void stop() {
        var end = Instant.now();
        var duration = Duration.between(this.begin, end);
        var message = "🕒 %ds %dms".formatted(duration.toSeconds(), duration.toMillisPart());
        Log.user(message);
    }
}
