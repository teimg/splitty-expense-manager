package utils;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public abstract class ConfigProvider {
    private final FileBasedConfigurationBuilder<FileBasedConfiguration> builder;
    private final Configuration properties;

    /**
     * Attempts to read a default and a custom configuration file
     * and with this initializes a Configuration object.
     * Crashes if something goes wrong while reading the configuration files.
     * @param defaultPath filepath pointing to default config location
     * @param customPath filepath pointing to custom config location
     */
    protected ConfigProvider(Path defaultPath, Path customPath) {
        // Creates the custom configuration file if it doesn't already exist.
        if (!Files.exists(customPath)) {
            try {
                Files.copy(defaultPath, customPath);
            } catch (IOException e) {
                // Crash on file creation error.
                throw new RuntimeException("couldn't create the custom configuration file");
            }
        }

        // Create a builder for the configuration file.
        builder = new
                FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
                        .configure(new Parameters().properties().setPath(customPath.toString()));

        // Load the configuration.
        try {
            properties = builder.getConfiguration();
        } catch (ConfigurationException e) {
            // Crash on loading config error.
            throw new RuntimeException("couldn't read the custom configuration file");
        }
    }

    /**
     * Gets the property value associated with a certain property name.
     * Returns null if the key cannot be found.
     * @param key the property name
     * @return the property value
     */
    protected String get(String key) {
        return properties.getString(key);
    }

    /**
     * Setter method to adapt properties in the properties file.
     * @param key - Key
     * @param value - Value
     */
    protected void set(String key, String value) {
        properties.setProperty(key, value);
    }

    /**
     * Saves the current configuration to disk.
     */
    public void save() {
        try {
            builder.save();
        } catch (ConfigurationException e) {
            // TODO: handle writing config exception
            throw new RuntimeException(e);
        }
    }
}
