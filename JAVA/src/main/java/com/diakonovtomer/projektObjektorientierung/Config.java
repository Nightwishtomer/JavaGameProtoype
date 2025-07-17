package com.diakonovtomer.projektObjektorientierung;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Utility class for reading configuration values from an external {@code config.properties} file.
 * <p>
 * The configuration file is loaded once when the class is first accessed.
 * All configuration values can be accessed statically using {@link #get(String)} or {@link #get(String, String)}.
 * </p>
 *
 * <p><strong>Example:</strong></p>
 * <pre>{@code
 * String dbHost = Config.get("database.host", "localhost");
 * }</pre>
 *
 * <p>The {@code config.properties} file must be located in the working directory
 * of the application.</p>
 *
 * @author Artiem
 */
public class Config {
    
    
    /**
     * The internal properties object holding all loaded key-value pairs.
     */
    private static final Properties props = new Properties();

    // Static initializer block — runs when the class is first loaded
    static {
        try {
            FileInputStream fis = new FileInputStream("config.properties"); // путь к файлу
            props.load(fis);
        } catch (IOException e) {
            System.err.println("Failed to load configuration: " + e.getMessage());
        }
    }

    /**
     * Retrieves a configuration value by key.
     *
     * @param key the key to look up
     * @return the value associated with the key, or {@code null} if not found
     */
    public static String get(String key) {
        return props.getProperty(key);
    }

    /**
     * Retrieves a configuration value by key, or returns a default value if the key is not found.
     *
     * @param key          the key to look up
     * @param defaultValue the default value to return if key is missing
     * @return the value associated with the key, or {@code defaultValue} if not found
     */
    public static String get(String key, String defaultValue) {
        return props.getProperty(key, defaultValue);
    }
}
