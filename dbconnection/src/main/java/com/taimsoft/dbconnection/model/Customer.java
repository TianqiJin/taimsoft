package com.taimsoft.dbconnection.model;

import com.taimsoft.dbconnection.model.basemodels.BaseModel;
import com.taimsoft.dbconnection.model.basemodels.UserBaseModels;

import javax.persistence.*;
import java.util.List;

/**
 * Created by tjin on 2017-07-23.
 */
@Entity
@Table(name = "customer")
public class Customer extends UserBaseModels {
    @Column(name = "store_credit")
    private double storeCredit;
    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    private List<Transaction> transactionList;
    @Column(nullable = false)
    private boolean deleted;

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

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
