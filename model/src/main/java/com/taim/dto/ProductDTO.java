package com.taim.dto;

import com.taim.dto.basedtos.BaseModelDTO;
import javafx.beans.property.*;

public class ProductDTO extends BaseModelDTO{
    private StringProperty sku;
    private DoubleProperty length;
    private DoubleProperty width;
    private DoubleProperty height;
    private StringProperty displayName;
    private StringProperty picUrl;
    private StringProperty texture;
    private DoubleProperty totalNum;
    private DoubleProperty virtualTotalNum;
    private DoubleProperty unitPrice;
    private BooleanProperty isChecked;

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
        virtualTotalNum = new SimpleDoubleProperty();
        isChecked = new SimpleBooleanProperty();
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

    public boolean isIsChecked() {
        return isChecked.get();
    }

    public BooleanProperty isCheckedProperty() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked.set(isChecked);
    }

    public double getVirtualTotalNum() {
        return virtualTotalNum.get();
    }

    public DoubleProperty virtualTotalNumProperty() {
        return virtualTotalNum;
    }

    public void setVirtualTotalNum(double virtualTotalNum) {
        this.virtualTotalNum.set(virtualTotalNum);
    }
}
