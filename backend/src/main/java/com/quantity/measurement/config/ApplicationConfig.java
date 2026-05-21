package com.quantity.measurement.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ApplicationConfig {

    private static final Properties properties = new Properties();

    static {
        try (
                InputStream inputStream =
                        ApplicationConfig.class
                                .getClassLoader()
                                .getResourceAsStream(
                                        "application.properties")
        ) {
            if (inputStream == null) {
                throw new RuntimeException("application.properties not found");
            }

            properties.load(inputStream);

        } catch (IOException e) {
            throw new RuntimeException("Failed to load properties", e);
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    public static int getIntProperty(String key) {
        return Integer.parseInt(properties.getProperty(key));
    }
}