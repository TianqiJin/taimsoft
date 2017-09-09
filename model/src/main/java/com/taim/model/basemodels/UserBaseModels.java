package com.taim.model.basemodels;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * Created by tjin on 2017-07-31.
 */
@MappedSuperclass
public class UserBaseModels extends BaseModel{
    @Column
    private String fullname;
    @Column
    private String phone;
    @Column
    private String email;

    public UserBaseModels(){}

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

    @Override
    public String toString() {
        return "{\"UserBaseModels\":"
                + super.toString()
                + ", \"fullname\":\"" + fullname + "\""
                + ", \"phone\":\"" + phone + "\""
                + ", \"email\":\"" + email + "\""
                + "}";
    }
}
