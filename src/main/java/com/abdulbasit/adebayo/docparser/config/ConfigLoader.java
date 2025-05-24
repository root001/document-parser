package com.abdulbasit.adebayo.docparser.config;

import com.abdulbasit.adebayo.docparser.exception.ConfigException;
import org.springframework.context.annotation.Bean;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public class ConfigLoader {
    private static final Logger logger = LoggerFactory.getLogger(ConfigLoader.class);

    public Config loadConfig(String filePath) throws ConfigException {
        try (InputStream inputStream = Files.newInputStream(Paths.get(filePath))) {
            LoaderOptions loaderOptions = new LoaderOptions();
            Yaml yaml = new Yaml(new Constructor(Config.class, loaderOptions));

            Config config = yaml.load(inputStream);

            if (config.getInputCsv() == null) {
                throw new ConfigException("Missing required field: inputCsv");
            }
            if (config.getInputXml() == null) {
                throw new ConfigException("Missing required field: inputXml");
            }
            if (config.getOutputPath() == null) {
                throw new ConfigException("Missing required field: outputPath");
            }

            logger.info("Loaded configuration: {}", config);

            // Update log level if specified in config
            if (config.getLogLevel() != null) {
                setLogLevel(config.getLogLevel());
            }

            return config;
        } catch (Exception e) {
            throw new ConfigException("Failed to load config: " + e.getMessage());
        }
    }

    /**
     * Sets log level using SLF4J/Logback approach
     */
    private void setLogLevel(String logLevel) {
        try {
            ch.qos.logback.classic.Logger rootLogger =
                    (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
            rootLogger.setLevel(ch.qos.logback.classic.Level.valueOf(logLevel.toUpperCase()));
            logger.info("Set log level to: {}", logLevel);
        } catch (Exception e) {
            logger.warn("Could not set log level to {}: {}", logLevel, e.getMessage());
        }
    }

    @Bean
    public Config config() {
        try {
            return loadConfig("config.yaml");
        } catch (ConfigException e) {
            logger.error("Failed to load configuration: {}", e.getMessage());
            throw new RuntimeException("Failed to load configuration", e);
        }
    }
}