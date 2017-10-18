package com.taim.model;


import com.taim.model.basemodels.UserBaseModel;

import javax.persistence.*;
import java.util.List;

/**
 * Created by tjin on 2017-07-23.
 */
@Entity
@Table(name = "customer")
public class Customer extends UserBaseModel {
    public enum CustomerClass{
        CLASSA("Class A"),
        CLASSB("Class B"),
        CLASSC("Class C");

        CustomerClass(String vvalue){
            this.value = vvalue;
        }

        private String value;

        public String getValue() {
            return value;
        }
    }
    @Column(name = "store_credit")
    private double storeCredit;
    @Column(name = "customer_class")
    private CustomerClass customerClass;
    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
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

    public CustomerClass getCustomerClass() {
        return customerClass;
    }

    public void setCustomerClass(CustomerClass customerClass) {
        this.customerClass = customerClass;
    }

    @Override
    public String toString() {
        return "{\"Customer\":"
                + super.toString()
                + ", \"storeCredit\":\"" + storeCredit + "\""
                + ", \"customerClass\":\"" + customerClass + "\""
                + ", \"transactionList\":" + transactionList
                + "}";
    }
}
