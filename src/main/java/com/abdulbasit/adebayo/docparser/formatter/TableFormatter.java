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
        builder.append(String.format("| %-" + BRAND_WIDTH + "s | %-" + MODEL_WIDTH + "s | %-" + DATE_WIDTH + "s | %-" + CUR_WIDTH + "s | %-" + AMOUNT_WIDTH + "s |\n",
            "Brand", "Model", "Release Date", "Currency", "Amount"));
        
        // Divider
        builder.append(String.format("|-%" + BRAND_WIDTH + "s-|-%" + MODEL_WIDTH + "s-|-%" + DATE_WIDTH + "s-|-%" + CUR_WIDTH + "s-|-%" + AMOUNT_WIDTH + "s-|\n",
            "", "", "", "", ""));
        
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
            
            builder.append(String.format("| %-" + BRAND_WIDTH + "s | %-" + MODEL_WIDTH + "s | %-" + DATE_WIDTH + "s | %-" + CUR_WIDTH + "s | %-" + AMOUNT_WIDTH + "s |\n",
                truncate(release.brandType(), BRAND_WIDTH),
                truncate(release.model(), MODEL_WIDTH),
                DateFormatter.formatForOutput(release.releaseDate()),
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
