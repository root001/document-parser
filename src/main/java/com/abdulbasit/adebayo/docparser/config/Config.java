package com.abdulbasit.adebayo.docparser.config;

import com.fasterxml.jackson.annotation.JsonProperty;

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

    public String getOutputFormat() {
        return outputFormat;
    }

    public void setOutputFormat(String outputFormat) {
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