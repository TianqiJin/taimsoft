package com.taimsoft.desktopui.controllers.pdfs;

import com.taim.dto.CustomerDTO;
import com.taim.dto.OrganizationDTO;
import com.taim.dto.StaffDTO;
import com.taim.dto.TransactionDTO;
import com.taim.dto.basedtos.UserBaseModelDTO;
import com.taim.model.Organization;
import com.taim.model.basemodels.UserBaseModel;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.converter.NumberStringConverter;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.taimsoft.desktopui.constants.Constant.FXStyle.FX_ERROR_LABEL_COLOR;

public class BillingInfoController {
    private Pattern pattern;
    private Matcher matcher;
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private UserBaseModelDTO user;
    private UserBaseModelDTO inputUser;

    @FXML
    private TextField nameField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField streetField;
    @FXML
    private TextField cityField;
    @FXML
    private TextField postalCodeField;
    @FXML
    private TextField countryField;
    @FXML
    private TextField streetNumField;
    @FXML
    private CheckBox infoCheckbox;
    @FXML
    private Label nameErrorLabel;
    @FXML
    private Label emailErrorLabel;
    @FXML
    private Label phoneErrorLabel;
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

        emailField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue) { // we only care about loosing focus
                matcher = pattern.matcher(emailField.getText());
                if(matcher.matches()){
                    emailErrorLabel.setText("");
                }else{
                    emailErrorLabel.setText("Email is invalid");
                    emailErrorLabel.setStyle(FX_ERROR_LABEL_COLOR);
                }
            }
        });
        phoneField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue) { // we only care about loosing focus
                if(StringUtils.isNotEmpty(phoneField.getText())){
                    phoneErrorLabel.setText("");
                }else{
                    phoneErrorLabel.setText("Phone number cannot be empty");
                    phoneErrorLabel.setStyle(FX_ERROR_LABEL_COLOR);
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

    public BillingInfoController(){
        pattern = Pattern.compile(EMAIL_PATTERN);
    }

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

    public boolean isReadyForInvoiceGeneration(){
        return StringUtils.isEmpty(emailErrorLabel.getText())
                && StringUtils.isEmpty(nameErrorLabel.getText())
                && StringUtils.isEmpty(phoneErrorLabel.getText());
    }

    public UserBaseModelDTO getUser() {
        return this.user;
    }
}
