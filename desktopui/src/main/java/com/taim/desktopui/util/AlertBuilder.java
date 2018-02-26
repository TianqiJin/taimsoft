package com.taim.desktopui.util;

import com.jfoenix.controls.JFXAlert;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialogLayout;
import com.taim.desktopui.TaimDesktop;
import com.taim.desktopui.constants.Constant;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Optional;

/**
 * Created by jiawei.liu on 9/18/17.
 */
public class AlertBuilder {
    private JFXAlert<ButtonType> alert;
    private JFXDialogLayout content;
    private Alert.AlertType type;

    public AlertBuilder(Stage stage){
        alert = new JFXAlert<>(stage);
        content = new JFXDialogLayout();
    }

    public AlertBuilder alertType(Alert.AlertType type){
        this.type = type;
        return this;
    }

    public AlertBuilder alertHeaderText(String headText){
        content.setHeading(new Text(headText));
        return this;
    }

    public AlertBuilder alertContentText(String contentText){
        content.setBody(new Text(contentText));
        return this;
    }

    public AlertBuilder alertButton(JFXButton... buttons){
        content.setActions(buttons);
        return this;
    }

    public JFXAlert<ButtonType> build(){
        if(this.type == null){
            this.type = Alert.AlertType.INFORMATION;
        }
        if(content.getHeading().size() == 0){
            if(this.type.equals(Alert.AlertType.INFORMATION)){
                content.setHeading(new Text("INFORMATION"));
            }else if(this.type.equals(Alert.AlertType.CONFIRMATION)){
                content.setHeading(new Text("CONFIRMATION"));
            }else if(this.type.equals(Alert.AlertType.ERROR)){
                content.setHeading(new Text("ERROR"));
            }else if(this.type.equals(Alert.AlertType.WARNING)){
                content.setHeading(new Text("WARNING"));
            }
        }

        if(this.content.getActions().size() == 0){
            if(this.type.equals(Alert.AlertType.CONFIRMATION)){
                JFXButton doneButton = new JFXButton("Done");
                JFXButton cancelButton = new JFXButton("Cancel");
                doneButton.setDefaultButton(true);
                cancelButton.setCancelButton(true);
                doneButton.setOnAction(event -> {alert.setResult(ButtonType.OK); alert.close();});
                cancelButton.setOnAction(event -> {alert.setResult(ButtonType.CANCEL);alert.close();});
                content.setActions(doneButton, cancelButton);
            }else{
                JFXButton doneButton = new JFXButton("Done");
                doneButton.setDefaultButton(true);
                doneButton.setOnAction(event -> {alert.setResult(ButtonType.OK); alert.close();});
                content.setActions(doneButton);
            }
        }
        alert.setContent(content);

        return alert;
    }
}
