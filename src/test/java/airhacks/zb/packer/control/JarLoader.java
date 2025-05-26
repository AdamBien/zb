package airhacks.zb.packer.control;

import java.io.IOException;
import java.nio.file.Path;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public interface JarLoader {
 
    static Manifest loadManifest(Path jarFile) throws IOException {
        try(var jar = new JarFile(jarFile.toFile())) {
            return jar.getManifest();
        }
    }

}
