package com.taim.model;



import com.taim.model.basemodels.UserBaseModels;
import javax.persistence.*;
import java.util.List;

/**
 * Created by Tjin on 7/15/2017.
 */
@Entity
@Table(name = "staff")
public class Staff extends UserBaseModels {

    public enum Position{
        SALES("Sales"), MANAGER("Manager"), DISTRIBUTOR("Distributor");

        private String pos;

        Position(String pos){
            this.pos = pos;
        }

        public static Position getPosition(String pos){
            for (Position ppos : Position.values()){
                if (ppos.name().equalsIgnoreCase(pos)){
                    return ppos;
                }
            }
            return null;
        }
    }

    @Column(nullable = false)
    private String userName;
    @Column(nullable = false)
    private String password;
    @Column(name = "picture_url")
    private String picUrl;
    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;
    @OneToMany(mappedBy = "staff", fetch = FetchType.LAZY)
    private List<Transaction> transactionList;

    public Staff(){}

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public List<Transaction> getTransactionList() {
        return transactionList;
    }

    public void setTransactionList(List<Transaction> transactionList) {
        this.transactionList = transactionList;
    }

    @Override
    public String toString() {
        return "{\"Staff\":"
                + super.toString()
                + ", \"userName\":\"" + userName + "\""
                + ", \"password\":\"" + password + "\""
                + ", \"picUrl\":\"" + picUrl + "\""
                + ", \"organization\":" + organization
                + ", \"transactionList\":" + transactionList
                + "}";
    }
}
