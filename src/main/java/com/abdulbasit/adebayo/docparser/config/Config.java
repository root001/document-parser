package com.abdulbasit.adebayo.docparser.config;

public class Config {
    private String inputCsv;
    private String inputXml;
    private String outputPath;
    private String logLevel = "info";
    private String outputFormat = "json"; // json or xml

    public String getInput_csv() {
        return inputCsv;
    }

    public void setInput_csv(String inputCsv) {
        this.inputCsv = inputCsv;
    }

    public String getInput_xml() {
        return inputXml;
    }

    public void setInput_xml(String inputXml) {
        this.inputXml = inputXml;
    }

    public String getOutput_path() {
        return outputPath;
    }

    public void setOutput_path(String outputPath) {
        this.outputPath = outputPath;
    }

    public String getLog_level() {
        return logLevel;
    }

    public void setLog_level(String logLevel) {
        this.logLevel = logLevel;
    }

    public String getOutput_format() {
        return outputFormat;
    }

    public void setOutput_format(String outputFormat) {
        this.outputFormat = outputFormat;
    }
}
