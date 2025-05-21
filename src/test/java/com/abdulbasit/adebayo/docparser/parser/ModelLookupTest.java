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
    }

    @Test
    void getBrandForModel_UnknownModel_ReturnsUnknown(@TempDir Path tempDir) throws IOException {
        String csvContent = "model1,brand1";
        Path lookupFile = tempDir.resolve("lookup.csv");
        Files.writeString(lookupFile, csvContent);

        ModelLookup lookup = new ModelLookup(lookupFile);
        assertEquals("Unknown", lookup.getBrandForModel("unknownModel"));
    }

    @Test
    void getBrandForModel_EmptyFile_ReturnsUnknown(@TempDir Path tempDir) throws IOException {
        Path lookupFile = tempDir.resolve("empty.csv");
        Files.writeString(lookupFile, "");

        ModelLookup lookup = new ModelLookup(lookupFile);
        assertEquals("Unknown", lookup.getBrandForModel("anyModel"));
    }
}
