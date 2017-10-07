package com.taim.dto;

import com.taim.dto.basedtos.UserBaseModelDTO;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.ArrayList;
import java.util.List;

public class VendorDTO extends UserBaseModelDTO {
    private BooleanProperty checked;
    private List<TransactionDTO> transactionList;

    public VendorDTO(){
        transactionList = new ArrayList<>();
        checked = new SimpleBooleanProperty();
    }

    public boolean isChecked() {
        return checked.get();
    }

    public BooleanProperty checkedProperty() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked.set(checked);
    }

    public List<TransactionDTO> getTransactionList() {
        return transactionList;
    }

    public void setTransactionList(List<TransactionDTO> transactionList) {
        this.transactionList = transactionList;
    }
}
