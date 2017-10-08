package com.taim.model;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.taim.model.basemodels.UserBaseModel;
import javax.persistence.*;

import java.util.List;

/**
 * Created by Tjin on 7/20/2017.
 */
@Entity
@Table(name = "vendor")
@JsonIdentityInfo(scope = Vendor.class,
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Vendor extends UserBaseModel {
    @OneToMany(mappedBy = "vendor", fetch = FetchType.EAGER)
    private List<Transaction> transactionList;

    public Vendor(){}

    public List<Transaction> getTransactionList() {
        return transactionList;
    }

    public void setTransactionList(List<Transaction> transactionList) {
        this.transactionList = transactionList;
    }

}
