package com.taim.dto;

import com.taim.dto.basedtos.UserBaseModelDTO;
import com.taim.model.Customer;
import com.taim.model.Property;
import com.taim.model.basemodels.UserBaseModel;
import javafx.beans.property.*;


import java.util.ArrayList;
import java.util.List;

public class CustomerDTO extends UserBaseModelDTO{
    private DoubleProperty storeCredit;
    private BooleanProperty checked;
    private StringProperty pstNumber;
    private ObjectProperty<PropertyDTO.CustomerClassDTO> customerClass;
    private ObjectProperty<UserBaseModel.UserType> userType;

    public CustomerDTO(){
        storeCredit = new SimpleDoubleProperty();
        checked = new SimpleBooleanProperty();
        customerClass = new SimpleObjectProperty<>();
        userType = new SimpleObjectProperty<>();
        pstNumber = new SimpleStringProperty();
    }

    public double getStoreCredit() {
        return storeCredit.get();
    }

    public DoubleProperty storeCreditProperty() {
        return storeCredit;
    }

    public void setStoreCredit(double storeCredit) {
        this.storeCredit.set(storeCredit);
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

    public PropertyDTO.CustomerClassDTO getCustomerClass() {
        return customerClass.get();
    }

    public ObjectProperty<PropertyDTO.CustomerClassDTO> customerClassProperty() {
        return customerClass;
    }

    public void setCustomerClass(PropertyDTO.CustomerClassDTO customerClass) {
        this.customerClass.set(customerClass);
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

    public String getPstNumber() {
        return pstNumber.get();
    }

    public StringProperty pstNumberProperty() {
        return pstNumber;
    }

    public void setPstNumber(String pstNumber) {
        this.pstNumber.set(pstNumber);
    }
}
