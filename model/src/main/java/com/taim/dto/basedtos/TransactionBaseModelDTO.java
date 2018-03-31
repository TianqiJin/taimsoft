package com.taim.dto.basedtos;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TransactionBaseModelDTO extends BaseModelDTO {
    private StringProperty presentId;

    public TransactionBaseModelDTO(){
        presentId = new SimpleStringProperty();
    }

    public String getPresentId() {
        return presentId.get();
    }

    public StringProperty presentIdProperty() {
        return presentId;
    }

    public void setPresentId(String presentId) {
        this.presentId.set(presentId);
    }
}
