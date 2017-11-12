package com.taimsoft.desktopui.controllers.edit;

import com.taim.dto.CustomerDTO;
import com.taim.dto.OrganizationDTO;
import com.taimsoft.desktopui.util.AlertBuilder;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;
import org.omg.CORBA.ORB;

import static com.taimsoft.desktopui.constants.Constant.FXStyle.FX_ERROR_LABEL_COLOR;

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
    @FXML
    private Label nameErrorLabel;

    private OrganizationDTO organization;

    @FXML
    private void initialize(){
        initErrorLabel(nameField, nameErrorLabel, "Organization name must not be empty!");
    }

    public boolean isOrgInfoValid(){
        return StringUtils.isEmpty(nameErrorLabel.getText());
    }

    public void initData(OrganizationDTO organization){
        this.organization = organization;
        initOrgInfoTextFields();
    }

    public void scanOrgInfoFields(){
        nameField.requestFocus();
        streetNumberField.requestFocus();
        streetField.requestFocus();
        cityField.requestFocus();
        countryField.requestFocus();
        postalCodeField.requestFocus();
    }

    private void initOrgInfoTextFields(){
        if(this.organization != null){
            nameField.textProperty().bindBidirectional(this.organization.orgNameProperty());
            streetNumberField.textProperty().bindBidirectional(this.organization.streetNumProperty());
            streetField.textProperty().bindBidirectional(this.organization.streetProperty());
            cityField.textProperty().bindBidirectional(this.organization.cityProperty());
            countryField.textProperty().bindBidirectional(this.organization.countryProperty());
            postalCodeField.textProperty().bindBidirectional(this.organization.postalCodeProperty());
        }
    }

    private void initErrorLabel(TextField field, Label errorLabel, String errorMsg){
        field.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue) { // we only care about loosing focus
                if(StringUtils.isNotEmpty(field.getText())){
                    errorLabel.setText("");
                }else{
                    errorLabel.setText(errorMsg);
                    errorLabel.setStyle(FX_ERROR_LABEL_COLOR);
                }
            }
        });
    }
}
