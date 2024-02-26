package utils;

import java.io.*;
import java.nio.file.Paths;
import java.util.Properties;

public abstract class Configuration {
    private final Properties properties;

    /**
     * Attempts to read a default and a custom configuration file
     * and with this initializes Configuration class.
     * @param path filepath to configuration files
     * @throws IOException if either the default configuration file doesn't exist
     *                     or an IOException occurs while loading properties
     */
    protected Configuration(String path) throws IOException {
        Properties defaultProperties = new Properties();
        FileInputStream defaultReader = new FileInputStream(
                Paths.get(path, "default.properties").toString());
        defaultProperties.load(defaultReader);
        properties = new Properties(defaultProperties);
        try (FileInputStream customReader = new FileInputStream(
                Paths.get(path, "custom.properties").toString())) {
            properties.load(customReader);
        } catch (FileNotFoundException ignored) {}
    }

    /**
     * Gets the property value associated with a certain property name.
     * @param key the property name
     * @return the property value
     */
    protected String get(String key) {
        return properties.getProperty(key);
    }
}
