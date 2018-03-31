package com.taim.desktopui.controllers.search;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class SearchRootController implements Initializable{
    private Stage dialogStage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    private void handleFilterButton(){}

    @FXML
    private void handleClearButton(){

    }

    @FXML
    private void handleCanelButton(){
        dialogStage.close();
    }

    @FXML
    private void handleAddButton(){}

    public Stage getDialogStage() {
        return dialogStage;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
}
