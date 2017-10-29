package com.taim.model;


import com.fasterxml.jackson.annotation.*;
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
    @JoinColumn(name = "customer_class_id")
    private Property.CustomerClass customerClass;
    @Column(name = "user_type")
    private UserType userType;
    @Column(name = "pst_num")
    private String pstNumber;
    @OneToMany(mappedBy = "customer", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Fetch(FetchMode.SUBSELECT)
    private List<Transaction> transactionList;

    public Customer(){}

    public double getStoreCredit() {
        return storeCredit;
    }

    public void setStoreCredit(double storeCredit) {
        this.storeCredit = storeCredit;
    }

    public List<Transaction> getTransactionList() {
        return transactionList;
    }

    public void setTransactionList(List<Transaction> transactionList) {
        this.transactionList = transactionList;
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
