package com.taimsoft.desktopui.controllers.login;

import com.taimsoft.desktopui.controllers.settings.GeneralSettingsController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class PropertyConfigurationController {
    @FXML
    private AnchorPane settingsPane;
    @FXML
    private Button finishButton;

    @FXML
    private void initialize(){
        finishButton.setLayoutX(settingsPane.widthProperty().subtract(finishButton.widthProperty()).divide(2).getValue());
        try {
            FXMLLoader fXMLLoader = new FXMLLoader();
            AnchorPane root = fXMLLoader.load(this.getClass().getResource("/fxml/settings/GeneralSettings.fxml").openStream());
            GeneralSettingsController controller = fXMLLoader.getController();
            controller.initWelcomeData();
            root.prefHeightProperty().bind(settingsPane.heightProperty());
            root.prefWidthProperty().bind(settingsPane.widthProperty());
            settingsPane.getChildren().setAll(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void handleFinishButton(){}
}
