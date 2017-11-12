package com.taim.model;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.taim.model.basemodels.BaseModel;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Tjin on 7/20/2017.
 */
@Entity
@Table(name = "organization")
@JsonIdentityInfo(scope = Organization.class,
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Organization extends BaseModel {
    @Column(name = "name", nullable = false)
    private String orgName;
    @Column(name = "street_num")
    private String streetNum;
    @Column()
    private String street;
    @Column()
    private String city;
    @Column()
    private String country;
    @Column(name = "postal_code")
    private String postalCode;

    public Organization(){}

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getStreetNum() {
        return streetNum;
    }

    public void setStreetNum(String streetNum) {
        this.streetNum = streetNum;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

}
