package com.taim.desktopui.controllers.invoices;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import com.taim.dto.CustomerDTO;
import com.taim.dto.OrganizationDTO;
import com.taim.dto.StaffDTO;
import com.taim.dto.basedtos.UserBaseModelDTO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.taim.desktopui.constants.Constant.FXStyle.FX_ERROR_LABEL_COLOR;

public class BillingInfoController {
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private UserBaseModelDTO user;
    private UserBaseModelDTO inputUser;

    @FXML
    private JFXTextField nameField;
    @FXML
    private JFXTextField phoneField;
    @FXML
    private JFXTextField emailField;
    @FXML
    private JFXTextField streetField;
    @FXML
    private JFXTextField cityField;
    @FXML
    private JFXTextField postalCodeField;
    @FXML
    private JFXTextField countryField;
    @FXML
    private JFXTextField streetNumField;
    @FXML
    private JFXCheckBox infoCheckbox;
    @FXML
    private Label nameErrorLabel;
    @FXML
    private Label streetNumErrorLabel;
    @FXML
    private Label streetErrorLabel;
    @FXML
    private Label cityErrorLabel;
    @FXML
    private Label countryErrorLabel;
    @FXML
    private Label postalCodeErrorLabel;

    @FXML
    private Label nameLabel;

    @FXML
    private void initialize(){
        nameField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue) { // we only care about loosing focus
                if(StringUtils.isNotEmpty(nameField.getText())){
                    nameErrorLabel.setText("");
                }else{
                    nameErrorLabel.setText("Billing name cannot be empty");
                    nameErrorLabel.setStyle(FX_ERROR_LABEL_COLOR);
                }
            }
        });
        streetNumField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue) { // we only care about loosing focus
                if(StringUtils.isNotEmpty(streetNumField.getText())){
                    streetNumErrorLabel.setText("");
                }else{
                    streetNumErrorLabel.setText("Street Number cannot be empty");
                    streetNumErrorLabel.setStyle(FX_ERROR_LABEL_COLOR);
                }
            }
        });
        streetField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue) { // we only care about loosing focus
                if(StringUtils.isNotEmpty(streetField.getText())){
                    streetErrorLabel.setText("");
                }else{
                    streetErrorLabel.setText("Street cannot be empty");
                    streetErrorLabel.setStyle(FX_ERROR_LABEL_COLOR);
                }
            }
        });
        cityField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue) { // we only care about loosing focus
                if(StringUtils.isNotEmpty(cityField.getText())){
                    cityErrorLabel.setText("");
                }else{
                    cityErrorLabel.setText("City cannot be empty");
                    cityErrorLabel.setStyle(FX_ERROR_LABEL_COLOR);
                }
            }
        });
        countryField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue) { // we only care about loosing focus
                if(StringUtils.isNotEmpty(countryField.getText())){
                    countryErrorLabel.setText("");
                }else{
                    countryErrorLabel.setText("Country cannot be empty");
                    countryErrorLabel.setStyle(FX_ERROR_LABEL_COLOR);
                }
            }
        });
        postalCodeField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue) { // we only care about loosing focus
                if(StringUtils.isNotEmpty(postalCodeField.getText())){
                    postalCodeErrorLabel.setText("");
                }else{
                    postalCodeErrorLabel.setText("Postal Code cannot be empty");
                    postalCodeErrorLabel.setStyle(FX_ERROR_LABEL_COLOR);
                }
            }
        });

        infoCheckbox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue){
                if(user != null){
                    if(inputUser.getOrganization() != null){
                        user.getOrganization().setPostalCode(inputUser.getOrganization().getPostalCode());
                        user.getOrganization().setStreetNum(inputUser.getOrganization().getStreetNum());
                        user.getOrganization().setStreet(inputUser.getOrganization().getStreet());
                        user.getOrganization().setCity(inputUser.getOrganization().getCity());
                        user.getOrganization().setCountry(inputUser.getOrganization().getCountry());
                        user.getOrganization().setOrgName(inputUser.getOrganization().getOrgName());
                    }
                    user.setEmail(inputUser.getEmail());
                    user.setFullname(inputUser.getFullname());
                    user.setPhone(inputUser.getPhone());
                }
            }else{
                nameField.clear();
                emailField.clear();
                phoneField.clear();
                streetField.clear();
                streetNumField.clear();
                cityField.clear();
                postalCodeField.clear();
                countryField.clear();
            }
        });
    }

    public BillingInfoController(){}

    public void initData(UserBaseModelDTO inputUser){
        this.user = new UserBaseModelDTO();
        this.inputUser = inputUser;
        OrganizationDTO organization = new OrganizationDTO();
        this.user.setOrganization(organization);
        bindBillingInfoFields();

        if(this.inputUser instanceof StaffDTO){
            infoCheckbox.setText("Use Staff Info");
            nameLabel.setText("From (*)");
        }else if(this.inputUser instanceof CustomerDTO){
            infoCheckbox.setText("Use Customer Info");
            nameLabel.setText("To (*)");
        }
    }

    private void bindBillingInfoFields(){
        nameField.textProperty().bindBidirectional(this.user.fullnameProperty());
        emailField.textProperty().bindBidirectional(this.user.emailProperty());
        phoneField.textProperty().bindBidirectional(this.user.phoneProperty());
        streetField.textProperty().bindBidirectional(this.user.getOrganization().streetProperty());
        streetNumField.textProperty().bindBidirectional(this.user.getOrganization().streetNumProperty());
        cityField.textProperty().bindBidirectional(this.user.getOrganization().cityProperty());
        postalCodeField.textProperty().bindBidirectional(this.user.getOrganization().postalCodeProperty());
        countryField.textProperty().bindBidirectional(this.user.getOrganization().countryProperty());
    }

    public void scanRequiredFields(){
        nameField.requestFocus();
        streetNumField.requestFocus();
        streetField.requestFocus();
        cityField.requestFocus();
        countryField.requestFocus();
        postalCodeField.requestFocus();
    }

    public boolean isReadyForInvoiceGeneration(){
        return StringUtils.isEmpty(streetNumErrorLabel.getText())
                && StringUtils.isEmpty(nameErrorLabel.getText())
                && StringUtils.isEmpty(streetErrorLabel.getText())
                && StringUtils.isEmpty(cityErrorLabel.getText())
                && StringUtils.isEmpty(countryErrorLabel.getText())
                && StringUtils.isEmpty(postalCodeErrorLabel.getText());
    }

    public UserBaseModelDTO getUser() {
        return this.user;
    }
}
