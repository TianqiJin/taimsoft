package com.taim.model;


import com.taim.model.basemodels.BaseModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by Tjin on 8/19/2017.
 */
@Entity
@Table(name = "package_info")
public class PackageInfo extends BaseModel {
    @Column
    private int box;
    @Column
    private int pieces;

    public PackageInfo(){}

    public int getBox() {
        return box;
    }

    public void setBox(int box) {
        this.box = box;
    }

    public int getPieces() {
        return pieces;
    }

    public void setPieces(int pieces) {
        this.pieces = pieces;
    }
}
