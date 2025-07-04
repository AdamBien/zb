package airhacks.zb.configuration.control;

import airhacks.AppArguments;

import java.util.Map;
import java.util.Optional;

public enum Configuration {
    SOURCES_DIR, RESOURCES_DIR, CLASSES_DIR, JAR_DIR, JAR_FILE_NAME;

    static final String DISCOVERED = "<discovered by zb>";
    static {
        PropertyFile.createIfNotExists(defaults());
    }

    public String get(String defaultValue) {
        var property = System.getProperty(toProperty());
        return Optional
                .ofNullable(property)
                .stream()
                .filter(p -> !p.equals(DISCOVERED))
                .findFirst()
                .orElse(defaultValue);
    }

    static Map<String, String> defaults() {
        return Map.of(
                SOURCES_DIR.toProperty(), DISCOVERED,
                RESOURCES_DIR.toProperty(), DISCOVERED,
                CLASSES_DIR.toProperty(), AppArguments.Defaults.CLASSES_DIR.asString(),
                JAR_DIR.toProperty(), AppArguments.Defaults.JAR_DIR.asString(),
                JAR_FILE_NAME.toProperty(), AppArguments.Defaults.JAR_FILE_NAME);
    }

    String toProperty() {
        return name().toLowerCase().replace("_", ".");
    }

}
