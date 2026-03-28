package com.qa.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Reads environment-specific .properties files based on -Denv system property.
 * Defaults to "dev" if not specified.
 *
 * Usage:
 *   ConfigManager.get("jsonplaceholder.base.url")
 *   mvn test -Denv=ci
 */
public class ConfigManager {

    private static final Logger log = LogManager.getLogger(ConfigManager.class);
    private static final Properties properties = new Properties();

    static {
        String env = System.getProperty("env", "dev");
        String configPath = Paths.get("config", env + ".properties").toString();
        log.info("Loading configuration from: {}", configPath);

        // Try loading from filesystem first (relative to project root)
        try (InputStream fileStream = new FileInputStream(configPath)) {
            properties.load(fileStream);
            log.info("Loaded {} properties from filesystem: {}", properties.size(), configPath);
        } catch (IOException fsException) {
            // Fall back to classpath resource
            String classpathResource = "/" + env + ".properties";
            try (InputStream classpathStream = ConfigManager.class.getResourceAsStream(classpathResource)) {
                if (classpathStream != null) {
                    properties.load(classpathStream);
                    log.info("Loaded {} properties from classpath: {}", properties.size(), classpathResource);
                } else {
                    log.error("Could not find properties file at '{}' or classpath '{}'",
                            configPath, classpathResource);
                    throw new RuntimeException(
                            "Failed to load configuration for env='" + env + "'. " +
                            "Expected file at: " + configPath
                    );
                }
            } catch (IOException cpException) {
                log.error("Error loading properties from classpath: {}", cpException.getMessage());
                throw new RuntimeException("Failed to load configuration: " + cpException.getMessage(), cpException);
            }
        }
    }

    private ConfigManager() {
        // Utility class — prevent instantiation
    }

    /**
     * Returns the value for the given property key.
     *
     * @param key the property key
     * @return the property value
     * @throws RuntimeException if the key is not found
     */
    public static String get(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            throw new RuntimeException("Property '" + key + "' not found in configuration.");
        }
        return value.trim();
    }

    /**
     * Returns the value for the given property key, or the provided default if not found.
     *
     * @param key          the property key
     * @param defaultValue the default value to return if key is missing
     * @return the property value or defaultValue
     */
    public static String get(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue).trim();
    }

    /**
     * Returns the integer value for the given property key.
     *
     * @param key the property key
     * @return integer value of the property
     */
    public static int getInt(String key) {
        return Integer.parseInt(get(key));
    }

    /**
     * Returns the boolean value for the given property key.
     *
     * @param key the property key
     * @return boolean value of the property
     */
    public static boolean getBoolean(String key) {
        return Boolean.parseBoolean(get(key));
    }
}
