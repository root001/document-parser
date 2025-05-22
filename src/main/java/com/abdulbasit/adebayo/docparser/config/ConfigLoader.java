package com.abdulbasit.adebayo.docparser.config;

import com.abdulbasit.adebayo.docparser.exception.ConfigException;
import org.apache.logging.log4j.Level;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.apache.logging.log4j.core.config.Configurator;

public class ConfigLoader {
    private static final Logger logger = LoggerFactory.getLogger(ConfigLoader.class);
    
    public Config loadConfig(String filePath) throws ConfigException {
        try (InputStream inputStream = Files.newInputStream(Paths.get(filePath))) {
            Yaml yaml = new Yaml(new Constructor(Config.class));
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
            // Update log level if specified in config
            if (config.getLog_level() != null) {
                Configurator.setRootLevel(Level.valueOf(config.getLog_level().toUpperCase()));
                logger.info("Set log level to: {}", config.getLog_level());
            }
            return config;
        } catch (Exception e) {
            throw new ConfigException("Failed to load config: " + e.getMessage());
        }
    }
}
