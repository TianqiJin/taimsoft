package com.taimsoft.dbconnection.model;

import com.taimsoft.dbconnection.model.basemodels.BaseModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by tjin on 2017-07-28.
 */
@Entity
@Table(name = "product")
public class Product extends BaseModel {
    @Column
    private double length;
    @Column
    private double width;
    @Column
    private double height;
    @Column
    private String texture;
    @Column(name = "total_num")
    private double totalNum;
    @Column(name = "unit_price")
    private double unitPrice;

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
}
