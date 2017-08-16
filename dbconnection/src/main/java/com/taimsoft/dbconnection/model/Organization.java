package com.taimsoft.dbconnection.model;

import com.taimsoft.dbconnection.model.basemodels.BaseModel;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Tjin on 7/20/2017.
 */
@Entity
@Table(name = "organization")
public class Organization extends BaseModel {
    @Column(name = "name", nullable = false)
    private String orgName;
    @Column(name = "street_num", nullable = false)
    private String streetNum;
    @Column(nullable = false)
    private String street;
    @Column(nullable = false)
    private String city;
    @Column(nullable = false)
    private String country;
    @Column(name = "postal_code", nullable = false)
    private String postalCode;
    @OneToMany(mappedBy = "organization", fetch = FetchType.LAZY)
    private Set<Staff> staffs;

    public Organization(){
        staffs = new HashSet<>();
    }

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

    public Set<Staff> getStaffs() {
        return staffs;
    }

    public void setStaffs(Set<Staff> staffs) {
        this.staffs = staffs;
    }
}
