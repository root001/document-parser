package com.abdulbasit.adebayo.docparser;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingTest {
    private static final Logger logger = LoggerFactory.getLogger(LoggingTest.class);

    @Test
    public void testLogging() {
        logger.trace("This is a TRACE message");
        logger.debug("This is a DEBUG message");
        logger.info("This is an INFO message");
        logger.warn("This is a WARN message");
        logger.error("This is an ERROR message");
    }
}
