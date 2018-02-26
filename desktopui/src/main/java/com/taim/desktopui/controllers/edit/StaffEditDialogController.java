package com.taim.desktopui.controllers.edit;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.taim.client.StaffClient;
import com.taim.dto.StaffDTO;
import com.taim.model.Staff;
import com.taim.desktopui.util.AlertBuilder;
import com.taim.desktopui.util.RestClientFactory;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static com.taim.desktopui.constants.Constant.FXStyle.FX_ERROR_LABEL_COLOR;

public class StaffEditDialogController {
    private static final Logger logger = LoggerFactory.getLogger(StaffEditDialogController.class);
    private StaffDTO staff;
    private Stage stage;
    private StaffClient client;
    private Executor executor;
    private OrganizationEditDialogController organizationEditDialogController;
    private boolean okClicked;

    @FXML
    private JFXTextField usernameField;
    @FXML
    private JFXPasswordField passwordField;
    @FXML
    private JFXTextField fullnameField;
    @FXML
    private JFXComboBox<Staff.Position> positionComboBox;
    @FXML
    private JFXTextField emailField;
    @FXML
    private JFXTextField phoneField;
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
        positionComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            staff.setPosition(newValue);
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
                this.staff = saveUpdateStaffTask.getValue();
                okClicked = true;
                stage.close();
            });
            saveUpdateStaffTask.exceptionProperty().addListener((observable, oldValue, newValue) -> {
                if(newValue != null) {
                    Exception ex = (Exception) newValue;
                    logger.error(ExceptionUtils.getRootCause(ex).getMessage());
                    JSONObject errorMsg = new JSONObject(ExceptionUtils.getRootCause(ex).getMessage());
                    new AlertBuilder(stage).alertType(Alert.AlertType.ERROR)
                            .alertContentText(errorMsg.getString("taimErrorMessage"))
                            .build()
                            .showAndWait();
                    if(errorMsg.getInt("taimErrorCode") == 1){
                        Task<StaffDTO> getStaffTask = new Task<StaffDTO>() {
                            @Override
                            protected StaffDTO call() throws Exception {
                                return RestClientFactory.getStaffClient().getById(staff.getId());
                            }
                        };
                        getStaffTask.setOnSucceeded(event -> {
                            initData(getStaffTask.getValue());
                        });
                        getStaffTask.exceptionProperty().addListener((observable1, oldValue1, newValue1) -> {
                            if(newValue1 != null) {
                                Exception newEx = (Exception) newValue1;
                                String newExMsg = ExceptionUtils.getRootCause(newEx).getMessage();
                                logger.error(newExMsg);
                                JSONObject newErrorMessage = new JSONObject(newExMsg);
                                new AlertBuilder(stage).alertType(Alert.AlertType.ERROR)
                                        .alertContentText(newErrorMessage.getString("taimErrorMessage"))
                                        .build()
                                        .showAndWait();
                            }});

                        executor.execute(getStaffTask);
                    }
                }
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

    public boolean isOkClicked() {
        return okClicked;
    }
}
