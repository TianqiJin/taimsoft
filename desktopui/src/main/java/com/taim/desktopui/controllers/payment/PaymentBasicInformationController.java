package com.taim.desktopui.controllers.payment;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.taim.desktopui.util.DateUtils;
import com.taim.desktopui.util.VistaNavigator;
import com.taim.dto.PaymentDTO;
import com.taim.dto.StaffDTO;
import com.taim.model.Staff;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.util.StringConverter;
import org.fxmisc.easybind.EasyBind;
import org.joda.time.DateTime;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class PaymentBasicInformationController implements Initializable{
    private static final String DATE_PATTERN = "yyyy-MM-dd";
    private static final String TYPE = "PAYMENT";
    private ObjectProperty<PaymentDTO> payment;

    @FXML
    private JFXTextField paymentIDTextField;
    @FXML
    private Label typeLabel;
    @FXML
    private JFXDatePicker paymentDatePicker;
    @FXML
    private Label staffNameLabel;

    public PaymentBasicInformationController(){
        payment = new SimpleObjectProperty<>();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        typeLabel.setText(TYPE);
        staffNameLabel.textProperty().bindBidirectional(VistaNavigator.getGlobalStaff().userNameProperty());
        paymentIDTextField.textProperty().bindBidirectional(EasyBind.monadic(payment).selectProperty(PaymentDTO::presentIdProperty));
        paymentDatePicker.setOnAction(event ->{
            this.payment.get().setDateCreated(DateUtils.toDateTime(paymentDatePicker.getValue()));
        });
        paymentDatePicker.setPromptText(DATE_PATTERN.toLowerCase());
        paymentDatePicker.setConverter(new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }
            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        });
    }

    public void setPayment(PaymentDTO paymentDTO){
        this.payment.set(paymentDTO);
        paymentDatePicker.setValue(DateUtils.toLocalDate(this.payment.get().getDateCreated()));
    }

    public PaymentDTO getPayment() {
        return payment.get();
    }

    public PaymentDTO paymentProperty() {
        return payment.get();
    }
}
