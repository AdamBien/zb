package airhacks.zb.packer.control;

import java.util.Optional;
import java.util.jar.Manifest;

public interface Manifestor {


    static Manifest manifest(String mainClass, Optional<String> version) {
        var manifest = new Manifest();
        var attributes = manifest.getMainAttributes();
        attributes.putValue("Manifest-Version", "1.0");
        attributes.putValue("Main-Class", mainClass);
        version.ifPresent(v -> attributes.putValue("Implementation-Version", v));
        return manifest;
    }

}
