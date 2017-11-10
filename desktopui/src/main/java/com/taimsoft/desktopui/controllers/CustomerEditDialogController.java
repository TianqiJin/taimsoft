package com.taimsoft.desktopui.controllers;

import com.taim.dto.CustomerDTO;
import com.taim.dto.OrganizationDTO;
import com.taim.dto.PropertyDTO;
import com.taim.model.Customer;
import com.taim.model.Organization;
import com.taim.model.basemodels.UserBaseModel;
import com.taimsoft.desktopui.util.AlertBuilder;
import com.taimsoft.desktopui.util.RestClientFactory;
import com.taimsoft.desktopui.util.TransactionPanelLoader;
import com.taimsoft.desktopui.util.VistaNavigator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.joda.time.DateTime;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by jiawei.liu on 10/4/17.
 */
public class CustomerEditDialogController {
    @FXML
    private TextField fullNameField;
    @FXML
    private TextField phoneField;
    @FXML
    private ComboBox userTypeComboBox;
    @FXML
    private ComboBox classComboBox;
    @FXML
    private TextField emailField;
    @FXML
    private TextField pstNumberField;
    @FXML
    private TextField storeCreditField;

    private Stage dialogStage;
    private CustomerDTO customer;
    private String errorMsg;
    private boolean okClicked;

    private ObservableList<String> options;
    private ObservableList<String> userTypeOpt;

    @FXML
    private void initialize(){}

    @FXML
    public void handleOrganization(){
        boolean newCreated = false;
        OrganizationDTO organization = customer.getOrganization();
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
                    this.customer.setOrganization(organization);
                }
            }
        }
    }



    public CustomerEditDialogController(){
        errorMsg = "";
        okClicked = false;
        options = FXCollections.observableArrayList(VistaNavigator.getGlobalProperty().getCustomerClasses().stream()
                .map(PropertyDTO.CustomerClassDTO::getCustomerClassName)
                .collect(Collectors.toList()));
        userTypeOpt = FXCollections.observableArrayList(
                Arrays.stream(UserBaseModel.UserType.values()).map(u->u.getValue()).collect(Collectors.toList()));
    }

    public void setDialogStage(Stage dialogStage){
        this.dialogStage = dialogStage;
    }

    public void setTextField(CustomerDTO customer){
        if(customer != null){
            this.customer = customer;
            fullNameField.setText(customer.getFullname());
            phoneField.setText(customer.getPhone());
            classComboBox.setItems(options);
            classComboBox.setValue(customer.getCustomerClass().getCustomerClassName());
            userTypeComboBox.setItems(userTypeOpt);
            userTypeComboBox.setValue(customer.getUserType().getValue());
            emailField.setText(customer.getEmail());
            storeCreditField.setText(String.valueOf(customer.getStoreCredit()));
            pstNumberField.setText(String.valueOf(customer.getPstNumber()));
        }

    }

    public void handleOk(){
        if(isInputValid()){
            customer.setFullname(fullNameField.getText());
            customer.setPhone(phoneField.getText());
            Optional<PropertyDTO.CustomerClassDTO>customerClass =
                    VistaNavigator.getGlobalProperty().getCustomerClasses().stream()
                            .filter(customerClassDTO -> customerClassDTO.getCustomerClassName().equals(classComboBox.getValue()))
                            .findAny();
            if(customerClass.isPresent()){
                customer.setCustomerClass(customerClass.get());
            }
            customer.setUserType(UserBaseModel.UserType.getUserType((String)userTypeComboBox.getSelectionModel().getSelectedItem()));
            customer.setPstNumber(pstNumberField.getText());

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
