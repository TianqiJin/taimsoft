package com.taim.backend.controller.model;

import com.taim.model.Transaction;
import org.joda.time.DateTime;

public class TransactionSearchQueryParam {
    private Transaction.TransactionType type;
    private Transaction.TransactionCategory category;
    private DateTime fromDate;
    private DateTime toDate;
    private Long customerId;
    private Long vendorId;
    private Long staffId;
    private Transaction.PaymentStatus paymentStatus;
    private Transaction.DeliveryStatus deliveryStatus;
    private String transactionNotes;

    public TransactionSearchQueryParam(){}

    public Transaction.TransactionType getType() {
        return type;
    }

    public void setType(Transaction.TransactionType type) {
        this.type = type;
    }

    public Transaction.TransactionCategory getCategory() {
        return category;
    }

    public void setCategory(Transaction.TransactionCategory category) {
        this.category = category;
    }

    public DateTime getFromDate() {
        return fromDate;
    }

    public void setFromDate(DateTime fromDate) {
        this.fromDate = fromDate;
    }

    public DateTime getToDate() {
        return toDate;
    }

    public void setToDate(DateTime toDate) {
        this.toDate = toDate;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getVendorId() {
        return vendorId;
    }

    public void setVendorId(Long vendorId) {
        this.vendorId = vendorId;
    }

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public Transaction.PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(Transaction.PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Transaction.DeliveryStatus getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(Transaction.DeliveryStatus deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public String getTransactionNotes() {
        return transactionNotes;
    }

    public void setTransactionNotes(String transactionNotes) {
        this.transactionNotes = transactionNotes;
    }
}
