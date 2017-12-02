package com.taimsoft.desktopui.controllers.login;

import com.taim.client.PropertyClient;
import com.taim.dto.PropertyDTO;
import com.taimsoft.desktopui.controllers.settings.GeneralSettingsController;
import com.taimsoft.desktopui.uicomponents.FadingStatusMessage;
import com.taimsoft.desktopui.util.AlertBuilder;
import com.taimsoft.desktopui.util.RestClientFactory;
import com.taimsoft.desktopui.util.VistaNavigator;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class PropertyConfigurationController {
    private static final Logger logger = LoggerFactory.getLogger(PropertyConfigurationController.class);
    private GeneralSettingsController controller;
    private Stage stage;

    @FXML
    private AnchorPane settingsPane;
    @FXML
    private VBox buttonBox;

    @FXML
    private void initialize(){
        buttonBox.prefWidthProperty().bind(settingsPane.widthProperty());

        try {
            FXMLLoader fXMLLoader = new FXMLLoader();
            AnchorPane root = fXMLLoader.load(this.getClass().getResource("/fxml/settings/GeneralSettings.fxml").openStream());
            controller = fXMLLoader.getController();
            controller.initWelcomeData();
            root.prefHeightProperty().bind(settingsPane.heightProperty());
            root.prefWidthProperty().bind(settingsPane.widthProperty());
            settingsPane.getChildren().setAll(root);
        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    @FXML
    private void handleFinishButton(){
        controller.scanCompanyInfoFields();
        if(controller.getProperty().getLicense() == null){
            new AlertBuilder()
                    .alertType(Alert.AlertType.ERROR)
                    .alertContentText("No valid is found. Please upload a valid license")
                    .build()
                    .showAndWait();
        }else{
            if(controller.isCompanyInfoValid()){
                if(controller.getCustomerClassTable().getItems() != null){
                    controller.getProperty().setCustomerClasses(controller.getCustomerClassTable().getItems());
                }
                Task<PropertyDTO> addPropertyTask = new Task<PropertyDTO>() {
                    @Override
                    protected PropertyDTO call() throws Exception {
                        return controller.getPropertyClient().add(controller.getProperty());
                    }
                };

                addPropertyTask.setOnSucceeded(event -> {
                    VistaNavigator.setGlobalProperty(addPropertyTask.getValue());
                    this.stage.close();
                });

                addPropertyTask.setOnFailed(event ->
                        FadingStatusMessage.flash("Failed to create properties. Please try again.", settingsPane));

                controller.getExecutor().execute(addPropertyTask);
            }
        }

    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
