package com.taim.model;



import com.taim.model.basemodels.BaseModel;
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
    @Column
    private int discount;
    @Column(name = "note")
    private String note;
    @OneToOne
    @JoinColumn(name = "package_info_id")
    private PackageInfo packageInfo;

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

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public PackageInfo getPackageInfo() {
        return packageInfo;
    }

    public void setPackageInfo(PackageInfo packageInfo) {
        this.packageInfo = packageInfo;
    }
}
