package com.taim.model;


import com.taim.model.basemodels.UserBaseModels;
import javax.persistence.*;

import java.util.List;

/**
 * Created by Tjin on 7/20/2017.
 */
@Entity
@Table(name = "vendor")
public class Vendor extends UserBaseModels {
    @OneToMany(mappedBy = "vendor", fetch = FetchType.LAZY)
    private List<Transaction> transactionList;

    public Vendor(){}

    public List<Transaction> getTransactionList() {
        return transactionList;
    }

    public void setTransactionList(List<Transaction> transactionList) {
        this.transactionList = transactionList;
    }

    @Override
    public String toString() {
        return "{\"Vendor\":"
                + super.toString()
                + ", \"transactionList\":" + transactionList
                + "}";
    }
}
