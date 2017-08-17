package com.taimsoft.model;

import com.taimsoft.model.basemodels.UserBaseModels;

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
    @Column(nullable = false)
    private boolean deleted;

    public Vendor(){}

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