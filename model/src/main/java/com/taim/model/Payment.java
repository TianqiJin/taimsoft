package com.taim.model;



import com.taim.model.basemodels.BaseModel;
import com.taim.model.basemodels.TransactionBaseModel;

import javax.persistence.*;

/**
 * Created by tjin on 2017-08-01.
 */
@Entity
@Table(name = "payment")
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
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "staff_id")
    private Staff staff;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id")
    private Customer customer;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vendor_id")
    private Vendor vendor;
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

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Vendor getVendor() {
        return vendor;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
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

    public Staff getStaff() {
        return staff;
    }
}
