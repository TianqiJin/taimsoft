package com.taim.desktopui.controllers.payment;

import com.taim.dto.PaymentDTO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class GeneratePaymentRootController {
    private static final Logger logger = LoggerFactory.getLogger(GeneratePaymentRootController.class);
    private Executor executor;
    private PaymentDTO payment;
    private Stage dialogStage;
    private PaymentMemoController paymentMemoController;

    @FXML
    private Label titleLabel;
    @FXML
    private TitledPane memoPane;

    public GeneratePaymentRootController(){
        this.executor = Executors.newCachedThreadPool(r -> {
            Thread t = new Thread(r);
            t.setDaemon(true);
            return t;
        });
    }

    @FXML
    private void initialize(){}

    @FXML
    private void handleCancelButton(){}

    @FXML
    private void handleConfirmButton(){}

    public void initPayment(PaymentDTO payment){
        if(payment == null){
            this.payment = new PaymentDTO();
            payment.setDateModified(DateTime.now());
            payment.setDateCreated(DateTime.now());
        }else{
            this.payment = payment;
        }
    }

    public void initPaymentTransactionPanel(){}

    public void initPaymentInformationPanel(){}

    public void initBasicInformationPanel(){}

    public PaymentMemoController initMemoPanel(){
        try {
            FXMLLoader fXMLLoader = new FXMLLoader();
            AnchorPane root = fXMLLoader.load(this.getClass().getResource("/fxml/payment/PaymentMemo.fxml").openStream());
            root.prefHeightProperty().bind(memoPane.heightProperty());
            root.prefWidthProperty().bind(memoPane.widthProperty());
            PaymentMemoController controller = fXMLLoader.getController();
            memoPane.setContent(root);
            return controller;

        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
        }

        return null;
    }

    public void initSummaryPanel(){}


}
