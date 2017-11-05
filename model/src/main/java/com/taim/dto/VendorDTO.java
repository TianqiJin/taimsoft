package com.taim.dto;

import com.taim.dto.basedtos.UserBaseModelDTO;
import com.taim.model.basemodels.UserBaseModel;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;


import java.util.ArrayList;
import java.util.List;

public class VendorDTO extends UserBaseModelDTO {
    private BooleanProperty checked;
    private List<TransactionDTO> transactionList;
    private ObjectProperty<UserBaseModel.UserType> userType;
    public VendorDTO(){
        transactionList = new ArrayList<>();
        checked = new SimpleBooleanProperty();
        userType = new SimpleObjectProperty<>();
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

    public UserBaseModel.UserType getUserType() {
        return userType.get();
    }

    public ObjectProperty<UserBaseModel.UserType> userTypeProperty() {
        return userType;
    }

    public void setUserType(UserBaseModel.UserType userType) {
        this.userType.set(userType);
    }
}
