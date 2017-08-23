package com.taim.taimsoft.model;



import com.taim.taimsoft.model.basemodels.UserBaseModels;

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
    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;
    @OneToMany(mappedBy = "staff", fetch = FetchType.LAZY)
    private List<Transaction> transactionList;
    @Column(name = "deleted", nullable = false)
    private boolean deleted;

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

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }
}
