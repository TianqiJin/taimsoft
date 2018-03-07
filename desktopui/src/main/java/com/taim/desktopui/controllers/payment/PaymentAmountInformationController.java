package com.taim.desktopui.controllers.payment;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.taim.dto.CustomerDTO;
import com.taim.model.Payment;
import javafx.fxml.FXML;

public class PaymentAmountInformationController {
     enum SearchType{
        BY_NAME("By Name"),
        BY_PHONE("By Phone"),
        BY_EMAIL("By Name");

        private String value;

        SearchType(String vvalue){
            this.value = vvalue;
        }

        public String getValue() {
            return value;
        }

        public static SearchType getSearchType(String value){
            for (SearchType st : SearchType.values()){
                if (value.equalsIgnoreCase(st.name())){
                    return st;
                }
            }
            return null;
        }
    }

    private CustomerDTO customer;

    @FXML
    private JFXComboBox<String> customerComboBox;
    @FXML
    private JFXComboBox<String> customerSearchTypeComboBox;
    @FXML
    private JFXTextField paymentAmountTextField;
    @FXML
    private JFXComboBox<String> paymentTypeComboBox;

    @FXML
    private void initialize(){}

    public CustomerDTO getCustomer() {
        return this.customer;
    }

    public Payment.PaymentType getPaymentType(){
        return Payment.PaymentType.getValue(paymentTypeComboBox.getValue());
    }
}
