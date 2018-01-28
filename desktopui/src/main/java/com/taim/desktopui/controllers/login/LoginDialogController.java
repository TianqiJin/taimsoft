package com.taim.desktopui.controllers.login;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.taim.client.PropertyClient;
import com.taim.client.StaffClient;
import com.taim.desktopui.uicomponents.FadingStatusMessage;
import com.taim.dto.OrganizationDTO;
import com.taim.dto.PropertyDTO;
import com.taim.dto.StaffDTO;
import com.taim.licensegen.GenerateLicense;
import com.taim.desktopui.constants.Constant;
import com.taim.desktopui.controllers.edit.StaffEditDialogController;
import com.taim.desktopui.util.AlertBuilder;
import com.taim.desktopui.util.RestClientFactory;
import com.taim.desktopui.util.VistaNavigator;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.joda.time.DateTime;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class LoginDialogController {
    private static final Logger logger = LoggerFactory.getLogger(LoginDialogController.class);
    private Stage dialogStage;
    private StaffClient staffClient;
    private PropertyClient propertyClient;
    private Executor executor;
    private boolean successful;

    @FXML
    private JFXTextField userNameField;
    @FXML
    private JFXPasswordField passwordField;
    @FXML
    private JFXButton loginButton;
    @FXML
    private Label errorLMsgLabel;
    @FXML
    private Hyperlink signUpLink;
    @FXML
    private Hyperlink uploadLicenseLink;
    @FXML
    private AnchorPane logingRoot;

    @FXML
    private void initialize(){
        userNameField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!userNameField.getText().trim().isEmpty()){
                loginButton.setDisable(false);
            }
            else{
                loginButton.setDisable(true);
            }
        });
        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!passwordField.getText().trim().isEmpty()){
                loginButton.setDisable(false);
            }
            else{
                loginButton.setDisable(true);
            }
        });
        userNameField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue) { // we only care about loosing focus
               errorLMsgLabel.setText("");
            }
        });
        passwordField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue) { // we only care about loosing focus
                errorLMsgLabel.setText("");
            }
        });
        signUpLink.setOnAction(event -> {
            try{
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(LoginDialogController.class.getResource("/fxml/edit/StaffEditDialog.fxml"));
                AnchorPane page = loader.load();

                Stage dialogStage = new Stage();
                dialogStage.setTitle("Edit Staff");
                page.getStylesheets().add(LoginDialogController.class.getResource("/css/jfoneix.css").toExternalForm());
                Scene scene = new Scene(page);
                dialogStage.setScene(scene);
                dialogStage.initModality(Modality.WINDOW_MODAL);
                //Set the stage bound to the maximum of the screen
                Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
                dialogStage.setX(bounds.getMinX());
                dialogStage.setY(bounds.getMinY());
                dialogStage.setWidth(bounds.getWidth());
                dialogStage.setHeight(bounds.getHeight());
                StaffEditDialogController controller = loader.getController();
                controller.setStage(dialogStage);
                StaffDTO staffDTO = new StaffDTO();
                staffDTO.setDateCreated(DateTime.now());
                staffDTO.setDateModified(DateTime.now());
                OrganizationDTO organization = new OrganizationDTO();
                organization.setDateCreated(DateTime.now());
                organization.setDateModified(DateTime.now());
                staffDTO.setOrganization(organization);
                controller.initData(staffDTO);

                dialogStage.showAndWait();
            }catch (IOException e){
                logger.error(e.getMessage(), e);
            }

        });

        uploadLicenseLink.setOnAction(event -> handleUploadLicenseLink());
    }

    @FXML
    public void handleLogin(){
        loginButton.setDisable(true);
        String userName = userNameField.getText();
        String password = passwordField.getText();

        Task<StaffDTO> staffDTOTask = new Task<StaffDTO>() {
            @Override
            protected StaffDTO call() throws Exception {
                return staffClient.getByName(userName);
            }
        };
        Task<List<PropertyDTO>> propertyTask = new Task<List<PropertyDTO>>() {
            @Override
            protected List<PropertyDTO> call() throws Exception {
                return propertyClient.getList();
            }
        };

        propertyTask.setOnSucceeded(event -> {
            if(propertyTask.getValue().size() != 0){
                signUpLink.setDisable(false);
                VistaNavigator.setGlobalProperty(propertyTask.getValue().get(0));
                Path tmpLicenseFile = null;
                boolean isLicenseValid = false;
                try {
                    tmpLicenseFile = Files.createTempFile("tmpLicenseFile", null);
                    Files.write(tmpLicenseFile, VistaNavigator.getGlobalProperty().getLicense().getLicenseFile());
                    GenerateLicense generateLicense = new GenerateLicense();
                    try {
                        generateLicense.verifyLicense(tmpLicenseFile.toFile(), "Taim Desktop");
                        isLicenseValid = true;
                    } catch (GenerateLicense.GenerateLicenseException e) {
                        new AlertBuilder().alertType(Alert.AlertType.ERROR)
                                .alertContentText("Please upload a new license!\n\n" + e.getMessage())
                                .build()
                                .showAndWait();
                    }
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }finally {
                    if(tmpLicenseFile != null && tmpLicenseFile.toFile().exists()){
                        tmpLicenseFile.toFile().delete();
                    }
                }

                if(isLicenseValid){
                    successful = true;
                    dialogStage.close();
                }
            }else{
                signUpLink.setDisable(true);
                try {
                    FXMLLoader fXMLLoader = new FXMLLoader();
                    AnchorPane root = fXMLLoader.load(this.getClass().getResource("/fxml/login/WelcomeDialog.fxml").openStream());
                    Scene scene = new Scene(root);

                    Stage welcomeStage = new Stage();
                    welcomeStage.setTitle("Welcome");
                    welcomeStage.setScene(scene);
                    welcomeStage.initModality(Modality.WINDOW_MODAL);

                    WelcomeDialogController controller = fXMLLoader.getController();
                    controller.setStage(welcomeStage);

                    welcomeStage.showAndWait();
                    successful = true;
                    dialogStage.close();
                } catch (IOException ex) {
                    logger.error(ex.getMessage(), ex);
                }
            }
        });

        propertyTask.exceptionProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null) {
                Exception ex = (Exception) newValue;
                logger.error(ExceptionUtils.getRootCause(ex).getMessage());
                JSONObject errorMsg = new JSONObject(ExceptionUtils.getRootCause(ex).getMessage());
                new AlertBuilder().alertType(Alert.AlertType.ERROR)
                        .alertContentText(errorMsg.getString("taimErrorMessage"))
                        .build()
                        .showAndWait();
            }
        });

        staffDTOTask.setOnSucceeded(event -> {
            StaffDTO staffDTO = staffDTOTask.getValue();
            if(staffDTO.getPassword().equals(password)){
                VistaNavigator.setGlobalStaff(staffDTO);
                executor.execute(propertyTask);
            }else{
                errorLMsgLabel.setText("Username/Password is incorrect");
                errorLMsgLabel.setStyle(Constant.FXStyle.FX_ERROR_LABEL_COLOR);
            }
        });

        staffDTOTask.setOnFailed(event -> {
            logger.error(staffDTOTask.getException().getMessage());
            errorLMsgLabel.setText("Unable to retrieve staff information");
            errorLMsgLabel.setStyle(Constant.FXStyle.FX_ERROR_LABEL_COLOR);
        });

        executor.execute(staffDTOTask);
    }

    @FXML
    public void handleCancel(){
        dialogStage.close();
    }

    public void handleUploadLicenseLink(){
        if(VistaNavigator.getGlobalProperty() == null){
            new AlertBuilder()
                    .alertType(Alert.AlertType.WARNING)
                    .alertContentText("Please login first to obtain original license information")
                    .build()
                    .showAndWait();
        }else{
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("DAT files (*.dat)", "*.dat");
            fileChooser.getExtensionFilters().add(extFilter);
            fileChooser.setTitle("Upload New License");
            File licenseFile = fileChooser.showOpenDialog(logingRoot.getScene().getWindow());

            boolean isLicenseValid = false;
            GenerateLicense generateLicense = new GenerateLicense();
            try{
                generateLicense.verifyLicense(licenseFile, Constant.Product.TAIM_DESKTOP);
                isLicenseValid = true;
            }catch (GenerateLicense.GenerateLicenseException e){
                logger.error(e.getMessage(), e);
                new AlertBuilder()
                        .alertType(Alert.AlertType.ERROR)
                        .alertContentText(e.getMessage())
                        .build()
                        .showAndWait();
            }

            if(isLicenseValid){
                try {
                    VistaNavigator.getGlobalProperty().getLicense().setLicenseFile(Files.readAllBytes(licenseFile.toPath()));
                    Task<PropertyDTO> savePropertyTask = new Task<PropertyDTO>() {
                        @Override
                        protected PropertyDTO call() throws Exception {
                            return propertyClient.update(VistaNavigator.getGlobalProperty());
                        }
                    };

                    savePropertyTask.setOnSucceeded(event -> {FadingStatusMessage.flash("SUCCESSFULLY UPLOADED LICENSE", logingRoot);});

                    savePropertyTask.setOnFailed(event -> {
                        logger.error(savePropertyTask.getException().getMessage());
                        new AlertBuilder()
                                .alertType(Alert.AlertType.ERROR)
                                .alertContentText("Failed to upload license. Please try again later")
                                .build()
                                .showAndWait();
                    });

                    executor.execute(savePropertyTask);
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }

    }

    public LoginDialogController() {
        this.propertyClient = RestClientFactory.getPropertyClient();
        this.staffClient = RestClientFactory.getStaffClient();
        executor = Executors.newCachedThreadPool(r -> {
            Thread t = new Thread(r);
            t.setDaemon(true);
            return t;
        });
    }

    public void setDialogStage(Stage dialogStage){
        this.dialogStage = dialogStage;
    }

    public boolean isSuccessful() {
        return successful;
    }
}
