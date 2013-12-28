package me.vemacs.bungee.misc;

import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.plugin.Plugin;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.Map;

public class ConfigUtils {
    public static File loadResource(Plugin plugin, String resource) {
        File folder = plugin.getDataFolder();
        if (!folder.exists())
            folder.mkdir();
        File resourceFile = new File(folder, resource);
        try {
            if (!resourceFile.exists()) {
                resourceFile.createNewFile();
                try (InputStream in = plugin.getResourceAsStream(resource);
                     OutputStream out = new FileOutputStream(resourceFile)) {
                    ByteStreams.copy(in, out);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resourceFile;
    }

    public static Map<?, ?> loadMapFrom(File file) {
        Yaml yaml = new Yaml();
        Object tmp = null;
        try (FileInputStream fis = new FileInputStream(file)) {
            tmp = yaml.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (tmp instanceof Map)
            return (Map) tmp;
        return null;
    }
}
