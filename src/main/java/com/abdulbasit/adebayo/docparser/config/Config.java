package com.abdulbasit.adebayo.docparser.config;

import java.util.Map;

public class Config {
    private String inputCsv;
    private String inputXml;
    private String outputPath;
    private String logLevel = "info";
    private String outputFormat = "json"; // json/xml/table
    private Map<String, Object> filters;
    private Map<String, String> sort;
    private Map<String, String> currencyMapping;

    // Getters and Setters
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

    public Map<String, Object> getFilters() {
        return filters;
    }

    public void setFilters(Map<String, Object> filters) {
        this.filters = filters;
    }

    public Map<String, String> getSort() {
        return sort;
    }

    public void setSort(Map<String, String> sort) {
        this.sort = sort;
    }

    public Map<String, String> getCurrencyMapping() {
        return currencyMapping;
    }

    public void setCurrencyMapping(Map<String, String> currencyMapping) {
        this.currencyMapping = currencyMapping;
    }
}
