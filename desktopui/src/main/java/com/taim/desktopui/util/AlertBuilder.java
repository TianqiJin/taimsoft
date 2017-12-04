package com.taim.desktopui.util;

import com.taim.desktopui.TaimDesktop;
import com.taim.desktopui.constants.Constant;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Created by jiawei.liu on 9/18/17.
 */
public class AlertBuilder {
    private static Alert alert;

    public AlertBuilder(){
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle("TAIM SOFTWARE ERROR DIALOG");
        alert.setContentText(null);
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
        if(alert.getAlertType().equals(Alert.AlertType.INFORMATION) && alert.getHeaderText() == null){
            alert.setHeaderText("INFORMATION");
        }else if(alert.getAlertType().equals(Alert.AlertType.CONFIRMATION) && alert.getHeaderText() == null){
            alert.setHeaderText("CONFIRMATION");
        }else if (alert.getAlertType().equals(Alert.AlertType.WARNING) && alert.getHeaderText() == null){
            alert.setHeaderText("WARNING");
        }else if (alert.getAlertType().equals(Alert.AlertType.ERROR) && alert.getHeaderText() == null){
            alert.setHeaderText("ERROR");
        }

        alert.initStyle(StageStyle.UNIFIED);

        return alert;
    }
}
