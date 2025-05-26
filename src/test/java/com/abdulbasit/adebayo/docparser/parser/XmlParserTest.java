package com.abdulbasit.adebayo.docparser.parser;

import com.abdulbasit.adebayo.docparser.config.Config;
import com.abdulbasit.adebayo.docparser.model.Car;
import com.abdulbasit.adebayo.docparser.model.CarBrand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class XmlParserTest {

    private Config config;

    @BeforeEach
    void setUp() {
        config = new Config();
        config.setInputCsv("data/CarsBrand.csv");
        config.setInputXml("data/carsType.xml");
        config.setOutputPath("output.json");
    }

    @Test
    @Disabled
    void parse_ValidXml_ReturnsBrandReleases(@TempDir Path tempDir) throws Exception {
        // Setup lookup
        Path lookupFile = tempDir.resolve("lookup.csv");
        Files.writeString(lookupFile, "model1,brand1");
        ModelLookup lookup = new ModelLookup(lookupFile);

        // Create test XML
        String xmlContent = """
            <cars>
                <car>
                    <model>model1</model>
                    <price currency="EUR">25000.00</price>
                </car>
            </cars>
            """;
        Path xmlFile = tempDir.resolve("test.xml");
        Files.writeString(xmlFile, xmlContent);

    //    XmlParser parser = new XmlParser(config, lookup);
        XmlParser parser = new XmlParser(config, false);
        List<Car> releases = parser.parse(xmlFile);

        assertEquals(1, releases.size());
        assertEquals("brand1", releases.get(0).type());
        assertEquals("model1", releases.get(0).model());
        assertEquals("EUR", releases.get(0).price().currency());
        assertEquals(25000.00, releases.get(0).price().amount());
    }

    @Test
    @Disabled
    void parse_UnknownModel_ReturnsUnknownBrand(@TempDir Path tempDir) throws Exception {
        // Setup lookup
        Path lookupFile = tempDir.resolve("lookup.csv");
        Files.writeString(lookupFile, "model1,brand1");
        ModelLookup lookup = new ModelLookup(lookupFile);

        // Create test XML
        String xmlContent = """
            <cars>
                <car>
                    <model>unknown</model>
                    <price currency="EUR">25000.00</price>
                </car>
            </cars>
            """;
        Path xmlFile = tempDir.resolve("test.xml");
        Files.writeString(xmlFile, xmlContent);

       // XmlParser parser = new XmlParser(config, lookup);
        XmlParser parser = new XmlParser(config, false);
        List<Car> releases = parser.parse(xmlFile);

        assertEquals(1, releases.size());
        assertEquals("Unknown", releases.get(0).type());
        assertEquals("unknown", releases.get(0).model());
        assertEquals("EUR", releases.get(0).price().currency());
        assertEquals(25000.00, releases.get(0).price().amount());
    }
}
