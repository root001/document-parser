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
        List<Car> releases = new ArrayList<>();
        
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(xmlPath.toFile());
            List<Price> priceList = new ArrayList<>();
            NodeList productNodes = document.getElementsByTagName("car");
            for (int i = 0; i < productNodes.getLength(); i++) {
                Element product = (Element) productNodes.item(i);
                String type = product.getElementsByTagName("type").item(0).getTextContent();
                String model = product.getElementsByTagName("model").item(0).getTextContent();
                String currency = product.getElementsByTagName("price").item(0).getTextContent();
                String amount = product.getElementsByTagName("price").item(1).getTextContent();
                NodeList pricesListNode = product.getElementsByTagName("prices");
                for (int j = 0; j < pricesListNode.getLength(); j++) {
                    Element pricesList = (Element) pricesListNode.item(i);
                    String currencyPrice = pricesList.getElementsByTagName("price").item(0).getTextContent();
                    String amountPrice = pricesList.getElementsByTagName("price").item(1).getTextContent();
                    priceList.add(new Price(currencyPrice, Double.parseDouble(amountPrice)));
                }
                
                releases.add(new Car(
                    type,
                    model,
                    new Price(currency, Double.parseDouble(amount)),
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
