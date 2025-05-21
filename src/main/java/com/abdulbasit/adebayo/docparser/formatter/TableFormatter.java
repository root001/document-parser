package com.abdulbasit.adebayo.docparser.formatter;

import com.abdulbasit.adebayo.docparser.model.BrandRelease;
import com.abdulbasit.adebayo.docparser.util.DateFormatter;
import java.util.List;

public class TableFormatter {
    private static final int BRAND_WIDTH = 10;
    private static final int MODEL_WIDTH = 10;
    private static final int DATE_WIDTH = 25;
    private static final int VERSION_WIDTH = 8;

    public String format(List<BrandRelease> releases) {
        StringBuilder builder = new StringBuilder();
        
        // Header
        builder.append(String.format("| %-" + BRAND_WIDTH + "s | %-" + MODEL_WIDTH + "s | %-" + DATE_WIDTH + "s | %-" + VERSION_WIDTH + "s |\n",
            "Brand", "Model", "Release Date (yyyy,dd,MM)", "Version"));
        
        // Divider
        builder.append(String.format("|-%" + BRAND_WIDTH + "s-|-%" + MODEL_WIDTH + "s-|-%" + DATE_WIDTH + "s-|-%" + VERSION_WIDTH + "s-|\n",
            "", "", "", ""));
        
        // Rows
        for (BrandRelease release : releases) {
            builder.append(String.format("| %-" + BRAND_WIDTH + "s | %-" + MODEL_WIDTH + "s | %-" + DATE_WIDTH + "s | %-" + VERSION_WIDTH + "s |\n",
                truncate(release.brandName(), BRAND_WIDTH),
                truncate(release.productName(), MODEL_WIDTH),
                DateFormatter.formatForOutput(release.releaseDate()),
                truncate(release.version(), VERSION_WIDTH)));
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
