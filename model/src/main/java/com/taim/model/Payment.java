package com.taim.model;



import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.taim.model.basemodels.BaseModel;
import com.taim.model.basemodels.TransactionBaseModel;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

/**
 * Created by tjin on 2017-08-01.
 */
@Entity
@Table(name = "payment")
@JsonIdentityInfo(scope = Payment.class,
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Payment extends TransactionBaseModel {
    public enum PaymentType{
        CUSTOMER_PAYMENT("Customer Payment"),
        VENDOR_PAYMENT("Vendor Payment");

        private String value;

        PaymentType(String vvalue){ this.value = vvalue; }

        public static PaymentType getType(String value){
            for (PaymentType pt : PaymentType.values()){
                if (value.equalsIgnoreCase(pt.getValue())){
                    return pt;
                }
            }
            return null;
        }

        public String getValue() {
            return value;
        }
    }

    @Column(name = "payment_amount", nullable = false)
    private double paymentAmount;
    @Column(name = "payment_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "payment_method_id")
    private PaymentMethod paymentMethod;
    @Column(name = "staff_id")
    private Long staffID;
    @Column(name = "user_id")
    private Long userID;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Fetch(FetchMode.SUBSELECT)
    @JoinColumn(name = "payment_id")
    private List<PaymentDetail> paymentDetails;
    @Column
    private String note;

    public Payment(){}

    public double getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Long getStaffID() {
        return staffID;
    }

    public void setStaffID(Long staffID) {
        this.staffID = staffID;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public List<PaymentDetail> getPaymentDetails() {
        return paymentDetails;
    }

    public void setPaymentDetails(List<PaymentDetail> paymentDetails) {
        this.paymentDetails = paymentDetails;
    }
}
