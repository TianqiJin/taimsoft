package com.taimsoft.desktopui.controllers.edit;

import com.taim.client.StaffClient;
import com.taim.dto.StaffDTO;
import com.taim.model.Staff;
import com.taimsoft.desktopui.uicomponents.FadingStatusMessage;
import com.taimsoft.desktopui.util.RestClientFactory;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static com.taimsoft.desktopui.constants.Constant.FXStyle.FX_ERROR_LABEL_COLOR;

public class StaffEditDialogController {
    private static final Logger logger = LoggerFactory.getLogger(StaffEditDialogController.class);
    private StaffDTO staff;
    private Stage stage;
    private StaffClient client;
    private Executor executor;
    private OrganizationEditDialogController organizationEditDialogController;
    private boolean okClicked;

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField fullnameField;
    @FXML
    private ComboBox<Staff.Position> positionComboBox;
    @FXML
    private TextField emailField;
    @FXML
    private TextField phoneField;
    @FXML
    private Label usernameErrorLabel;
    @FXML
    private Label passwordErrorLabel;
    @FXML
    private Label fullnameErrorLabel;
    @FXML
    private Label emailErrorLabel;
    @FXML
    private Label phoneErrorLabel;
    @FXML
    private AnchorPane root;
    @FXML
    private AnchorPane orgRootPane;
    @FXML
    private SplitPane rootSplitPane;

    public StaffEditDialogController(){
        okClicked = false;
        client = RestClientFactory.getStaffClient();
        executor = Executors.newCachedThreadPool(r -> {
            Thread t = new Thread(r);
            t.setDaemon(true);
            return t;
        });
    }

    @FXML
    private void initialize(){
        positionComboBox.setItems(FXCollections.observableArrayList(Staff.Position.values()));
        initErrorLabel(usernameField, usernameErrorLabel, "Username must not be empty!");
        initErrorLabel(passwordField, passwordErrorLabel, "Password must not be empty!");
        initErrorLabel(fullnameField, fullnameErrorLabel, "Fullname must not be empty!");
        initErrorLabel(emailField, emailErrorLabel, "Email must not be empty!");
        initErrorLabel(phoneField, phoneErrorLabel, "Phone must not be empty!");
        positionComboBox.setOnAction(event -> {
            staff.setPosition(positionComboBox.getSelectionModel().getSelectedItem());
        });
    }

    @FXML
    private void handleConfirmButton(){
        scanBasicInfoFields();
        organizationEditDialogController.scanOrgInfoFields();
        if(isBasicInfoValid() && organizationEditDialogController.isOrgInfoValid()){
            Task<StaffDTO> saveUpdateStaffTask = new Task<StaffDTO>() {
                @Override
                protected StaffDTO call() throws Exception {
                    return client.saveOrUpdate(staff);
                }
            };
            saveUpdateStaffTask.setOnSucceeded(event -> {
                okClicked = true;
                stage.close();
            });
            saveUpdateStaffTask.setOnFailed(event -> {
                logger.error(saveUpdateStaffTask.getException().getMessage(), saveUpdateStaffTask.getException());
                FadingStatusMessage.flash("FAILED TO SAVE STAFF", root);
            });

            executor.execute(saveUpdateStaffTask);
        }
    }

    @FXML
    private void handleCancelButton(){
        stage.close();
    }

    private void initGeneralInfoTextFields(){
        usernameField.textProperty().bindBidirectional(this.staff.userNameProperty());
        passwordField.textProperty().bindBidirectional(this.staff.passwordProperty());
        fullnameField.textProperty().bindBidirectional(this.staff.fullnameProperty());
        emailField.textProperty().bindBidirectional(this.staff.emailProperty());
        phoneField.textProperty().bindBidirectional(this.staff.phoneProperty());
        if(this.staff.getPosition() != null){
            positionComboBox.setValue(this.staff.getPosition());
        }else{
            positionComboBox.getSelectionModel().selectFirst();
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

    private void scanBasicInfoFields(){
        usernameField.requestFocus();
        passwordField.requestFocus();
        fullnameField.requestFocus();
        emailField.requestFocus();
        phoneField.requestFocus();
    }

    private boolean isBasicInfoValid(){
        return StringUtils.isEmpty(usernameErrorLabel.getText())
                && StringUtils.isEmpty(passwordErrorLabel.getText())
                && StringUtils.isEmpty(fullnameErrorLabel.getText())
                && StringUtils.isEmpty(emailErrorLabel.getText())
                && StringUtils.isEmpty(phoneErrorLabel.getText());
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public StaffDTO getStaff() {
        return staff;
    }

    public void initData(StaffDTO staff){
        this.staff = staff;
        initGeneralInfoTextFields();
        try {
            FXMLLoader fXMLLoader = new FXMLLoader();
            AnchorPane orgRoot = fXMLLoader.load(this.getClass().getResource("/fxml/edit/OrganizationEditDialog.fxml").openStream());
            orgRoot.prefHeightProperty().bind(orgRootPane.heightProperty());
            orgRoot.prefWidthProperty().bind(orgRootPane.widthProperty());
            organizationEditDialogController = fXMLLoader.getController();
            organizationEditDialogController.initData(staff.getOrganization());
            orgRootPane.getChildren().setAll(orgRoot);
        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
        }
    }
}
