package com.taimsoft.desktopui.controllers;

import com.taim.dto.OrganizationDTO;
import com.taim.dto.VendorDTO;
import com.taim.model.basemodels.UserBaseModel;
import com.taimsoft.desktopui.util.AlertBuilder;
import com.taimsoft.desktopui.util.RestClientFactory;
import com.taimsoft.desktopui.util.TransactionPanelLoader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.joda.time.DateTime;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Created by jiawei.liu on 10/31/17.
 */
public class VendorEditDialogController {
    @FXML
    private TextField fullNameField;
    @FXML
    private TextField phoneField;
    @FXML
    private ComboBox userTypeComboBox;
    @FXML
    private TextField emailField;


    private Stage dialogStage;
    private VendorDTO vendor;
    private String errorMsg;
    private boolean okClicked;

    private ObservableList<String> userTypeOpt;

    @FXML
    private void initialize(){}

    @FXML
    public void handleOrganization(){
        boolean newCreated = false;
        OrganizationDTO organization = vendor.getOrganization();
        if (organization == null){
            organization = new OrganizationDTO();
            organization.setDateCreated(DateTime.now());
            newCreated = true;
        }

        boolean okClicked = TransactionPanelLoader.showOrganizationEditor(organization);
        if(okClicked){
            boolean flag = true;
            try{
                organization.setDateModified(DateTime.now());
                if (newCreated){
                    RestClientFactory.getOrganizationClient().add(organization);
                }else{
                    RestClientFactory.getOrganizationClient().update(organization);
                }new AlertBuilder()
                        .alertHeaderText("Address Record Created successfully!")
                        .alertType(Alert.AlertType.INFORMATION)
                        .alertTitle("Address Info")
                        .alertContentText(organization.getOrgName())
                        .build()
                        .showAndWait();

            }catch(Exception e){
                e.printStackTrace();
                flag = false;
                new AlertBuilder()
                        .alertType(Alert.AlertType.ERROR)
                        .alertTitle("Error")
                        .alertHeaderText("Add Address Info Error")
                        .alertContentText("Unable To Add/Edit Address Info" + organization.getOrgName() )
                        .build()
                        .showAndWait();
            }finally{
                if(flag){
                    this.vendor.setOrganization(organization);
                }
            }
        }
    }



    public VendorEditDialogController(){
        errorMsg = "";
        okClicked = false;
        userTypeOpt = FXCollections.observableArrayList(
                Arrays.stream(UserBaseModel.UserType.values()).map(u->u.getValue()).collect(Collectors.toList()));
    }

    public void setDialogStage(Stage dialogStage){
        this.dialogStage = dialogStage;
    }
    public void setTextField(VendorDTO vendor){
        this.vendor = vendor;
        fullNameField.setText(vendor.getFullname());
        phoneField.setText(vendor.getPhone());

        userTypeComboBox.setItems(userTypeOpt);
        if (vendor.getUserType()!=null){
            userTypeComboBox.setValue(vendor.getUserType().getValue());
        }else{
            userTypeComboBox.setValue(userTypeOpt.get(0));
        }
        emailField.setText(vendor.getEmail());
    }
    public void handleOk(){
        if(isInputValid()){
            vendor.setFullname(fullNameField.getText());
            vendor.setPhone(phoneField.getText());
            vendor.setUserType(UserBaseModel.UserType.getUserType((String)userTypeComboBox.getSelectionModel().getSelectedItem()));
            vendor.setEmail(emailField.getText());

            okClicked = true;
            dialogStage.close();
        }
        else{
            new AlertBuilder()
                    .alertHeaderText("Please correct the invalid fields")
                    .alertType(Alert.AlertType.WARNING)
                    .alertTitle("Invalid Vendor Fields")
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

        if(errorMsg.length() == 0){
            return true;
        }
        return false;
    }
}
