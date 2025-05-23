package com.abdulbasit.adebayo.docparser.util;

public class commonsUtils {

    /**
     * Converts a Price object to its string representation.
     * Handles the price with currency format from XML.
     */
    private String convertPriceToString(Object price) {
        if (price == null) {
            return "0.00";
        }

        // Based on your XML structure: <price currency="USD">25000.00</price>
        // The Price object likely has amount and currency fields
        // Using toString() for now, but you might want to format it as "25000.00 USD"
        String priceStr = price.toString();

        // If you want to format it differently, you can access Price fields:
        // if (price instanceof Price priceObj) {
        //     return String.format("%.2f %s", priceObj.getAmount(), priceObj.getCurrency());
        // }

        return priceStr;
    }
}
