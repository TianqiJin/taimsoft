package com.taim.model;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.taim.model.basemodels.UserBaseModel;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;


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
    @Column(name = "user_type")
    private UserType userType;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "organization_id")
    private Organization organization;
    @OneToMany(mappedBy = "vendor", fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    private List<Transaction> transactionList;

    public Vendor(){}

    public List<Transaction> getTransactionList() {
        return transactionList;
    }

    public void setTransactionList(List<Transaction> transactionList) {
        this.transactionList = transactionList;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }
}
