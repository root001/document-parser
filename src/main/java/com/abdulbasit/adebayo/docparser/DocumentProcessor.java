package com.abdulbasit.adebayo.docparser;

import com.abdulbasit.adebayo.docparser.config.Config;
import com.abdulbasit.adebayo.docparser.config.ConfigLoader;
import com.abdulbasit.adebayo.docparser.formatter.TableFormatter;
import com.abdulbasit.adebayo.docparser.model.Brand;
import com.abdulbasit.adebayo.docparser.model.Car;
import com.abdulbasit.adebayo.docparser.model.CarBrand;
import com.abdulbasit.adebayo.docparser.parser.*;
import com.abdulbasit.adebayo.docparser.writer.OutputWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Component
public class DocumentProcessor {
    private static final Logger logger = LoggerFactory.getLogger(DocumentProcessor.class);

    private final ConfigLoader configLoader;
    private final CsvParser csvParser;
    private final XmlParser xmlParser;
    private final DataMerger dataMerger;
    private final OutputWriter outputWriter;

    public DocumentProcessor(ConfigLoader configLoader, CsvParser csvParser, 
                           XmlParser xmlParser, DataMerger dataMerger,
                           OutputWriter outputWriter) {
        this.configLoader = configLoader;
        this.csvParser = csvParser;
        this.xmlParser = xmlParser;
        this.dataMerger = dataMerger;
        this.outputWriter = outputWriter;
    }

    public void processDocuments() {
        try {
            logger.info("===== Starting Document Processing =====");
            
            // Load configuration
            logger.info("Loading configuration...");
            Config config = configLoader.loadConfig("config.yaml");
            logger.info("Configuration loaded: inputCSV={}, inputXML={}, outputPath={}", 
                config.getInput_csv(), config.getInput_xml(), config.getOutput_path());

            // Process CSV
            logger.info("Processing CSV file...");
            List<Brand> csvData = csvParser.parse(Paths.get(config.getInput_csv()));
            logger.info("Processed {} records from CSV", csvData.size());

            // Process XML
            logger.info("Processing XML file...");
            List<Car> xmlData = xmlParser.parse(Paths.get(config.getInput_xml()));
            logger.info("Processed {} records from XML", xmlData.size());

            // Merge data
            logger.info("Merging data...");
            List<CarBrand> mergedData = dataMerger.mergeData(csvData, xmlData, null);
            logger.info("Merged {} records", mergedData.size());

            // Write output
            logger.info("Writing output as {}...", config.getOutput_format());
            Path outputPath = Paths.get(config.getOutput_path());
            
            switch (config.getOutput_format()) {
                case "json":
                    outputWriter.writeJson(outputPath.resolve("output.json"), mergedData);
                    break;
                case "xml":
                    outputWriter.writeXml(outputPath.resolve("output.xml"), mergedData);
                    break;
                case "table":
                    logger.info("Table output:\n{}", new TableFormatter().format(mergedData));
                    break;
                default:
                    logger.warn("Unknown output format: {}", config.getOutput_format());
            }
            
            logger.info("===== Processing Completed Successfully =====");
            logger.info("Output generated at: {}", outputPath.toAbsolutePath());
        } catch (Exception e) {
            logger.error("Document processing failed", e);
            throw new RuntimeException("Processing failed", e);
        }
    }
}
