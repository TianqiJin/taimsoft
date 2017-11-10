package com.taimsoft.desktopui.controllers.login;

import com.taimsoft.desktopui.controllers.settings.ISettingsController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class WelcomeDialogController {
    private Stage welcomeStage;
    @FXML
    private void initialize(){}

    @FXML
    private void handleConfigurePropertiesButton(){
        try {
            FXMLLoader fXMLLoader = new FXMLLoader();
            AnchorPane root = fXMLLoader.load(this.getClass().getResource("/fxml/login/PropertyConfiguration.fxml").openStream());
            PropertyConfigurationController controller = fXMLLoader.getController();
            root.getStylesheets().add(WelcomeDialogController.class.getResource("/css/bootstrap3.css").toExternalForm());
            Scene scene = new Scene(root);

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Configure Properties");
            dialogStage.setScene(scene);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
            dialogStage.setX(bounds.getMinX());
            dialogStage.setY(bounds.getMinY());
            dialogStage.setWidth(bounds.getWidth());
            dialogStage.setHeight(bounds.getHeight());
            controller.setStage(dialogStage);

            dialogStage.showAndWait();
            welcomeStage.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public Stage getStage() {
        return welcomeStage;
    }

    public void setStage(Stage stage) {
        this.welcomeStage = stage;
    }
}
