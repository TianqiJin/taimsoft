package com.taim.dto;

import com.taim.dto.basedtos.BaseModelDTO;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ProductDTO extends BaseModelDTO{
    private StringProperty sku;
    private DoubleProperty length;
    private DoubleProperty width;
    private DoubleProperty height;
    private StringProperty displayName;
    private StringProperty picUrl;
    private StringProperty texture;
    private DoubleProperty totalNum;
    private DoubleProperty unitPrice;

    public ProductDTO(){
        unitPrice = new SimpleDoubleProperty();
        sku = new SimpleStringProperty();
        length = new SimpleDoubleProperty();
        width = new SimpleDoubleProperty();
        height = new SimpleDoubleProperty();
        displayName = new SimpleStringProperty();
        picUrl = new SimpleStringProperty();
        texture = new SimpleStringProperty();
        totalNum = new SimpleDoubleProperty();
    }

    public String getSku() {
        return sku.get();
    }

    public StringProperty skuProperty() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku.set(sku);
    }

    public double getLength() {
        return length.get();
    }

    public DoubleProperty lengthProperty() {
        return length;
    }

    public void setLength(double length) {
        this.length.set(length);
    }

    public double getWidth() {
        return width.get();
    }

    public DoubleProperty widthProperty() {
        return width;
    }

    public void setWidth(double width) {
        this.width.set(width);
    }

    public double getHeight() {
        return height.get();
    }

    public DoubleProperty heightProperty() {
        return height;
    }

    public void setHeight(double height) {
        this.height.set(height);
    }

    public String getDisplayName() {
        return displayName.get();
    }

    public StringProperty displayNameProperty() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName.set(displayName);
    }

    public String getPicUrl() {
        return picUrl.get();
    }

    public StringProperty picUrlProperty() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl.set(picUrl);
    }

    public String getTexture() {
        return texture.get();
    }

    public StringProperty textureProperty() {
        return texture;
    }

    public void setTexture(String texture) {
        this.texture.set(texture);
    }

    public double getTotalNum() {
        return totalNum.get();
    }

    public DoubleProperty totalNumProperty() {
        return totalNum;
    }

    public void setTotalNum(double totalNum) {
        this.totalNum.set(totalNum);
    }

    public double getUnitPrice() {
        return unitPrice.get();
    }

    public DoubleProperty unitPriceProperty() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice.set(unitPrice);
    }
}
