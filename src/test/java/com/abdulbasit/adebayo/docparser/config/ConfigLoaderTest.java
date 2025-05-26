package com.abdulbasit.adebayo.docparser.config;

import com.abdulbasit.adebayo.docparser.exception.ConfigException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.api.BeforeEach;

import java.nio.file.Files;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.*;

class ConfigLoaderTest {

    private ConfigLoader configLoader;

    @BeforeEach
    void setUp() {
        configLoader = new ConfigLoader();
    }

    @Test
    void loadConfig_ValidConfig_ReturnsConfig(@TempDir Path tempDir) throws Exception {
        // Create a test YAML file
        String yamlContent = """
                inputCsv: test.csv
                inputXml: test.xml
                outputPath: output/
                logLevel: debug
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
        // Missing output_path
        String yamlContent = """
                inputCsv: test.csv
                inputXml: test.xml
                """;
        Path configFile = tempDir.resolve("config.yaml");
        Files.writeString(configFile, yamlContent);

        ConfigException exception = assertThrows(ConfigException.class, () ->
                configLoader.loadConfig(configFile.toString()));

        assertTrue(exception.getMessage().contains("outputPath"));
    }

    @Test
    void loadConfig_InvalidFile_ThrowsConfigException() {
        ConfigException exception = assertThrows(ConfigException.class, () ->
                configLoader.loadConfig("nonexistent.yaml"));

        assertTrue(exception.getMessage().contains("Failed to load config"));
    }

    @Test
    void loadConfig_EmptyFile_ThrowsConfigException(@TempDir Path tempDir) throws Exception {
        String yamlContent = "";
        Path configFile = tempDir.resolve("empty.yaml");
        Files.writeString(configFile, yamlContent);

        assertThrows(ConfigException.class, () ->
                configLoader.loadConfig(configFile.toString()));
    }

    @Test
    void loadConfig_WithoutLogLevel_LoadsSuccessfully(@TempDir Path tempDir) throws Exception {
        String yamlContent = """
                inputCsv: test.csv
                inputXml: test.xml
                outputPath: output/
                """;
        Path configFile = tempDir.resolve("config.yaml");
        Files.writeString(configFile, yamlContent);

        Config config = configLoader.loadConfig(configFile.toString());

        assertNotNull(config);
        assertEquals("test.csv", config.getInputCsv());
        assertEquals("test.xml", config.getInputXml());
        assertEquals("output/", config.getOutputPath());
    //    assertNull(config.getLog_level()); // Should be null when not specified
    }
}