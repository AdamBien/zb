package airhacks.zb.packer.control;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public interface JarLoader {
 
    static Manifest loadManifest(Path jarFile) throws IOException {
        try(var jar = new JarFile(jarFile.toFile())) {
            return jar.getManifest();
        }
    }

    static List<JarEntry> loadMetaInfServices(Path jarFile) throws IOException {
        try(var jar = new JarFile(jarFile.toFile())) {
            return jar
            .stream()
            .filter(JarLoader::isMetaServices)
            .toList();
        }
    }

    static boolean isMetaServices(JarEntry entry) {
        return entry.getName().startsWith("META-INF/services");
    }

}
