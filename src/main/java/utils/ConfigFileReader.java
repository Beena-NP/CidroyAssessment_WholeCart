package utils;

import java.io.InputStream;
import java.util.Properties;

public class ConfigFileReader {

    private Properties properties;

    public ConfigFileReader() {

        properties = new Properties();

        try (InputStream input = getClass().getClassLoader()
                .getResourceAsStream("testdata.properties")) {

            if (input == null) {
                throw new RuntimeException("testdata.properties file not found");
            }

            properties.load(input);

        } catch (Exception e) {
            throw new RuntimeException("Unable to load testdata.properties", e);
        }
    }

    public String getBaseUrl() {
        return properties.getProperty("base_url");
    }

    public String getUsername() {
        String username = System.getenv("BUYER_USERNAME");
        if (username == null || username.isBlank()) {
            throw new RuntimeException(
                    "BUYER_USERNAME environment variable is not set."
            );
        }
        return username;
    }

        public String getPassword () {
            String password = System.getenv("BUYER_PASSWORD");
            if (password == null || password.isBlank()) {

                throw new RuntimeException("BUYER_PASSWORD environment variable is not set.");
            }
            return password;
        }
}
