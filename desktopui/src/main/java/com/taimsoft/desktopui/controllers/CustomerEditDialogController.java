package com.taimsoft.desktopui.controllers;

import com.taim.dto.CustomerDTO;
import com.taim.model.Customer;
import com.taimsoft.desktopui.util.AlertBuilder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Created by jiawei.liu on 10/4/17.
 */
public class CustomerEditDialogController {
    @FXML
    private TextField fullNameField;
    @FXML
    private TextField phoneField;
    @FXML
    private ComboBox classField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField storeCreditField;

    private Stage dialogStage;
    private CustomerDTO customer;
    private String errorMsg;
    private boolean okClicked;

    private ObservableList<Customer.CustomerClass> options = FXCollections.observableArrayList(
            Customer.CustomerClass.values()
    );

    @FXML
    private void initialize(){}

    public CustomerEditDialogController(){
        errorMsg = "";
        okClicked = false;
    }
    public void setDialogStage(Stage dialogStage){
        this.dialogStage = dialogStage;
    }
    public void setTextField(CustomerDTO customer){
        this.customer = customer;
        fullNameField.setText(customer.getFullname());
        phoneField.setText(customer.getPhone());
        classField.setItems(options);
        classField.setValue(customer.getCustomerClass());
        emailField.setText(customer.getEmail());
        storeCreditField.setText(String.valueOf(customer.getStoreCredit()));
    }
    public void handleOk(){
        if(isInputValid()){
            customer.setFullname(fullNameField.getText());
            customer.setPhone(phoneField.getText());
            customer.setCustomerClass((Customer.CustomerClass) classField.getValue());
            customer.setEmail(emailField.getText());
            customer.setStoreCredit(Double.valueOf(storeCreditField.getText()));

            okClicked = true;
            dialogStage.close();
        }
        else{
            new AlertBuilder()
                    .alertHeaderText("Please correct the invalid fields")
                    .alertType(Alert.AlertType.WARNING)
                    .alertTitle("Invalid Customer Fields")
                    .alertContentText(errorMsg)
                    .build()
                    .showAndWait();
        }
    }
    public void handleCancle(){
        dialogStage.close();
    }

    public boolean isOKClicked(){
        return okClicked;
    }

    private boolean isInputValid(){
        if(fullNameField.getText() == null || fullNameField.getText().length() == 0){
            errorMsg += "Full Name should not be empty! \n";
        }
        try{
            Double.parseDouble(storeCreditField.getText());
        }catch (NumberFormatException e){
            errorMsg += "Store Credit must be numbers! \n";
        }
        //TODO: inpsect the validation for emailField

        if(errorMsg.length() == 0){
            return true;
        }
        return false;
    }
}
