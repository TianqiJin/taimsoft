package com.taim.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.taim.model.basemodels.BaseModel;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.util.List;

/**
 * Created by tjin on 2017-07-28.
 */
@Entity
@Table(name = "transaction")
//@JsonIdentityInfo(scope = Transaction.class,
//        generator = ObjectIdGenerators.PropertyGenerator.class,
//        property = "id")
public class Transaction extends BaseModel {
    /**
     * Indicate the transaction type
     */
    public enum TransactionType{
        QUOTATION("Quotation"),
        INVOICE("Invoice"),
        STOCK("Stock"),
        RETURN("Return");

        TransactionType(String vvalue){
            this.value = vvalue;
        }

        private String value;

        public String getValue() {
            return value;
        }
    }

    /**
     * Indicate the payment status
     */
    public enum PaymentStatus{
        UNPAID("Unpaid"),
        PARTIALLY_PAID("Partially Paid"),
        PAID("Fully Paid");

        private String value;

        PaymentStatus(String vvalue){
            this.value = vvalue;
        }

        public String getValue() {
            return value;
        }
    }

    @Column(name = "sale_amount")
    private double saleAmount;
    @Column
    private double gst;
    @Column
    private double pst;
    @ManyToOne
    @JoinColumn(name = "staff_id")
    private Staff staff;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    @JsonBackReference
    private Customer customer;
    @ManyToOne
    @JoinColumn(name = "vendor_id")
    private Vendor vendor;
    @Column(name = "transaction_type")
    private TransactionType transactionType;
    @Column(name = "payment_status")
    private PaymentStatus paymentStatus;
    @JoinColumn(name = "delivery_status_id")
    @OneToOne(cascade = CascadeType.ALL)
    private DeliveryStatus deliveryStatus;
    @Column(name = "payment_due_date")
    private DateTime paymentDueDate;
    @Column(name = "delivery_due_date")
    private DateTime deliveryDueDate;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "transaction_id")
    private List<TransactionDetail> transactionDetails;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "transaction_id")
    private List<Payment> payments;
    @Column(name = "ref_id")
    private int refId;
    @Column(name = "is_finalized")
    private boolean finalized;
    @Column
    private String note;

    public Transaction(){}

    public double getSaleAmount() {
        return saleAmount;
    }

    public void setSaleAmount(double saleAmount) {
        this.saleAmount = saleAmount;
    }

    public Staff getStaff() {
        return staff;
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

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public List<TransactionDetail> getTransactionDetails() {
        return transactionDetails;
    }

    public void setTransactionDetails(List<TransactionDetail> transactionDetails) {
        this.transactionDetails = transactionDetails;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public DeliveryStatus getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(DeliveryStatus deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public double getGst() {
        return gst;
    }

    public void setGst(double gst) {
        this.gst = gst;
    }

    public double getPst() {
        return pst;
    }

    public void setPst(double pst) {
        this.pst = pst;
    }

    public int getRefId() {
        return refId;
    }

    public void setRefId(int refId) {
        this.refId = refId;
    }

    public DateTime getPaymentDueDate() {
        return paymentDueDate;
    }

    public void setPaymentDueDate(DateTime paymentDueDate) {
        this.paymentDueDate = paymentDueDate;
    }

    public DateTime getDeliveryDueDate() {
        return deliveryDueDate;
    }

    public void setDeliveryDueDate(DateTime deliveryDueDate) {
        this.deliveryDueDate = deliveryDueDate;
    }

    public boolean isFinalized() {
        return finalized;
    }

    public void setFinalized(boolean finalized) {
        this.finalized = finalized;
    }

//    @Override
//    public String toString() {
//        return "{\"Transaction\":"
//                + super.toString()
//                + ", \"saleAmount\":\"" + saleAmount + "\""
//                + ", \"gst\":\"" + gst + "\""
//                + ", \"pst\":\"" + pst + "\""
//                + ", \"staff\":" + staff
//                + ", \"customer\":" + customer
//                + ", \"vendor\":" + vendor
//                + ", \"transactionType\":\"" + transactionType + "\""
//                + ", \"paymentStatus\":\"" + paymentStatus + "\""
//                + ", \"deliveryStatus\":" + deliveryStatus
//                + ", \"paymentDueDate\":" + paymentDueDate
//                + ", \"deliveryDueDate\":" + deliveryDueDate
//                + ", \"transactionDetails\":" + transactionDetails
//                + ", \"payments\":" + payments
//                + ", \"refId\":\"" + refId + "\""
//                + ", \"finalized\":\"" + finalized + "\""
//                + ", \"note\":\"" + note + "\""
//                + "}";
//    }
}
