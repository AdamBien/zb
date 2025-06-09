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

    record JarEntryWithContent(String name, String content) {
        static JarEntryWithContent of(JarFile jar,JarEntry entry){
            try(var is = jar.getInputStream(entry)) {
                return new JarEntryWithContent(entry.getName(), new String(is.readAllBytes()));
            } catch (IOException e) {
                throw new RuntimeException("Failed to read entry: " + entry.getName(), e);
            }
        }
    }

    static List<JarEntryWithContent> loadMetaInfServices(Path jarFile) throws IOException {
        try(var jar = new JarFile(jarFile.toFile())) {
            return jar
            .stream()
            .filter(JarLoader::isMetaServices)
            .map(entry -> JarEntryWithContent.of(jar, entry))
            .toList();
        }
    }

    static boolean isMetaServices(JarEntry entry) {
        return entry.getName().startsWith("META-INF/services");
    }


}
