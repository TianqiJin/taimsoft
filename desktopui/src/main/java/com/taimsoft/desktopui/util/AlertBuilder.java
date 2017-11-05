package com.taimsoft.desktopui.util;

import com.taimsoft.desktopui.constants.Constant;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Created by jiawei.liu on 9/18/17.
 */
public class AlertBuilder {
    private static Alert alert;

    public AlertBuilder(){
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle(null);
        alert.setContentText(null);
        alert.setTitle(null);
        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
        alertStage.getIcons().add(new Image(AlertBuilder.class.getResourceAsStream(Constant.Image.appIconPath)));
        alert.getDialogPane().getStylesheets().add(AlertBuilder.class.getResource("/css/bootstrap3.css").toExternalForm());
    }

    public AlertBuilder alertType(Alert.AlertType type){
        alert.setAlertType(type);
        return this;
    }
    public AlertBuilder alertHeaderText(String headText){
        alert.setHeaderText(headText);
        return this;
    }
    public AlertBuilder alertContentText(String contentText){
        alert.setContentText(contentText);
        return this;
    }
    public AlertBuilder alertTitle(String title){
        alert.setTitle(title);
        return this;
    }
    public AlertBuilder alertButton(ButtonType...buttonTypes){
        alert.getButtonTypes().setAll(buttonTypes);
        return this;
    }
    public Alert build(){
        return alert;
    }
}
