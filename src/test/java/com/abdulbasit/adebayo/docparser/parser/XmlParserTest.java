package com.abdulbasit.adebayo.docparser.parser;

import com.abdulbasit.adebayo.docparser.model.BrandRelease;
import com.abdulbasit.adebayo.docparser.exception.ParseException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.Path;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class XmlParserTest {
    @Test
    void parse_ValidXml_ReturnsBrandReleases(@TempDir Path tempDir) throws Exception {
        // Setup lookup
        Path lookupFile = tempDir.resolve("lookup.csv");
        Files.writeString(lookupFile, "model1,brand1");
        ModelLookup lookup = new ModelLookup(lookupFile);

        // Create test XML
        String xmlContent = """
            <products>
                <product>
                    <model>model1</model>
                    <version>1.0</version>
                    <releaseDate>01/15/2023</releaseDate>
                </product>
            </products>
            """;
        Path xmlFile = tempDir.resolve("test.xml");
        Files.writeString(xmlFile, xmlContent);

        XmlParser parser = new XmlParser(lookup);
        List<BrandRelease> result = parser.parse(xmlFile);
        
        assertEquals(1, result.size());
        assertEquals("brand1", result.get(0).brandName());
    }

    @Test
    void parse_UnknownModel_ReturnsUnknownBrand(@TempDir Path tempDir) throws Exception {
        // Setup empty lookup
        Path lookupFile = tempDir.resolve("lookup.csv");
        Files.writeString(lookupFile, "");
        ModelLookup lookup = new ModelLookup(lookupFile);

        // Create test XML
        String xmlContent = """
            <products>
                <product>
                    <model>unknownModel</model>
                    <version>1.0</version>
                    <releaseDate>01/15/2023</releaseDate>
                </product>
            </products>
            """;
        Path xmlFile = tempDir.resolve("test.xml");
        Files.writeString(xmlFile, xmlContent);

        XmlParser parser = new XmlParser(lookup);
        List<BrandRelease> result = parser.parse(xmlFile);
        
        assertEquals("Unknown", result.get(0).brandName());
    }
}
