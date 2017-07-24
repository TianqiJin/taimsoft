package com.taimsoft.dbconnection.model;

import javax.persistence.*;

/**
 * Created by Tjin on 7/15/2017.
 */
@Entity
@Table(name = "staff")
public class Staff extends BaseModel{

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

    @Column(name = "username", nullable = false)
    private String userName;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "fullname", nullable = false)
    private String fullName;
    @ManyToOne
    @JoinColumn(name = "organzation_id", nullable = false)
    private Organization organization;
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
