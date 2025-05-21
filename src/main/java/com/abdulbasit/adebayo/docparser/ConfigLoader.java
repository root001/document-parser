package com.abdulbasit.adebayo.docparser;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ConfigLoader {
    private static final Logger logger = LoggerFactory.getLogger(ConfigLoader.class);
    
    public Config loadConfig(String filePath) throws ConfigException {
        try (InputStream inputStream = Files.newInputStream(Paths.get(filePath))) {
            Yaml yaml = new Yaml(new Constructor(Config.class));
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
                org.apache.logging.log4j.core.config.Configurator.setRootLevel(
                    org.apache.logging.log4j.Level.valueOf(config.getLogLevel().toUpperCase()));
                logger.info("Set log level to: {}", config.getLogLevel());
            }
            return config;
        } catch (Exception e) {
            throw new ConfigException("Failed to load config: " + e.getMessage());
        }
    }
}
