package com.taimsoft.desktopui.controllers.pdfs;

import com.taim.dto.CustomerDTO;
import com.taim.dto.StaffDTO;
import com.taim.dto.TransactionDTO;
import com.taim.dto.basedtos.UserBaseModelDTO;
import com.taim.model.basemodels.UserBaseModel;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
                if(this.user != null){
                    nameField.setText(this.user.getFullname());
                    emailField.setText(this.user.getEmail());
                    phoneField.setText(this.user.getPhone());
                    if(this.user.getOrganization() != null){
                        streetNumField.setText(this.user.getOrganization().getStreetNum());
                        streetField.setText(this.user.getOrganization().getStreet());
                        cityField.setText(this.user.getOrganization().getCity());
                        postalCodeField.setText(this.user.getOrganization().getPostalCode());
                        countryField.setText(this.user.getOrganization().getCountry());
                    }

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

    public void initData(UserBaseModelDTO user){
        this.user = user;
        if(this.user instanceof StaffDTO){
            infoCheckbox.setText("Use Staff Info");
            nameLabel.setText("From");
        }else if(this.user instanceof CustomerDTO){
            infoCheckbox.setText("Use Customer Info");
            nameLabel.setText("To");
        }
    }

    public UserBaseModelDTO getUser() {
        return user;
    }
}
