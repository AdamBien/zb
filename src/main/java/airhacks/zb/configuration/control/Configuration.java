package airhacks.zb.configuration.control;

import airhacks.AppArguments;
import airhacks.zb.log.boundary.Log;

import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

public enum Configuration {
    SOURCES_DIR, RESOURCES_DIR, CLASSES_DIR, JAR_DIR, JAR_FILE_NAME;

    static final String DISCOVERED = "<discovered by zb>";
    static {
        PropertyFile.createIfNotExists(defaults());
    }

    public String get(String defaultValue) {
        var properties = new Properties();
        try {
            try(var reader = new FileReader(PropertyFile.PROPERTY_FILE.toFile())){
                properties.load(reader);
                var property = toProperty();
                var value = properties.getProperty(property);
                return Optional
                        .ofNullable(value)
                        .stream()
                        .filter(p -> !p.equals(DISCOVERED))
                        .findFirst()
                        .orElse(defaultValue);
        
            }
        } catch (IOException e) {
            Log.error("failed to load properties from %s".formatted(PropertyFile.PROPERTY_FILE),e);
        }
        return defaultValue;
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
