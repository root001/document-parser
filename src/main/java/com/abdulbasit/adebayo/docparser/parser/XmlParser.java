package com.abdulbasit.adebayo.docparser.parser;

import com.abdulbasit.adebayo.docparser.model.Car;
import com.abdulbasit.adebayo.docparser.exception.ParseException;
import com.abdulbasit.adebayo.docparser.model.Price;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
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
            logger.debug("Attempting to parse XML document");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(xmlPath.toFile());
            
            NodeList productNodes = document.getElementsByTagName("product");
            for (int i = 0; i < productNodes.getLength(); i++) {
                Element product = (Element) productNodes.item(i);
                
                String model = product.getElementsByTagName("model").item(0).getTextContent();
                String brand = modelLookup.getBrandForModel(model);
                
                // Handle optional price elements
                Price price = null;
                List<Price> priceList = new ArrayList<>();
                
                NodeList priceNodes = product.getElementsByTagName("price");
                if (priceNodes.getLength() > 0) {
                    Element priceElem = (Element) priceNodes.item(0);
                    String currency = priceElem.getAttribute("currency");
                    String amount = priceElem.getTextContent();
                    price = new Price(currency, Double.parseDouble(amount));
                    priceList.add(price);
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
        
        logger.info("Parsed {} product releases from XML", releases.size());
        return releases;
    }
}
