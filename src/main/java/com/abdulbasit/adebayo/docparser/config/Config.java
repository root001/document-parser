package com.abdulbasit.adebayo.docparser.config;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class Config {
    private String input_csv;
    private String input_xml;
    private String output_path;
    private String log_level = "info";
    private String output_format = "json"; // json/xml/table
    private Map<String, Object> filters;
 //   private Map<String, String> sort;
 //   private Map<String, String> currencyMapping;
    private Map<String, String> sort;
    private Map<String, String> currencyMapping;

    // Getters and Setters
    public String getInput_csv() {
        return input_csv;
    }

    public void setInput_csv(String input_csv) {
        this.input_csv = input_csv;
    }

    public String getInput_xml() {
        return input_xml;
    }

    public void setInput_xml(String input_xml) {
        this.input_xml = input_xml;
    }

    public String getOutput_path() {
        return output_path;
    }

    public void setOutput_path(String output_path) {
        this.output_path = output_path;
    }

    public String getLog_level() {
        return log_level;
    }

    public void setLog_level(String log_level) {
        this.log_level = log_level;
    }

    public String getOutput_format() {
        return output_format;
    }

    public void setOutput_format(String output_format) {
        this.output_format = output_format;
    }

    public Map<String, Object> getFilters() {
        return filters;
    }

    public void setFilters(Map<String, Object> filters) {
        this.filters = filters;
    }

}
