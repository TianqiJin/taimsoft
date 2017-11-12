package com.taim.model;


import com.taim.model.basemodels.BaseModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by Tjin on 8/16/2017.
 */
@Entity
@Table(name = "delivery_status")
public class DeliveryStatus extends BaseModel {
    public enum Status{
        UNDELIVERED("Undelivered"),
        DELIVERING("Delivering"),
        DELIVERED("Delivered");

        private String value;

        Status(String vvalue){
            this.value = vvalue;
        }

        public String getValue() {
            return value;
        }
    }

    @Column(nullable = false)
    private Status status;

    public DeliveryStatus(){}

    public Status getStatus() {
        return status;
    }

    public static Status getStatus(String value){
        for (Status s : Status.values()){
            if (s.name().equalsIgnoreCase(value)){
                return s;
            }
        }
        return null;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

}
