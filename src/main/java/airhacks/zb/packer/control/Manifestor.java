package airhacks.zb.packer.control;

import java.util.jar.Manifest;

public interface Manifestor {


    static Manifest manifest(String mainClass) {
        var manifest = new Manifest();
        var attributes = manifest.getMainAttributes();
        attributes.putValue("Manifest-Version", "1.0");
        attributes.putValue("Main-Class", mainClass);
        return manifest;
    }
    
}
