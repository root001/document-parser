package com.abdulbasit.adebayo.docparser.parser;

import com.abdulbasit.adebayo.docparser.model.Car;
import com.abdulbasit.adebayo.docparser.exception.ParseException;
import com.abdulbasit.adebayo.docparser.model.Price;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class XmlParser {
    private static final Logger logger = LoggerFactory.getLogger(XmlParser.class);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    
    private final ModelLookup modelLookup;
    private final boolean caseSensitiveLookup;

    public XmlParser(ModelLookup modelLookup) {
        this(modelLookup, false);
    }

    public XmlParser(ModelLookup modelLookup, boolean caseSensitiveLookup) {
        this.modelLookup = modelLookup;
        this.caseSensitiveLookup = caseSensitiveLookup;
    }

    public List<Car> parse(Path xmlPath) throws ParseException {
        logger.info("Starting XML parsing for file: {}", xmlPath);
        List<Car> releases = new ArrayList<>();
        
        try {
            if (!Files.exists(xmlPath)) {
                logger.error("XML file not found at path: {}", xmlPath);
                throw new ParseException("XML file not found: " + xmlPath);
            }
            
            logger.debug("Attempting to parse XML document (size: {} bytes)", Files.size(xmlPath));
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(xmlPath.toFile());
            
            NodeList carNodes = document.getElementsByTagName("car");
            logger.debug("Found {} car elements in XML", carNodes.getLength());
            
            for (int i = 0; i < carNodes.getLength(); i++) {
                Element car = (Element) carNodes.item(i);
                logger.trace("Processing car element #{}", i+1);
                
                Node modelNode = car.getElementsByTagName("model").item(0);
                if (modelNode == null) {
                    logger.warn("Missing model in car element #{}", i+1);
                    continue;
                }
                String model = modelNode.getTextContent();
                logger.debug("Processing model: {}", model);
                
                String brand = modelLookup.getBrandForModel(model);
                if (brand == null) {
                    logger.warn("No brand mapping found for model: {}", model);
                }
                
                // Handle optional price elements
                Price price = null;
                List<Price> priceList = new ArrayList<>();
                
                // Parse main price
                NodeList priceNodes = car.getElementsByTagName("price");
                if (priceNodes.getLength() > 0) {
                    Element priceElem = (Element) priceNodes.item(0);
                    String currency = priceElem.getAttribute("currency");
                    String amount = priceElem.getTextContent();
                    try {
                        price = new Price(currency, Double.parseDouble(amount));
                        priceList.add(price);
                        logger.debug("Parsed price: {} {}", amount, currency);
                    } catch (NumberFormatException e) {
                        logger.warn("Invalid price format for model {}: {}", model, amount);
                    }
                }

                // Parse price list
                NodeList pricesNodes = car.getElementsByTagName("prices");
                if (pricesNodes.getLength() > 0) {
                    NodeList priceListNodes = ((Element)pricesNodes.item(0)).getElementsByTagName("price");
                    logger.debug("Found {} prices in price list", priceListNodes.getLength());
                    
                    for (int j = 0; j < priceListNodes.getLength(); j++) {
                        Element priceElem = (Element) priceListNodes.item(j);
                        String currency = priceElem.getAttribute("currency");
                        String amount = priceElem.getTextContent();
                        try {
                            Price p = new Price(currency, Double.parseDouble(amount));
                            priceList.add(p);
                            logger.trace("Added price to list: {} {}", amount, currency);
                        } catch (NumberFormatException e) {
                            logger.warn("Invalid price in list for model {}: {}", model, amount);
                        }
                    }
                }
                
                releases.add(new Car(
                    brand,
                    model,
                    price,
                    priceList
                ));
            }
        } catch (Exception e) {
            throw new ParseException("Failed to parse XML: " + e.getMessage(), e);
        }
        
        if (releases.isEmpty()) {
            logger.warn("No valid car records found in XML file");
        } else {
            logger.info("Successfully parsed {} car records from XML", releases.size());
            logger.debug("First parsed record: {}", releases.get(0));
        }
        return releases;
    }
}
