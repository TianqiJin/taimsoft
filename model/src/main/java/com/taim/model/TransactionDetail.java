package com.taim.model;



import com.taim.model.basemodels.BaseModel;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

/**
 * Created by tjin on 2017-07-28.
 */
@Entity
@Table(name = "transaction_detail")
public class TransactionDetail extends BaseModel {
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    @Column(nullable = false)
    private double quantity;
    @Column(name = "delivered_quantity", columnDefinition = "double default 0")
    private double deliveredQuantity = 0;
    @Column(name = "to_delivery_quantity", columnDefinition = "double default 0")
    private double toDeliveryQuantity = 0;
    @Column(name = "sale_amount", nullable = false)
    private double saleAmount;
    @Column
    private int discount;
    @Column(name = "note")
    private String note;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "package_info_id")
    private PackageInfo packageInfo;

    public TransactionDetail(){}

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

    public double getDeliveredQuantity() {
        return deliveredQuantity;
    }

    public void setDeliveredQuantity(double deliveredQuantity) {
        this.deliveredQuantity = deliveredQuantity;
    }

    public double getToDeliveryQuantity() {
        return toDeliveryQuantity;
    }

    public void setToDeliveryQuantity(double toDeliveryQuantity) {
        this.toDeliveryQuantity = toDeliveryQuantity;
    }
}
