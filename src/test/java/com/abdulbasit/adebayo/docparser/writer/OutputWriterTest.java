package com.abdulbasit.adebayo.docparser.writer;

import com.abdulbasit.adebayo.docparser.model.CarBrand;
import com.abdulbasit.adebayo.docparser.model.Price;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.xmlunit.assertj.XmlAssert.assertThat;

class OutputWriterTest {
    private static final Price PRICE = new Price("USD", 25000);
    private static final List<Price> PRICE_LIST = List.of(PRICE);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void writeJson_RoundTrip_Success(@TempDir Path tempDir) throws Exception {
        List<CarBrand> carBrands = List.of(
            new CarBrand("SUV", "Toyota", "RAV4", 
                LocalDate.of(2023, 1, 15), PRICE, PRICE_LIST)
        );
        Path jsonFile = tempDir.resolve("cars.json");
        
        OutputWriter.writeJson(jsonFile, carBrands);
        List<CarBrand> readCarBrands = OutputWriter.getJsonMapperForTesting().readValue(
            Files.newBufferedReader(jsonFile),
            new TypeReference<List<CarBrand>>() {});
            
        assertEquals(carBrands, readCarBrands);
    }

    @Test
    void writeXml_RoundTrip_Success(@TempDir Path tempDir) throws Exception {
        List<CarBrand> carBrands = List.of(
            new CarBrand("SUV", "Toyota", "RAV4",
                LocalDate.of(2023, 1, 15), PRICE, PRICE_LIST)
        );
        Path xmlFile = tempDir.resolve("cars.xml");
        
        OutputWriter.writeXml(xmlFile, carBrands);
        List<CarBrand> readCarBrands = OutputWriter.getXmlMapperForTesting().readValue(
            Files.newBufferedReader(xmlFile),
            new TypeReference<List<CarBrand>>() {});
            
        assertEquals(carBrands, readCarBrands);
    }

    @Test
    void writeXml_ValidSchema(@TempDir Path tempDir) throws Exception {
        List<CarBrand> carBrands = List.of(
            new CarBrand("SUV", "Toyota", "RAV4",
                LocalDate.of(2023, 1, 15), PRICE, PRICE_LIST)
        );
        Path xmlFile = tempDir.resolve("cars.xml");
        OutputWriter.writeXml(xmlFile, carBrands);
        
        String xmlContent = Files.readString(xmlFile);
        assertTrue(xmlContent.contains("<brandType>SUV</brandType>"));
        assertTrue(xmlContent.contains("<productName>Toyota</productName>"));
        assertTrue(xmlContent.contains("<model>RAV4</model>"));
        assertTrue(xmlContent.contains("<currency>USD</currency>"));
        assertTrue(xmlContent.contains("<amount>25000.0</amount>"));
    }

    @Test
    void writeJson_NullPrice_HandlesCorrectly(@TempDir Path tempDir) throws Exception {
        List<CarBrand> carBrands = List.of(
            new CarBrand("SUV", "Toyota", "RAV4",
                LocalDate.of(2023, 1, 15), null, PRICE_LIST)
        );
        Path jsonFile = tempDir.resolve("cars.json");
        
        OutputWriter.writeJson(jsonFile, carBrands);
        String jsonContent = Files.readString(jsonFile);
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        List<CarBrand> actualCarBrands = objectMapper.readValue(jsonContent, 
            new TypeReference<List<CarBrand>>() {});
            
        assertEquals(carBrands, actualCarBrands);
    }

    @Test
    void writeXml_MultiplePrices_IncludesAll(@TempDir Path tempDir) throws Exception {
        List<Price> multiplePrices = List.of(
            new Price("USD", 25000.0),
            new Price("EUR", 22000.0)
        );
        List<CarBrand> carBrands = List.of(
            new CarBrand("SUV", "Toyota", "RAV4",
                LocalDate.of(2023, 1, 15), null, multiplePrices)
        );
        Path xmlFile = tempDir.resolve("cars.xml");

        OutputWriter.writeXml(xmlFile, carBrands);
        String xmlContent = Files.readString(xmlFile);
/**
        assertThat(xmlContent)
            .nodesByXPath("//CarBrand/priceList/priceList[currency='USD']")
            .exist()
            .hasSize(1);

        assertThat(xmlContent)
            .nodesByXPath("//CarBrand/priceList/priceList[currency='EUR']")
            .exist()
            .hasSize(1);
**/
        assertTrue(xmlContent.contains("<currency>USD</currency>"));
        assertTrue(xmlContent.contains("<amount>25000.0</amount>"));
        assertTrue(xmlContent.contains("<currency>EUR</currency>"));
        assertTrue(xmlContent.contains("<amount>22000.0</amount>"));
    }
}
