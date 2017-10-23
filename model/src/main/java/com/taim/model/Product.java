package com.taim.model;




import com.taim.model.basemodels.BaseModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by tjin on 2017-07-28.
 */
@Entity
@Table(name = "product")
public class Product extends BaseModel {
    @Column(unique = true)
    private String sku;
    @Column
    private double length;
    @Column
    private double width;
    @Column
    private double height;
    @Column(name = "display_name")
    private String displayName;
    @Column(name = "picture_url")
    private String picUrl;
    @Column
    private String texture;
    @Column(name = "total_num")
    private double totalNum;
    @Column(name = "total_num_virtual")
    private double virtualTotalNum;
    @Column(name = "unit_price")
    private Double unitPrice;
    @Column(name = "piece_per_box")
    private int piecePerBox;

    public Product(){}

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(double totalNum) {
        this.totalNum = totalNum;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getTexture() {
        return texture;
    }

    public void setTexture(String texture) {
        this.texture = texture;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public double getVirtualTotalNum() {
        return virtualTotalNum;
    }

    public void setVirtualTotalNum(double virtualTotalNum) {
        this.virtualTotalNum = virtualTotalNum;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getPiecePerBox() {
        return piecePerBox;
    }

    public void setPiecePerBox(int piecePerBox) {
        this.piecePerBox = piecePerBox;
    }
}
