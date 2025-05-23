package com.abdulbasit.adebayo.docparser.config;

import com.abdulbasit.adebayo.docparser.exception.ConfigException;
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
            // You can configure loaderOptions here if needed, e.g.:
            // loaderOptions.setAllowDuplicateKeys(false); // Default is true in 2.x, was false in 1.x
            // loaderOptions.setProcessComments(true); // If you need to process comments
            Yaml yaml = new Yaml(new Constructor(Config.class, loaderOptions));

            Config config = yaml.load(inputStream);

            if (config.getInput_csv() == null) {
                throw new ConfigException("Missing required field: inputCsv");
            }
            if (config.getInput_xml() == null) {
                throw new ConfigException("Missing required field: inputXml");
            }
            if (config.getOutput_path() == null) {
                throw new ConfigException("Missing required field: outputPath");
            }

            logger.info("Loaded configuration: {}", config);

            // Update log level if specified in config (using SLF4J approach)
            if (config.getLog_level() != null) {
                setLogLevel(config.getLog_level());
            }

            return config;
        } catch (Exception e) {
            throw new ConfigException("Failed to load config: " + e.getMessage());
        }
    }

    /**
     * Sets log level using SLF4J/Logback approach instead of Log4j
     */
    private void setLogLevel(String logLevel) {
        try {
            // For Logback (most common with Spring Boot)
            ch.qos.logback.classic.Logger rootLogger =
                    (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
            rootLogger.setLevel(ch.qos.logback.classic.Level.valueOf(logLevel.toUpperCase()));
            logger.info("Set log level to: {}", logLevel);
        } catch (Exception e) {
            logger.warn("Could not set log level to {}: {}", logLevel, e.getMessage());
        }
    }
}
