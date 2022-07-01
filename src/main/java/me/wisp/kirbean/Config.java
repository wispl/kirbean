package me.wisp.kirbean;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class Config {
    private final Properties properties;

    public Config(String configPath) {
        properties = new Properties();
        try (FileInputStream stream = new FileInputStream(configPath)){
            properties.load(stream);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Config file not found: " + configPath, e);
        } catch (IOException e) {
            throw new RuntimeException("Error reading config file", e);
        }
    }

    public String getToken() {
        return properties.getProperty("token");
    }
}
