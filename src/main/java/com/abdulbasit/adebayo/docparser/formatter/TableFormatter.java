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
        builder.append(String.format("| %-" + BRAND_WIDTH + "s | %-" + MODEL_WIDTH + "s | %-" + DATE_WIDTH + "s | %-" + MODEL_WIDTH + "s | %-" + CUR_WIDTH + "s | %-" + AMOUNT_WIDTH + "s |\n",
            "Brand", "Model", "Release Date (yyyy,dd,MM)", "Model", "Currency", "Amount"));
        
        // Divider
        builder.append(String.format("|-%" + BRAND_WIDTH + "s-|-%" + MODEL_WIDTH + "s-|-%" + DATE_WIDTH + "s-|-%" + MODEL_WIDTH + "s | %-" + CUR_WIDTH + "s | %-" + AMOUNT_WIDTH + "s |\n",
            "", "", "", "", "", ""));
        
        // Rows
        for (CarBrand release : releases) {
            builder.append(String.format("| %-" + BRAND_WIDTH + "s | %-" + PRODUCT_WIDTH + "s | %-" + DATE_WIDTH + "s | %-" + MODEL_WIDTH + "s | %-" + CUR_WIDTH + "s | %-" + AMOUNT_WIDTH + "s |\n",
                truncate(release.brandType(), BRAND_WIDTH),
                truncate(release.productName(), PRODUCT_WIDTH),
                DateFormatter.formatForOutput(release.releaseDate()),
                truncate(release.model(), MODEL_WIDTH),
                truncate(release.price().currency(), CUR_WIDTH),
                    truncate(String.valueOf(release.price().amount()), AMOUNT_WIDTH)));
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
