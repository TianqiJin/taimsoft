package com.taim.model;

import com.taim.model.basemodels.BaseModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "payment_method")
public class PaymentMethod extends BaseModel{
    @Column
    private String method;

    public PaymentMethod(){}

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
