package com.taim.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.taim.model.basemodels.BaseModel;

import javax.persistence.*;

@Entity
@Table(name = "payment_detail")
@JsonIdentityInfo(scope = PaymentDetail.class,
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class PaymentDetail extends BaseModel {
    @Column
    private double amount;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "transaction_id", nullable = false)
    private Transaction transaction;
    @Column
    private boolean deposit;
    @Column
    private String note;

    public PaymentDetail(){}

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public boolean isDeposit() {
        return deposit;
    }

    public void setDeposit(boolean deposit) {
        this.deposit = deposit;
    }
}
