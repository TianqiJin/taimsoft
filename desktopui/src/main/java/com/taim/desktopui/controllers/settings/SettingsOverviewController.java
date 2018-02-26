package com.taim.desktopui.controllers.settings;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class SettingsOverviewController {
    private Stage stage;
    @FXML
    private TabPane settingsTabPane;

    @FXML
    public void initialize(){
        settingsTabPane.getSelectionModel().clearSelection();
        settingsTabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {
                FXMLLoader fXMLLoader = new FXMLLoader();
                AnchorPane root;
                if(newValue.getText().equals("General Settings")){
                    root = fXMLLoader.load(this.getClass().getResource("/fxml/settings/GeneralSettings.fxml").openStream());
                }else{
                    root = fXMLLoader.load(this.getClass().getResource("/fxml/settings/AccountSettings.fxml").openStream());
                }
                root.prefHeightProperty().bind(settingsTabPane.heightProperty());
                root.prefWidthProperty().bind(settingsTabPane.widthProperty());
                newValue.setContent(root);
                ISettingsController controller = fXMLLoader.getController();
                controller.setStage(stage);
                controller.initData();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        settingsTabPane.getSelectionModel().selectFirst();
    }

    public SettingsOverviewController(){}

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
