package com.abdulbasit.adebayo.docparser.config;

public class Config {
    private String inputCsv;
    private String inputXml;
    private String outputPath;
    private String logLevel = "info";

    public String getInputCsv() {
        return inputCsv;
    }

    public void setInputCsv(String inputCsv) {
        this.inputCsv = inputCsv;
    }

    public String getInputXml() {
        return inputXml;
    }

    public void setInputXml(String inputXml) {
        this.inputXml = inputXml;
    }

    public String getOutputPath() {
        return outputPath;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

    public String getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(String logLevel) {
        this.logLevel = logLevel;
    }
}
