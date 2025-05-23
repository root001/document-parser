package com.abdulbasit.adebayo.docparser.formatter;

import com.abdulbasit.adebayo.docparser.model.CarBrand;
import com.abdulbasit.adebayo.docparser.util.DateFormatter;
import java.util.List;

public class TableFormatter {
    private static final int BRAND_WIDTH = 10;
    private static final int MODEL_WIDTH = 10;
    private static final int DATE_WIDTH = 25;
    private static final int PRODUCT_WIDTH = 10;
    private static final int CUR_WIDTH = 8;
    private static final int AMOUNT_WIDTH = 10;

    public String format(List<CarBrand> releases) {
        StringBuilder builder = new StringBuilder();
        
        // Header
        builder.append(String.format("| %-" + BRAND_WIDTH + "s | %-" + MODEL_WIDTH + "s | %-" + DATE_WIDTH + "s | %-" + PRODUCT_WIDTH + "s | %-" + CUR_WIDTH + "s | %-" + AMOUNT_WIDTH + "s |\n",
            "Brand", "Model", "Release Date (yyyy,dd,MM)", "Product", "Currency", "Amount"));
        builder.append(String.format("|%s|%s|%s|%s|%s|%s|\n",
            "-".repeat(BRAND_WIDTH + 2),
            "-".repeat(MODEL_WIDTH + 2),
            "-".repeat(DATE_WIDTH + 2),
            "-".repeat(PRODUCT_WIDTH + 2),
            "-".repeat(CUR_WIDTH + 2),
            "-".repeat(AMOUNT_WIDTH + 2)));
        
        if (releases == null || releases.isEmpty()) {
            return builder.toString();
        }

        for (CarBrand release : releases) {
            String currency = release.price() != null ? release.price().currency() : "";
            String amount = release.price() != null ? String.format("%.2f", release.price().amount()) : "";
            
            String productName = release.productName() != null ? release.productName() : "";
            builder.append(String.format("| %-" + BRAND_WIDTH + "s | %-" + MODEL_WIDTH + "s | %-" + DATE_WIDTH + "s | %-" + PRODUCT_WIDTH + "s | %-" + CUR_WIDTH + "s | %-" + AMOUNT_WIDTH + "s |\n",
                truncate(release.brandType(), BRAND_WIDTH),
                truncate(release.model(), MODEL_WIDTH),
                DateFormatter.formatForOutput(release.releaseDate()),
                truncate(productName, PRODUCT_WIDTH),
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
