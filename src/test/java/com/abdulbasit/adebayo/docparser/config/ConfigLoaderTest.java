package com.abdulbasit.adebayo.docparser.config;

import com.abdulbasit.adebayo.docparser.exception.ConfigException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.Files;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.*;

class ConfigLoaderTest {
    private final ConfigLoader configLoader = new ConfigLoader();

    @Test
    void loadConfig_ValidConfig_ReturnsConfig(@TempDir Path tempDir) throws Exception {
        // Create a test YAML file
        String yamlContent = """
                input_csv: test.csv
                input_xml: test.xml
                output_path: output/
                log_level: debug
                """;
        Path configFile = tempDir.resolve("config.yaml");
        Files.writeString(configFile, yamlContent);

        Config config = configLoader.loadConfig(configFile.toString());

        assertNotNull(config);
        assertEquals("test.csv", config.getInputCsv());
        assertEquals("test.xml", config.getInputXml());
        assertEquals("output/", config.getOutputPath());
        assertEquals("debug", config.getLogLevel());
    }

    @Test
    void loadConfig_MissingRequiredField_ThrowsConfigException(@TempDir Path tempDir) throws Exception {
        // Missing outputPath
        String yamlContent = """
                inputCsv: test.csv
                inputXml: test.xml
                """;
        Path configFile = tempDir.resolve("config.yaml");
        Files.writeString(configFile, yamlContent);

        assertThrows(ConfigException.class, () -> 
            configLoader.loadConfig(configFile.toString()));
    }

    @Test
    void loadConfig_InvalidFile_ThrowsConfigException() {
        assertThrows(ConfigException.class, () -> 
            configLoader.loadConfig("nonexistent.yaml"));
    }
}
