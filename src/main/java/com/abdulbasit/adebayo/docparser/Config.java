package com.abdulbasit.adebayo.docparser;

import lombok.Data;

@Data
public class Config {
    private String inputCsv;
    private String inputXml;
    private String outputPath;
    private String logLevel = "info";
}
