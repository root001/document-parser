package com.abdulbasit.adebayo.docparser.config;

public class Config {
    private String inputCsv;
    private String inputXml;
    private String outputPath;
    private String logLevel = "info";

    public String getInputCsv() {
        return inputCsv;
    }

    public String getInputXml() {
        return inputXml;
    }

    public String getOutputPath() {
        return outputPath;
    }

    public String getLogLevel() {
        return logLevel;
    }
}
