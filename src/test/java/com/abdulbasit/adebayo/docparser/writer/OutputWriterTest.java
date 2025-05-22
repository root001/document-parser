package com.abdulbasit.adebayo.docparser.writer;

import com.abdulbasit.adebayo.docparser.model.Car;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class OutputWriterTest {
    @Test
    void writeJson_RoundTrip_Success(@TempDir Path tempDir) throws Exception {
        List<Car> cars = List.of(
            new Car("Toyota", "RAV4", 25000, LocalDate.of(2023, 1, 15))
        );
        Path jsonFile = tempDir.resolve("cars.json");
        
        OutputWriter.writeJson(jsonFile, cars);
        List<Car> readCars = OutputWriter.jsonMapper.readValue(
            Files.newBufferedReader(jsonFile),
            new TypeReference<List<Car>>() {});
            
        assertEquals(cars, readCars);
    }

    @Test
    void writeXml_RoundTrip_Success(@TempDir Path tempDir) throws Exception {
        List<Car> cars = List.of(
            new Car("Toyota", "RAV4", 25000, LocalDate.of(2023, 1, 15))
        );
        Path xmlFile = tempDir.resolve("cars.xml");
        
        OutputWriter.writeXml(xmlFile, cars);
        List<Car> readCars = OutputWriter.xmlMapper.readValue(
            Files.newBufferedReader(xmlFile), 
            new TypeReference<List<Car>>() {});
            
        assertEquals(cars, readCars);
    }

    @Test
    void writeXml_ValidSchema(@TempDir Path tempDir) throws Exception {
        List<Car> cars = List.of(
            new Car("Toyota", "RAV4", 25000, LocalDate.of(2023, 1, 15))
        );
        Path xmlFile = tempDir.resolve("cars.xml");
        OutputWriter.writeXml(xmlFile, cars);
        
        String xmlContent = Files.readString(xmlFile);
        assertTrue(xmlContent.contains("<releaseDate>2023,15,01</releaseDate>"));
        assertTrue(xmlContent.contains("<brand>Toyota</brand>"));
    }
}
