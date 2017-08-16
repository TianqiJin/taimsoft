package com.taimsoft.dbconnection.model;

import com.taimsoft.dbconnection.model.basemodels.BaseModel;

import javax.persistence.*;

/**
 * Created by tjin on 2017-08-01.
 */
@Entity
@Table(name = "payment")
public class Payment extends BaseModel {
    public enum PaymentType{
        CASH("Cash"),
        CREDIT("Credit"),
        DEBIT("Debit"),
        CHEQUE("Cheque");

        private String value;
        PaymentType(String vvalue){
            this.value = vvalue;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
    @Column(name = "payment_amount")
    private double paymentAmount;
    @Column(name = "payment_type")
    private PaymentType paymentType;
    @ManyToOne
    @JoinColumn(name = "transaction_id")
    private Transaction transaction;

    public Payment(){}

    public double getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }
}
