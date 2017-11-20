package com.taim.model;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.taim.model.basemodels.UserBaseModel;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;


import javax.persistence.*;
import java.util.List;

/**
 * Created by tjin on 2017-07-23.
 */
@Entity
@Table(name = "customer")
@JsonIdentityInfo(
        scope = Customer.class,
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Customer extends UserBaseModel {
    @Column(name = "store_credit")
    private double storeCredit;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_class_id", nullable = false)
    private Property.CustomerClass customerClass;
    @Column(name = "user_type", nullable = false)
    private UserType userType;
    @Column(name = "pst_num")
    private String pstNumber;

    public Customer(){}

    public double getStoreCredit() {
        return storeCredit;
    }

    public void setStoreCredit(double storeCredit) {
        this.storeCredit = storeCredit;
    }

    public Property.CustomerClass getCustomerClass() {
        return customerClass;
    }

    public void setCustomerClass(Property.CustomerClass customerClass) {
        this.customerClass = customerClass;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public String getPstNumber() {
        return pstNumber;
    }

    public void setPstNumber(String pstNumber) {
        this.pstNumber = pstNumber;
    }
}
