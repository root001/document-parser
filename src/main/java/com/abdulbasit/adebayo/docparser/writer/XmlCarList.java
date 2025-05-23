package com.abdulbasit.adebayo.docparser.writer;

import com.abdulbasit.adebayo.docparser.model.Car;
import com.abdulbasit.adebayo.docparser.model.CarBrand;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.List;

@JacksonXmlRootElement(localName = "Cars")
public class XmlCarList {
    @JacksonXmlProperty(localName = "Car")
    @JacksonXmlElementWrapper(useWrapping = false)
    private final List<CarBrand> cars;

    public XmlCarList(List<CarBrand> cars) {
        this.cars = cars;
    }

    public List<CarBrand> getCars() {
        return cars;
    }
}
