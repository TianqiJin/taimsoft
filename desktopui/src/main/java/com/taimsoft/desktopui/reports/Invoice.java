package com.taimsoft.desktopui.reports;


import com.taim.dto.*;
import com.taim.dto.basedtos.UserBaseModelDTO;
import org.joda.time.DateTime;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by jiawei.liu on 2/4/16.
 */
public class Invoice {
    protected int id;
    protected UserBaseModelDTO customer;
    protected TransactionDTO transaction;
    protected double total;
    protected DateTime invoiceDate;
    protected UserBaseModelDTO staff;
    protected PropertyDTO property;

    public Invoice(TransactionDTO transaction, UserBaseModelDTO customer, UserBaseModelDTO staff, PropertyDTO property){
        this.transaction = transaction;
        this.id = transaction.getId();
        this.invoiceDate = transaction.getDateCreated();
        this.total = transaction.getSaleAmount();
        this.customer = customer;
        this.staff = staff;
        this.property = property;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TransactionDTO getTransaction() {
        return transaction;
    }

    public void setTransaction(TransactionDTO transaction) {
        this.transaction = transaction;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public DateTime getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(DateTime invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public UserBaseModelDTO getCustomer() {
        return customer;
    }

    public void setCustomer(UserBaseModelDTO customer) {
        this.customer = customer;
    }

    public UserBaseModelDTO getStaff() {
        return staff;
    }

    public void setStaff(UserBaseModelDTO staff) {
        this.staff = staff;
    }

    public PropertyDTO getProperty() {
        return property;
    }

    public void setProperty(PropertyDTO property) {
        this.property = property;
    }
}
