package com.abdulbasit.adebayo.docparser.writer;

import com.abdulbasit.adebayo.docparser.model.Car;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.dataformat.xml.XmlAnnotationIntrospector;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class OutputWriter {
    private static final ObjectMapper jsonMapper;
    private static final XmlMapper xmlMapper; // Line 26 in your error likely refers to the initialization below

    static {
        jsonMapper = new ObjectMapper();
        jsonMapper.enable(SerializationFeature.INDENT_OUTPUT);
        // If you need to prevent dates from being written as timestamps:
        // jsonMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // This is the line that would trigger the error if XmlMapper class is not found
        // If your line 26 error points here, it's definitive.
        xmlMapper = new XmlMapper();
        xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
        // If you need to prevent dates from being written as timestamps:
        // xmlMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public static void writeJson(Path outputPath, List<Car> cars) throws IOException {
        Files.createDirectories(outputPath.getParent());
        jsonMapper.writeValue(outputPath.toFile(), cars);
    }

    public static void writeXml(Path outputPath, List<Car> cars) throws IOException {
        Files.createDirectories(outputPath.getParent());
        
        // Configure XML output
        xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
        // Configure XML output
        xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
        
        // Configure XML root element name
        xmlMapper.setAnnotationIntrospector(new XmlAnnotationIntrospector());
        xmlMapper.writeValue(outputPath.toFile(), cars);
    }

    // For testing purposes only
    static ObjectMapper getJsonMapperForTesting() {
        return jsonMapper;
    }

    // For testing purposes only
    static XmlMapper getXmlMapperForTesting() {
        return xmlMapper;
    }
}
