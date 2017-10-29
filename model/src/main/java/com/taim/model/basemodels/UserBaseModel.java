package com.taim.model.basemodels;

import com.taim.model.Organization;

import javax.persistence.*;

/**
 * Created by tjin on 2017-07-31.
 */
@MappedSuperclass
public class UserBaseModel extends BaseModel{
    public enum UserType{
        INDIVIDUAL("Individual"),
        COMPANY("Company");

        private String value;

        UserType(String vvalue){
            this.value = vvalue;
        }
    }

    @Column
    private String fullname;
    @Column
    private String phone;
    @Column
    private String email;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "organization_id")
    private Organization organization;

    public UserBaseModel(){}

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }
}
