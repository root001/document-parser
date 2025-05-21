package com.abdulbasit.adebayo.docparser.writer;

import com.abdulbasit.adebayo.docparser.model.Car;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class OutputWriter {
    private static final ObjectMapper jsonMapper = new ObjectMapper()
        .enable(SerializationFeature.INDENT_OUTPUT);
    
    private static final XmlMapper xmlMapper = (XmlMapper) new XmlMapper()
        .enable(SerializationFeature.INDENT_OUTPUT)
        .findAndRegisterModules();

    public static void writeJson(Path outputPath, List<Car> cars) throws IOException {
        jsonMapper.writeValue(Files.newBufferedWriter(outputPath), cars);
    }

    public static void writeXml(Path outputPath, List<Car> cars) throws IOException {
        xmlMapper.writeValue(Files.newBufferedWriter(outputPath), cars);
    }
}
