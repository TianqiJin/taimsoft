package com.taim.desktopui.controllers.payment;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.taim.dto.CustomerDTO;
import com.taim.model.Payment;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PaymentAmountInformationController implements Initializable{
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<String> allSearchTypes = Stream.of(SearchType.values()).map(SearchType::name).collect(Collectors.toList());
        customerSearchTypeComboBox.setItems(FXCollections.observableList(allSearchTypes));

    }

    public CustomerDTO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDTO customer) {
        this.customer = customer;
    }

    public Payment.PaymentType getPaymentType(){
        return Payment.PaymentType.getValue(paymentTypeComboBox.getValue());
    }
}
