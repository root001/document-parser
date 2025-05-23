package com.abdulbasit.adebayo.docparser.formatter;

import com.abdulbasit.adebayo.docparser.model.CarBrand;
import com.abdulbasit.adebayo.docparser.util.DateFormatter;
import java.util.List;

public class TableFormatter {
    private static final int BRAND_WIDTH = 10;
    private static final int MODEL_WIDTH = 10;
    private static final int DATE_WIDTH = 25;
    private static final int PRODUCT_WIDTH = 8;
    private static final int CUR_WIDTH = 8;
    private static final int AMOUNT_WIDTH = 8;

    public String format(List<CarBrand> releases) {
        StringBuilder builder = new StringBuilder();
        
        // Header
        builder.append("| Brand      | Model      | Release Date   | Currency | Amount     |\n");
        builder.append("|------------|------------|----------------|----------|------------|\n");
        
        if (releases == null || releases.isEmpty()) {
            return builder.toString();
        }

        for (CarBrand release : releases) {
            String currency = release.price() != null ? release.price().currency() : "";
            String amount = release.price() != null ? String.format("%.2f", release.price().amount()) : "";
            
            builder.append(String.format("| %-10s | %-10s | %-15s | %-8s | %-10s |\n",
                truncate(release.brandType(), 10),
                truncate(release.model(), 10),
                DateFormatter.formatForOutput(release.releaseDate()),
                truncate(currency, 8),
                truncate(amount, 10)));
        }
        
        // Rows
        if (releases == null || releases.isEmpty()) {
            return builder.toString();
        }

        for (CarBrand release : releases) {
            String currency = "";
            String amount = "";
            if (release.price() != null) {
                currency = release.price().currency();
                amount = String.valueOf(release.price().amount());
            }
            
            builder.append(String.format("| %-" + BRAND_WIDTH + "s | %-" + MODEL_WIDTH + "s | %-" + DATE_WIDTH + "s | %-" + PRODUCT_WIDTH + "s | %-" + CUR_WIDTH + "s | %-" + AMOUNT_WIDTH + "s |\n",
                truncate(release.brandType(), BRAND_WIDTH),
                truncate(release.model(), MODEL_WIDTH),
                DateFormatter.formatForOutput(release.releaseDate()),
                truncate(release.productName(), PRODUCT_WIDTH),
                truncate(currency, CUR_WIDTH),
                truncate(amount, AMOUNT_WIDTH)));
        }
        
        return builder.toString();
    }

    private String truncate(String value, int maxLength) {
        if (value == null) {
            return "";
        }
        return value.length() > maxLength ? value.substring(0, maxLength - 1) + "…" : value;
    }
}
