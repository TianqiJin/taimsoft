package com.taim.model.search;

import com.taim.model.Transaction;

import java.util.Date;

public class TransactionSearch {
    public static final String DATE_PATTERN = "yyyyMMdd";
    private Date fromDate;
    private Date toDate;
    private Transaction.TransactionCategory transactionCategory;
    private Transaction.TransactionType transactionType;
    private Transaction.DeliveryStatus deliveryStatus;
    private Transaction.PaymentStatus paymentStatus;
    private String transactionID;
    private long customerID;
    private long vendorID;
    private long staffID;
    private boolean isPaymentOverdue;
    private boolean isDeliveryOverdue;
    private boolean isFinalized;
    private String note;

    public TransactionSearch(){}

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public Transaction.TransactionCategory getTransactionCategory() {
        return transactionCategory;
    }

    public void setTransactionCategory(Transaction.TransactionCategory transactionCategory) {
        this.transactionCategory = transactionCategory;
    }

    public Transaction.TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(Transaction.TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Transaction.DeliveryStatus getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(Transaction.DeliveryStatus deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public Transaction.PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(Transaction.PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public long getCustomerID() {
        return customerID;
    }

    public void setCustomerID(long customerID) {
        this.customerID = customerID;
    }

    public long getVendorID() {
        return vendorID;
    }

    public void setVendorID(long vendorID) {
        this.vendorID = vendorID;
    }

    public long getStaffID() {
        return staffID;
    }

    public void setStaffID(long staffID) {
        this.staffID = staffID;
    }

    public boolean isPaymentOverdue() {
        return isPaymentOverdue;
    }

    public void setPaymentOverdue(boolean paymentOverdue) {
        isPaymentOverdue = paymentOverdue;
    }

    public boolean isDeliveryOverdue() {
        return isDeliveryOverdue;
    }

    public void setDeliveryOverdue(boolean deliveryOverdue) {
        isDeliveryOverdue = deliveryOverdue;
    }

    public boolean isFinalized() {
        return isFinalized;
    }

    public void setFinalized(boolean finalized) {
        isFinalized = finalized;
    }
}
