package com.taim.dto.basedtos;

import com.taim.model.basemodels.BaseModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class UserBaseModelDTO extends BaseModelDTO{
    private StringProperty fullname;
    private StringProperty phone;
    private StringProperty email;

    public UserBaseModelDTO(){
        fullname = new SimpleStringProperty();
        phone = new SimpleStringProperty();
        email = new SimpleStringProperty();
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
}
