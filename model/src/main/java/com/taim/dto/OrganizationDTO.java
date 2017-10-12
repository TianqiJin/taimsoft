package com.taim.dto;

import com.taim.dto.basedtos.BaseModelDTO;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OrganizationDTO extends BaseModelDTO {
    private StringProperty orgName;
    private StringProperty streetNum;
    private StringProperty street;
    private StringProperty city;
    private StringProperty country;
    private StringProperty postalCode;

    public OrganizationDTO(){
        orgName = new SimpleStringProperty();
        streetNum = new SimpleStringProperty();
        street = new SimpleStringProperty();
        city = new SimpleStringProperty();
        country = new SimpleStringProperty();
        postalCode = new SimpleStringProperty();
    }

    public String getOrgName() {
        return orgName.get();
    }

    public StringProperty orgNameProperty() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName.set(orgName);
    }

    public String getStreetNum() {
        return streetNum.get();
    }

    public StringProperty streetNumProperty() {
        return streetNum;
    }

    public void setStreetNum(String streetNum) {
        this.streetNum.set(streetNum);
    }

    public String getStreet() {
        return street.get();
    }

    public StringProperty streetProperty() {
        return street;
    }

    public void setStreet(String street) {
        this.street.set(street);
    }

    public String getCity() {
        return city.get();
    }

    public StringProperty cityProperty() {
        return city;
    }

    public void setCity(String city) {
        this.city.set(city);
    }

    public String getCountry() {
        return country.get();
    }

    public StringProperty countryProperty() {
        return country;
    }

    public void setCountry(String country) {
        this.country.set(country);
    }

    public String getPostalCode() {
        return postalCode.get();
    }

    public StringProperty postalCodeProperty() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode.set(postalCode);
    }
}
