package com.taim.desktopui.controllers.settings;

import com.jfoenix.controls.JFXTextArea;
import com.taim.client.PropertyClient;
import com.taim.desktopui.uicomponents.FadingStatusMessage;
import com.taim.desktopui.util.AlertBuilder;
import com.taim.desktopui.util.RestClientFactory;
import com.taim.dto.PropertyDTO;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executor;

public class ModifyTermsController {
    private static final Logger logger = LoggerFactory.getLogger(ModifyTermsController.class);
    private PropertyDTO property;
    private Executor executor;
    private PropertyClient propertyClient;
    private Stage dialogStage;

    @FXML
    private JFXTextArea termArea;

    @FXML
    private void initialize(){}

    @FXML
    public void handleSaveButton(){
        Task<PropertyDTO> savePropertyTask = new Task<PropertyDTO>() {
            @Override
            protected PropertyDTO call() throws Exception {
                return propertyClient.update(property);
            }
        };

        savePropertyTask.setOnSucceeded(event -> {
            dialogStage.close();
        });

        savePropertyTask.exceptionProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null) {
                Exception ex = (Exception) newValue;
                logger.error(ExceptionUtils.getRootCause(ex).getMessage());
                JSONObject errorMsg = new JSONObject(ExceptionUtils.getRootCause(ex).getMessage());
                new AlertBuilder().alertType(Alert.AlertType.ERROR)
                        .alertContentText(errorMsg.getString("taimErrorMessage"))
                        .build()
                        .showAndWait();
                if(errorMsg.getInt("taimErrorCode") == 1){
                    Task<PropertyDTO> getPropertyTask = new Task<PropertyDTO>() {
                        @Override
                        protected PropertyDTO call() throws Exception {
                            return RestClientFactory.getPropertyClient().getById(property.getId());
                        }
                    };
                    getPropertyTask.setOnSucceeded(event -> {
                        this.termArea.setText(getPropertyTask.getValue().getTerms());
                    });
                    getPropertyTask.exceptionProperty().addListener((observable1, oldValue1, newValue1) -> {
                        if(newValue1 != null) {
                            Exception newEx = (Exception) newValue1;
                            String newExMsg = ExceptionUtils.getRootCause(newEx).getMessage();
                            logger.error(newExMsg);
                            JSONObject newErrorMessage = new JSONObject(newExMsg);
                            new AlertBuilder().alertType(Alert.AlertType.ERROR)
                                    .alertHeaderText(newErrorMessage.getString("taimErrorMessage"))
                                    .build()
                                    .showAndWait();
                        }});

                    executor.execute(getPropertyTask);
                }
            }
        });

        executor.execute(savePropertyTask);
    }

    public void initData(PropertyDTO property, Executor executor, PropertyClient propertyClient){
        this.property = property;
        this.executor = executor;
        this.propertyClient = propertyClient;
        this.termArea.textProperty().bindBidirectional(this.property.termsProperty());
    }

    public PropertyDTO getProperty() {
        return property;
    }

    public void setProperty(PropertyDTO property) {
        this.property = property;
    }

    public Stage getDialogStage() {
        return dialogStage;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
}
