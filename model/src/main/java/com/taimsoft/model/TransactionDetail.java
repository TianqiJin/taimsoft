package com.taimsoft.model;


import com.taimsoft.model.basemodels.BaseModel;

import javax.persistence.*;

/**
 * Created by tjin on 2017-07-28.
 */
@Entity
@Table(name = "transaction_detail")
public class TransactionDetail extends BaseModel {
    @ManyToOne
    @JoinColumn(name = "transaction_id")
    private Transaction transaction;
    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;
    @Column
    private double quantity;
    @Column(name = "sale_amount")
    private double saleAmount;
    @Column(name = "note")
    private String note;

    public TransactionDetail(){}

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getSaleAmount() {
        return saleAmount;
    }

    public void setSaleAmount(double saleAmount) {
        this.saleAmount = saleAmount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
