package com.taim.dto.basedtos;

import com.taim.dto.OrganizationDTO;
import com.taim.model.basemodels.BaseModel;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class UserBaseModelDTO extends BaseModelDTO{
    private StringProperty fullname;
    private StringProperty phone;
    private StringProperty email;
    private ObjectProperty<OrganizationDTO> organization;

    public UserBaseModelDTO(){
        fullname = new SimpleStringProperty();
        phone = new SimpleStringProperty();
        email = new SimpleStringProperty();
        organization = new SimpleObjectProperty<>();
    }

    public String getFullname() {
        return fullname.get();
    }

    public StringProperty fullnameProperty() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname.set(fullname);
    }

    public String getPhone() {
        return phone.get();
    }

    public StringProperty phoneProperty() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone.set(phone);
    }

    public String getEmail() {
        return email.get();
    }

    public StringProperty emailProperty() {
        return email;
    }

    public void setEmail(String email) {
        this.email.set(email);
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
