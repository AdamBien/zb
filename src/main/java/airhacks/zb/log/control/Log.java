package airhacks.zb.log.control;

import java.io.PrintStream;

public enum Log {

    ERROR(Color.BRIGHT_RED, System.err),
    USER(Color.BRIGHT_CYAN, System.out),
    INFO(Color.BRIGHT_GREEN, System.out),
    SYSTEM(Color.SKY_BLUE, System.out),
    WARNING(Color.WARM_YELLOW, System.out),
    DEBUG(Color.SOFT_PURPLE, System.out);

    PrintStream out;

    enum Color {
        SOFT_GRAY("\033[38;5;246m"),      // Soft gray
        WARM_YELLOW("\033[38;5;220m"),        // Warm yellow
        BRIGHT_BLUE("\033[38;5;33m"),     // Bright blue
        BRIGHT_WHITE("\033[38;5;255m"),      // Bright white
        BRIGHT_RED("\033[38;5;196m"),         // Bright red
        BRIGHT_GREEN("\033[38;5;46m"),        // Bright green
        BRIGHT_YELLOW("\033[38;5;226m"),      // Bright yellow
        SKY_BLUE("\033[38;5;39m"),         // Sky blue
        SOFT_PURPLE("\033[38;5;129m"),      // Soft purple
        BRIGHT_CYAN("\033[38;5;51m"),         // Bright cyan
        BLACK_ON_WHITE("\033[38;5;232;48;5;255m");  // Black text on white background

        String code;

        Color(String code) {
            this.code = code;
        }
    }

    private final String value;
    private final static String RESET = "\u001B[0m";

    private Log(Color color, PrintStream out) {
        this.value = (color.code + "%s" + RESET);
        this.out = out;
    }

    public String formatted(String raw) {
        return this.value.formatted(raw);
    }

    public void out(String message) {
        var colored = formatted(message);
        this.out.println(colored);
    }

    public static void debug(String message) {
        Log.DEBUG.out(message);
    }

    public static void error(String message, Exception e) {
        Log.ERROR.out(message);
    }

    public static void user(String message){
        Log.INFO.out(message);
    }

    public static void warning(String message){
        Log.WARNING.out(message);
    }
}
