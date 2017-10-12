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
    @Column(name = "user_type")
    private UserType userType;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "organization_id")
    private Organization organization;
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

    public CustomerClass getCustomerClass() {
        return customerClass;
    }

    public void setCustomerClass(CustomerClass customerClass) {
        this.customerClass = customerClass;
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
