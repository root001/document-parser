package com.abdulbasit.adebayo.docparser.parser;

import com.abdulbasit.adebayo.docparser.model.BrandRelease;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class DataMergerTest {
    private final DataMerger merger = new DataMerger();

    @Test
    void mergeData_WithMatchingProduct_InheritsCsvDate() {
        List<BrandRelease> csvData = List.of(
            new BrandRelease("Toyota", "RAV4", LocalDate.of(2023, 1, 15), "v1.0")
        );
        
        List<BrandRelease> xmlData = List.of(
            new BrandRelease("Toyota", "RAV4", LocalDate.of(2023, 6, 1), "v1.0")
        );

        List<BrandRelease> result = merger.mergeData(csvData, xmlData);
        
        assertEquals(LocalDate.of(2023, 1, 15), result.get(0).releaseDate());
    }

    @Test
    void mergeData_WithMissingProduct_KeepsXmlDate() {
        List<BrandRelease> csvData = List.of(
            new BrandRelease("Toyota", "RAV4", LocalDate.of(2023, 1, 15), "v1.0")
        );
        
        List<BrandRelease> xmlData = List.of(
            new BrandRelease("Honda", "CR-V", LocalDate.of(2023, 3, 1), "v2.0")
        );

        List<BrandRelease> result = merger.mergeData(csvData, xmlData);
        
        assertEquals(LocalDate.of(2023, 3, 1), result.get(0).releaseDate());
    }

    @Test
    void mergeData_WithNullXmlDate_UsesCsvDate() {
        List<BrandRelease> csvData = List.of(
            new BrandRelease("Toyota", "RAV4", LocalDate.of(2023, 1, 15), "v1.0")
        );
        
        List<BrandRelease> xmlData = List.of(
            new BrandRelease("Toyota", "RAV4", null, "v1.0")
        );

        List<BrandRelease> result = merger.mergeData(csvData, xmlData);
        
        assertEquals(LocalDate.of(2023, 1, 15), result.get(0).releaseDate());
    }
}
