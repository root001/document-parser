package com.abdulbasit.adebayo.docparser.writer;

import com.abdulbasit.adebayo.docparser.model.Car;
import com.abdulbasit.adebayo.docparser.model.Price;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OutputWriterTest {
    private static final Price PRICE = new Price("USD", 25000);
    private static final List<Price> PRICE_LIST = List.of(PRICE);

    @Test
    void writeJson_RoundTrip_Success(@TempDir Path tempDir) throws Exception {
        List<Car> cars = List.of(
            new Car("SUV", "RAV4", PRICE, PRICE_LIST)
        );
        Path jsonFile = tempDir.resolve("cars.json");
        
        OutputWriter.writeJson(jsonFile, cars);
        List<Car> readCars = OutputWriter.getJsonMapperForTesting().readValue(
            Files.newBufferedReader(jsonFile),
            new TypeReference<List<Car>>() {});
            
        assertEquals(cars, readCars);
    }

    @Test
    void writeXml_RoundTrip_Success(@TempDir Path tempDir) throws Exception {
        List<Car> cars = List.of(
            new Car("SUV", "RAV4", PRICE, PRICE_LIST)
        );
        Path xmlFile = tempDir.resolve("cars.xml");
        
        OutputWriter.writeXml(xmlFile, cars);
        List<Car> readCars = OutputWriter.getXmlMapperForTesting().readValue(
            Files.newBufferedReader(xmlFile),
            new TypeReference<List<Car>>() {});
            
        assertEquals(cars, readCars);
    }

    @Test
    void writeXml_ValidSchema(@TempDir Path tempDir) throws Exception {
        List<Car> cars = List.of(
            new Car("SUV", "RAV4", PRICE, PRICE_LIST)
        );
        Path xmlFile = tempDir.resolve("cars.xml");
        OutputWriter.writeXml(xmlFile, cars);
        
        String xmlContent = Files.readString(xmlFile);
        assertTrue(xmlContent.contains("<type>SUV</type>"));
        assertTrue(xmlContent.contains("<model>RAV4</model>"));
        assertTrue(xmlContent.contains("<price currency=\"USD\">25000.0</price>"));
    }

    @Test
    void writeJson_NullPrice_HandlesCorrectly(@TempDir Path tempDir) throws Exception {
        List<Car> cars = List.of(
            new Car("SUV", "RAV4", null, PRICE_LIST)
        );
        Path jsonFile = tempDir.resolve("cars.json");
        
        OutputWriter.writeJson(jsonFile, cars);
        String jsonContent = Files.readString(jsonFile);
        assertTrue(jsonContent.contains("\"price\":null"));
        assertTrue(jsonContent.contains("\"priceList\":[{\"currency\":\"USD\",\"amount\":25000.0}]"));
    }

    @Test
    void writeXml_MultiplePrices_IncludesAll(@TempDir Path tempDir) throws Exception {
        List<Price> multiplePrices = List.of(
            new Price("USD", 25000),
            new Price("EUR", 22000)
        );
        List<Car> cars = List.of(
            new Car("SUV", "RAV4", null, multiplePrices)
        );
        Path xmlFile = tempDir.resolve("cars.xml");
        
        OutputWriter.writeXml(xmlFile, cars);
        String xmlContent = Files.readString(xmlFile);
        assertTrue(xmlContent.contains("<priceList><price currency=\"USD\">25000.0</price><price currency=\"EUR\">22000.0</price></priceList>"));
    }
}
