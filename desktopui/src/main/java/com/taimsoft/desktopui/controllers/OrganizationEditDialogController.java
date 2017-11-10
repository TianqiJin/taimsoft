package com.taimsoft.desktopui.controllers;

import com.taim.dto.CustomerDTO;
import com.taim.dto.OrganizationDTO;
import com.taimsoft.desktopui.util.AlertBuilder;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.omg.CORBA.ORB;

/**
 * Created by jiawei.liu on 10/31/17.
 */
public class OrganizationEditDialogController {
    @FXML
    private TextField nameField;
    @FXML
    private TextField countryField;
    @FXML
    private TextField cityField;
    @FXML
    private TextField streetNumberField;
    @FXML
    private TextField streetField;
    @FXML
    private TextField postalCodeField;

    private Stage dialogStage;
    private OrganizationDTO organization;
    private String errorMsg;
    private boolean okClicked;


    @FXML
    private void initialize(){}


    public OrganizationEditDialogController(){
        errorMsg = "";
        okClicked = false;
    }

    public void setDialogStage(Stage dialogStage){
        this.dialogStage = dialogStage;
    }
    public void setTextField(OrganizationDTO organization){
        this.organization = organization;
        nameField.setText(organization.getOrgName()==null?"":organization.getOrgName());
        countryField.setText(organization.getCountry()==null?"":organization.getCountry());
        cityField.setText(organization.getCity()==null?"":organization.getCity());
        streetNumberField.setText(organization.getStreetNum()==null?"":organization.getStreetNum());
        streetField.setText(organization.getStreet()==null?"":organization.getStreet());
        postalCodeField.setText(organization.getPostalCode()==null?"":organization.getPostalCode());
    }
    public void handleOk(){
        if(isInputValid()){
            organization.setOrgName(nameField.getText());
            organization.setCountry(countryField.getText());
            organization.setCity(cityField.getText());
            organization.setStreetNum(streetNumberField.getText());
            organization.setStreet(streetField.getText());
            organization.setPostalCode(postalCodeField.getText());

            okClicked = true;
            dialogStage.close();
        }
        else{
            new AlertBuilder()
                    .alertHeaderText("Please correct the invalid fields")
                    .alertType(Alert.AlertType.WARNING)
                    .alertTitle("Invalid Address Info Fields")
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
        if(errorMsg.length() == 0){
            return true;
        }
        return false;
    }
}
