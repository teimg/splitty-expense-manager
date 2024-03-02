package client.utils;

import utils.ConfigProvider;

import java.nio.file.Path;
import java.nio.file.Paths;

public class ClientConfiguration extends ConfigProvider {

    private static final Path location = Paths.get(
            "client", "src", "main", "resources", "client", "config");

    /**
     * Load a ClientConfiguration from known location.
     */
    public ClientConfiguration() {
        super(location.resolve("default.properties"), location.resolve("custom.properties"));
    }

    /**
     * Gets the configured server URL.
     * @return the URL
     */
    public String getServer() {
        return get("server");
    }

    /**
     * Gets the configured language to use on startup.
     * @return the name of the language, e.g. "english"
     */
    public String getStartupLanguage() {
        return get("language");
    }

    /**
     * Sets the configured language.
     * @param language = language
     */
    public void setStartupLanguage(String language) {
        set("language", language);
    }
}
