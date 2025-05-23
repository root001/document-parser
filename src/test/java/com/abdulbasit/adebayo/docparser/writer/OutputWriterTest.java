package com.abdulbasit.adebayo.docparser.writer;

import com.abdulbasit.adebayo.docparser.model.Car;
import com.abdulbasit.adebayo.docparser.model.Price;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.xmlunit.assertj.XmlAssert.assertThat;

import org.xmlunit.assertj.XmlAssert;

class OutputWriterTest {
    private static final Price PRICE = new Price("USD", 25000);
    private static final List<Price> PRICE_LIST = List.of(PRICE);
    private static final ObjectMapper objectMapper = new ObjectMapper();

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
        System.out.println("xmlContent :"+xmlContent);
        assertTrue(xmlContent.contains("<type>SUV</type>"));
        assertTrue(xmlContent.contains("<model>RAV4</model>"));
        assertTrue(xmlContent.contains("<currency>USD</currency>"));
        assertTrue(xmlContent.contains("<amount>25000.0</amount>"));
    }

    @Test
    void writeJson_NullPrice_HandlesCorrectly(@TempDir Path tempDir) throws Exception {
        List<Car> cars = List.of(
            new Car("SUV", "RAV4", null, PRICE_LIST)
        );
        Path jsonFile = tempDir.resolve("cars.json");
        
        OutputWriter.writeJson(jsonFile, cars);
        String jsonContent = Files.readString(jsonFile);
        // Parse the JSON content
        List<Car> actualCars = objectMapper.readValue(jsonContent, new TypeReference<>() {});
        // Create the expected object
        List<Car> expectedCars = List.of(
                new Car("SUV", "RAV4", null, List.of(new Price("USD", 25000.0)))
        );

        // Assert equality
        assertEquals(expectedCars, actualCars);
    }

    @Test
    void writeXml_MultiplePrices_IncludesAll(@TempDir Path tempDir) throws Exception {
        List<Price> multiplePrices = List.of(
                new Price("USD", 25000.0),
                new Price("EUR", 22000.0)
        );
        List<Car> cars = List.of(
                new Car("SUV", "RAV4", null, multiplePrices)
        );
        Path xmlFile = tempDir.resolve("cars.xml");

        OutputWriter.writeXml(xmlFile, cars);
        String xmlContent = Files.readString(xmlFile);
        System.out.println("Generated XML:\n" + xmlContent);

        // Updated XPath assertions to match current XML structure
        assertThat(xmlContent)
                .nodesByXPath("//Car/priceList/priceList[currency='USD']")
                .exist()
                .hasSize(1);

        assertThat(xmlContent)
                .nodesByXPath("//Car/priceList/priceList[currency='EUR']")
                .exist()
                .hasSize(1);

        assertTrue(xmlContent.contains("<currency>USD</currency>"));
        assertTrue(xmlContent.contains("<amount>25000.0</amount>"));
        assertTrue(xmlContent.contains("<currency>EUR</currency>"));
        assertTrue(xmlContent.contains("<amount>22000.0</amount>"));
    }
}
