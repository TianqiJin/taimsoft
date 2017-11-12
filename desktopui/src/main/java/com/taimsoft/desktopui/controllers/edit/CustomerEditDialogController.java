package com.taimsoft.desktopui.controllers.edit;

import com.taim.dto.CustomerDTO;
import com.taim.dto.OrganizationDTO;
import com.taim.dto.PropertyDTO;
import com.taim.dto.StaffDTO;
import com.taim.model.Customer;
import com.taim.model.Organization;
import com.taim.model.Property;
import com.taim.model.basemodels.UserBaseModel;
import com.taimsoft.desktopui.uicomponents.FadingStatusMessage;
import com.taimsoft.desktopui.util.AlertBuilder;
import com.taimsoft.desktopui.util.RestClientFactory;
import com.taimsoft.desktopui.util.TransactionPanelLoader;
import com.taimsoft.desktopui.util.VistaNavigator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static com.taimsoft.desktopui.constants.Constant.FXStyle.FX_ERROR_LABEL_COLOR;

/**
 * Created by jiawei.liu on 10/4/17.
 */
public class CustomerEditDialogController {
    private static final Logger logger = LoggerFactory.getLogger(CustomerEditDialogController.class);
    private Stage dialogStage;
    private CustomerDTO customer;
    private boolean okClicked;
    private OrganizationEditDialogController organizationEditDialogController;
    private ObservableList<String> options;
    private ObservableList<String> userTypeOpt;
    private Executor executor;

    @FXML
    private TextField fullNameField;
    @FXML
    private TextField phoneField;
    @FXML
    private ComboBox<String> userTypeComboBox;
    @FXML
    private ComboBox<String> classComboBox;
    @FXML
    private TextField emailField;
    @FXML
    private TextField pstNumberField;
    @FXML
    private TextField storeCreditField;
    @FXML
    private Label fullnameErrorLabel;
    @FXML
    private Label phoneErrorLabel;
    @FXML
    private Label emailErrorLabel;
    @FXML
    private Label storeCreditErrorLabel;
    @FXML
    private AnchorPane root;
    @FXML
    private AnchorPane orgRootPane;
    @FXML
    private SplitPane rootSplitPane;

    @FXML
    private void initialize(){
        initErrorLabel(fullNameField, fullnameErrorLabel, "Full name must not be empty!");
        initErrorLabel(phoneField, phoneErrorLabel, "Phone must not be empty!");
        initErrorLabel(emailField, emailErrorLabel, "Email name must not be empty!");
        storeCreditField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue) { // we only care about loosing focus
                try{
                    Double.parseDouble(storeCreditField.getText());
                }catch (NumberFormatException e){
                    storeCreditField.setText("Store credit must be a number!");
                    storeCreditErrorLabel.setStyle(FX_ERROR_LABEL_COLOR);
                }
            }
        });
        classComboBox.setItems(options);
        classComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            for(PropertyDTO.CustomerClassDTO customerClass : VistaNavigator.getGlobalProperty().getCustomerClasses()){
                if(customerClass.getCustomerClassName().equals(newValue)){
                    customer.setCustomerClass(customerClass);
                }
            }
        });

        userTypeComboBox.setItems(userTypeOpt);
        userTypeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            customer.setUserType(UserBaseModel.UserType.getUserType(newValue));
        });
    }

    public CustomerEditDialogController(){
        okClicked = false;
        options = FXCollections.observableArrayList(VistaNavigator.getGlobalProperty().getCustomerClasses().stream()
                .map(PropertyDTO.CustomerClassDTO::getCustomerClassName)
                .collect(Collectors.toList()));
        userTypeOpt = FXCollections.observableArrayList(
                Arrays.stream(UserBaseModel.UserType.values()).map(UserBaseModel.UserType::getValue).collect(Collectors.toList()));
        executor = Executors.newCachedThreadPool(r -> {
            Thread t = new Thread(r);
            t.setDaemon(true);
            return t;
        });
    }

    public void setDialogStage(Stage dialogStage){
        this.dialogStage = dialogStage;
    }

    public void initData(CustomerDTO customer){
        this.customer = customer;
        initGeneralInfoTextFields();
        try {
            FXMLLoader fXMLLoader = new FXMLLoader();
            AnchorPane orgRoot = fXMLLoader.load(this.getClass().getResource("/fxml/edit/OrganizationEditDialog.fxml").openStream());
            orgRoot.prefHeightProperty().bind(orgRootPane.heightProperty());
            orgRoot.prefWidthProperty().bind(orgRootPane.widthProperty());
            organizationEditDialogController = fXMLLoader.getController();
            organizationEditDialogController.initData(customer.getOrganization());
            orgRootPane.getChildren().setAll(orgRoot);
        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    public void handleOk(){
        scanBasicInfoFields();
        organizationEditDialogController.scanOrgInfoFields();
        if(isInputValid() && organizationEditDialogController.isOrgInfoValid()){
            Task<CustomerDTO> saveUpdateCustomerTask = new Task<CustomerDTO>() {
                @Override
                protected CustomerDTO call() throws Exception {
                    return RestClientFactory.getCustomerClient().saveOrUpdate(customer);
                }
            };
            saveUpdateCustomerTask.setOnSucceeded(event -> {
                okClicked = true;
                dialogStage.close();
            });
            saveUpdateCustomerTask.setOnFailed(event -> {
                logger.error(saveUpdateCustomerTask.getException().getMessage(), saveUpdateCustomerTask.getException());
                FadingStatusMessage.flash("FAILED TO SAVE CUSTOMER", root);
            });

            executor.execute(saveUpdateCustomerTask);
        }
    }
    public void handleCancel(){
        dialogStage.close();
    }

    public boolean isOKClicked(){
        return okClicked;
    }

    public CustomerDTO getCustomer() {
        return customer;
    }

    private boolean isInputValid(){
       return StringUtils.isEmpty(fullnameErrorLabel.getText())
               && StringUtils.isEmpty(emailErrorLabel.getText())
               && StringUtils.isEmpty(phoneErrorLabel.getText())
               && StringUtils.isEmpty(storeCreditErrorLabel.getText());

    }

    private void initGeneralInfoTextFields(){
        fullNameField.textProperty().bindBidirectional(this.customer.fullnameProperty());
        phoneField.textProperty().bindBidirectional(this.customer.phoneProperty());
        emailField.textProperty().bindBidirectional(this.customer.emailProperty());
        storeCreditField.textProperty().bindBidirectional(this.customer.storeCreditProperty(), new NumberStringConverter());
        pstNumberField.textProperty().bindBidirectional(this.customer.pstNumberProperty());
        if (customer.getCustomerClass()!=null) {
            classComboBox.setValue(customer.getCustomerClass().getCustomerClassName());
        }else {
            classComboBox.getSelectionModel().selectFirst();
        }
        if(customer.getUserType()!=null){
            userTypeComboBox.setValue(customer.getUserType().getValue());
        }else{
            userTypeComboBox.getSelectionModel().selectFirst();
        }

    }

    private void scanBasicInfoFields(){
        fullNameField.requestFocus();
        emailField.requestFocus();
        phoneField.requestFocus();
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
