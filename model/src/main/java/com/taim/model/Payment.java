package com.taim.model;



import com.taim.model.basemodels.BaseModel;

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
        CHEQUE("Cheque"),
        VOID("Void"),
        STORE_CREDIT("Store Credit");

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
        public static PaymentType getValue(String value){
            for (PaymentType p: PaymentType.values()){
                if (p.name().equalsIgnoreCase(value)){
                    return p;
                }
            }
            return null;
        }
    }
    @Column(name = "payment_amount", nullable = false)
    private double paymentAmount;
    @Column(name = "payment_type", nullable = false)
    private PaymentType paymentType;
    @Column(name = "is_deposit", nullable = false)
    private boolean deposit;

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

    public boolean isDeposit() {
        return deposit;
    }

    public void setDeposit(boolean deposit) {
        this.deposit = deposit;
    }

}
