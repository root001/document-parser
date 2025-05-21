package com.abdulbasit.adebayo.docparser.parser;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.Path;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;

class ModelLookupTest {
    @Test
    void getBrandForModel_KnownModel_ReturnsBrand(@TempDir Path tempDir) throws IOException {
        String csvContent = "model1,brand1\nmodel2,brand2";
        Path lookupFile = tempDir.resolve("lookup.csv");
        Files.writeString(lookupFile, csvContent);

        ModelLookup lookup = new ModelLookup(lookupFile);
        assertEquals("brand1", lookup.getBrandForModel("model1"));
        assertEquals("brand1", lookup.getBrandForModel("MODEL1")); // case insensitive
    }

    @Test
    void getBrandForModel_CaseSensitive_RespectsCase(@TempDir Path tempDir) throws IOException {
        String csvContent = "Model1,brand1";
        Path lookupFile = tempDir.resolve("lookup.csv");
        Files.writeString(lookupFile, csvContent);

        ModelLookup lookup = new ModelLookup(lookupFile, true);
        assertEquals("brand1", lookup.getBrandForModel("Model1"));
        assertEquals("Unknown", lookup.getBrandForModel("model1")); // case sensitive
    }

    @Test
    void getBrandForModel_InvalidFile_ThrowsException(@TempDir Path tempDir) {
        Path invalidFile = tempDir.resolve("nonexistent.csv");
        assertThrows(IOException.class, () -> new ModelLookup(invalidFile));
    }

    @Test
    void getBrandForModel_NullModel_ReturnsUnknown(@TempDir Path tempDir) throws IOException {
        Path lookupFile = tempDir.resolve("lookup.csv");
        Files.writeString(lookupFile, "model1,brand1");

        ModelLookup lookup = new ModelLookup(lookupFile);
        assertEquals("Unknown", lookup.getBrandForModel(null));
    }

    @Test
    void getBrandForModel_EmptyModel_ReturnsUnknown(@TempDir Path tempDir) throws IOException {
        Path lookupFile = tempDir.resolve("lookup.csv");
        Files.writeString(lookupFile, "model1,brand1");

        ModelLookup lookup = new ModelLookup(lookupFile);
        assertEquals("Unknown", lookup.getBrandForModel(""));
    }
}
