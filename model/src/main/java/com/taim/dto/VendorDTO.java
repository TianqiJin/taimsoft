package com.taim.dto;

import com.taim.dto.basedtos.UserBaseModelDTO;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.hibernate.usertype.UserType;

import java.util.ArrayList;
import java.util.List;

public class VendorDTO extends UserBaseModelDTO {
    private BooleanProperty checked;
    private List<TransactionDTO> transactionList;
    private ObjectProperty<UserType> userType;
    private ObjectProperty<OrganizationDTO> organization;
    public VendorDTO(){
        transactionList = new ArrayList<>();
        checked = new SimpleBooleanProperty();
        userType = new SimpleObjectProperty<>();
        organization = new SimpleObjectProperty<>();
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

    public UserType getUserType() {
        return userType.get();
    }

    public ObjectProperty<UserType> userTypeProperty() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType.set(userType);
    }

    public OrganizationDTO getOrganization() {
        return organization.get();
    }

    public ObjectProperty<OrganizationDTO> organizationProperty() {
        return organization;
    }

    public void setOrganization(OrganizationDTO organization) {
        this.organization.set(organization);
    }
}
