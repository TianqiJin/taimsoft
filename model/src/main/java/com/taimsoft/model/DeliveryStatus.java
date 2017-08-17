package com.taimsoft.model;

import com.taimsoft.model.basemodels.BaseModel;

import javax.persistence.*;

/**
 * Created by Tjin on 8/16/2017.
 */
@Entity
@Table(name = "delivery_status")
public class DeliveryStatus extends BaseModel{
    enum Status{
        UNDELIVERED("Undelivered"),
        DELIVERYING("Delivering"),
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

    public void setStatus(Status status) {
        this.status = status;
    }

}
