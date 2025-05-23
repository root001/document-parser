package com.abdulbasit.adebayo.docparser.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ConfigExceptionTest {
    @Test
    void constructor_WithMessage_ContainsMessage() {
        String message = "Test error message";
        ConfigException exception = new ConfigException(message);
        assertEquals(message, exception.getMessage());
    }

    @Test
    void constructor_WithNullMessage_Works() {
        ConfigException exception = new ConfigException(null);
        assertNull(exception.getMessage());
    }
}
