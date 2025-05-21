package com.abdulbasit.adebayo.docparser.config;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ConfigTest {
    @Test
    void getters_ReturnDefaultValues() {
        Config config = new Config();
        assertNull(config.getInput_csv());
        assertNull(config.getInput_xml());
        assertNull(config.getOutput_path());
        assertEquals("info", config.getLog_level());
    }
}
