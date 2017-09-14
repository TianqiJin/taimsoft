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
    @Column(name = "store_credit")
    private double storeCredit;
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

    @Override
    public String toString() {
        return "{\"Customer\":"
                + super.toString()
                + ", \"storeCredit\":\"" + storeCredit + "\""
                + ", \"transactionList\":" + transactionList
                + "}";
    }
}
