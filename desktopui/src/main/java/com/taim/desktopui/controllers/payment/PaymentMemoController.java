package com.taim.desktopui.controllers.payment;

import com.jfoenix.controls.JFXTextArea;
import com.taim.dto.PaymentDTO;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import org.fxmisc.easybind.EasyBind;

public class PaymentMemoController {
    private ObjectProperty<PaymentDTO> payment;
    @FXML
    private JFXTextArea memoTextArea;

    public PaymentMemoController(){
        this.payment = new SimpleObjectProperty<>();
    }

    @FXML
    private void initialize(){
        memoTextArea.textProperty().bindBidirectional(EasyBind.monadic(payment).selectProperty(PaymentDTO::noteProperty));
    }

    public void setPayment(PaymentDTO paymentDTO){
        this.payment.setValue(paymentDTO);
    }
}
