package com.abdulbasit.adebayo.docparser.config;

import com.abdulbasit.adebayo.docparser.exception.ConfigException;
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
                input_csv: test.csv
                input_xml: test.xml
                output_path: output/
                log_level: debug
                """;
        Path configFile = tempDir.resolve("config.yaml");
        Files.writeString(configFile, yamlContent);

        Config config = configLoader.loadConfig(configFile.toString());

        assertNotNull(config);
        assertEquals("test.csv", config.getInput_csv());
        assertEquals("test.xml", config.getInput_xml());
        assertEquals("output/", config.getOutput_path());
        assertEquals("debug", config.getLog_level());
    }

    @Test
    void loadConfig_MissingRequiredField_ThrowsConfigException(@TempDir Path tempDir) throws Exception {
        // Missing output_path
        String yamlContent = """
                input_csv: test.csv
                input_xml: test.xml
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
                input_csv: test.csv
                input_xml: test.xml
                output_path: output/
                """;
        Path configFile = tempDir.resolve("config.yaml");
        Files.writeString(configFile, yamlContent);

        Config config = configLoader.loadConfig(configFile.toString());

        assertNotNull(config);
        assertEquals("test.csv", config.getInput_csv());
        assertEquals("test.xml", config.getInput_xml());
        assertEquals("output/", config.getOutput_path());
    //    assertNull(config.getLog_level()); // Should be null when not specified
    }
}