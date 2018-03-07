package com.taim.desktopui.controllers.payment;

import com.jfoenix.controls.JFXTextArea;
import javafx.fxml.FXML;

public class PaymentMemoController {
    @FXML
    private JFXTextArea memoTextArea;

    @FXML
    private void initialize(){}

    public String getMemo(){
        return this.memoTextArea.getText();
    }
}
