package com.taim.dto;

import com.taim.dto.basedtos.UserBaseModelDTO;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.ArrayList;
import java.util.List;

public class VendorDTO extends UserBaseModelDTO {
    private BooleanProperty isChecked;
    private List<TransactionDTO> transactionList;

    public VendorDTO(){
        transactionList = new ArrayList<>();
        isChecked = new SimpleBooleanProperty();
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

    public List<TransactionDTO> getTransactionList() {
        return transactionList;
    }

    public void setTransactionList(List<TransactionDTO> transactionList) {
        this.transactionList = transactionList;
    }
}
