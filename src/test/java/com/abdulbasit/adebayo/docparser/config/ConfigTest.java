package com.abdulbasit.adebayo.docparser.config;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ConfigTest {
    @Test
    void getters_ReturnDefaultValues() {
        Config config = new Config();
        assertNull(config.getInputCsv());
        assertNull(config.getInputXml());
        assertNull(config.getOutputPath());
        assertEquals("info", config.getLogLevel());
    }
}
