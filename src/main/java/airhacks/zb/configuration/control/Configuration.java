package airhacks.zb.configuration.control;

import java.util.Optional;

public enum Configuration {
    SOURCES_DIR, RESOURCES_DIR, CLASSES_DIR, JAR_DIR, JAR_FILE_NAME;

    public Optional<String> get() {
        var property = System.getProperty(toProperty());
        return Optional.ofNullable(property);
    }


    String toProperty() {
        return name().toLowerCase().replace("_", ".");
    }

}
