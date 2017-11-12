package com.taim.model;



import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.taim.model.basemodels.UserBaseModel;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Tjin on 7/15/2017.
 */
@Entity
@Table(name = "staff")
@JsonIdentityInfo(scope = Staff.class,
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Staff extends UserBaseModel {

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
    @Column
    private Position position;

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

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

}
