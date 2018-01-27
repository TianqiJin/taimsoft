package com.taim.dto;

import com.taim.dto.basedtos.BaseModelDTO;
import com.taim.model.PackageInfo;
import javafx.beans.property.*;

public class TransactionDetailDTO extends BaseModelDTO{
    private ObjectProperty<ProductDTO> product;
    private DoubleProperty quantity;
    private DoubleProperty deliveredQuantity;
    private DoubleProperty saleAmount;
    private IntegerProperty discount;
    private StringProperty note;
    private ObjectProperty<PackageInfoDTO> packageInfo;

    public TransactionDetailDTO(){
        product = new SimpleObjectProperty<ProductDTO>();
        quantity = new SimpleDoubleProperty();
        deliveredQuantity = new SimpleDoubleProperty();
        saleAmount = new SimpleDoubleProperty();
        discount = new SimpleIntegerProperty();
        note = new SimpleStringProperty();
        packageInfo = new SimpleObjectProperty<PackageInfoDTO>();
    }

    public ProductDTO getProduct() {
        return product.get();
    }

    public ObjectProperty<ProductDTO> productProperty() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product.set(product);
    }

    public double getQuantity() {
        return quantity.get();
    }

    public DoubleProperty quantityProperty() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity.set(quantity);
    }

    public double getSaleAmount() {
        return saleAmount.get();
    }

    public DoubleProperty saleAmountProperty() {
        return saleAmount;
    }

    public void setSaleAmount(double saleAmount) {
        this.saleAmount.set(saleAmount);
    }

    public int getDiscount() {
        return discount.get();
    }

    public IntegerProperty discountProperty() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount.set(discount);
    }

    public String getNote() {
        return note.get();
    }

    public StringProperty noteProperty() {
        return note;
    }

    public void setNote(String note) {
        this.note.set(note);
    }

    public PackageInfoDTO getPackageInfo() {
        return packageInfo.get();
    }

    public ObjectProperty<PackageInfoDTO> packageInfoProperty() {
        return packageInfo;
    }

    public void setPackageInfo(PackageInfoDTO packageInfo) {
        this.packageInfo.set(packageInfo);
    }

    public double getDeliveredQuantity() {
        return deliveredQuantity.get();
    }

    public DoubleProperty deliveredQuantityProperty() {
        return deliveredQuantity;
    }

    public void setDeliveredQuantity(double deliveredQuantity) {
        this.deliveredQuantity.set(deliveredQuantity);
    }
}
