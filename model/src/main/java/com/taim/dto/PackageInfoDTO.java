package com.taim.dto;

import com.taim.dto.basedtos.BaseModelDTO;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class PackageInfoDTO extends BaseModelDTO {
    private IntegerProperty box;
    private IntegerProperty pieces;

    public PackageInfoDTO(){
        box = new SimpleIntegerProperty();
        pieces = new SimpleIntegerProperty();
    }

    public int getBox() {
        return box.get();
    }

    public IntegerProperty boxProperty() {
        return box;
    }

    public void setBox(int box) {
        this.box.set(box);
    }

    public int getPieces() {
        return pieces.get();
    }

    public IntegerProperty piecesProperty() {
        return pieces;
    }

    public void setPieces(int pieces) {
        this.pieces.set(pieces);
    }
}
