package com.taim.desktopui.controllers.payment;

import com.taim.desktopui.util.IDGenerator;
import com.taim.desktopui.util.VistaNavigator;
import com.taim.dto.PaymentDTO;
import com.taim.model.Payment;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class GeneratePaymentRootController implements Initializable{

    private static final Logger logger = LoggerFactory.getLogger(GeneratePaymentRootController.class);
    private Executor executor;
    private PaymentDTO payment;
    private Stage dialogStage;
    private PaymentMemoController paymentMemoController;
    private PaymentBasicInformationController paymentBasicInformationController;
    private PaymentAmountInformationController paymentAmountInformationController;
    private PaymentSummaryController paymentSummaryController;
    private PaymentTransactionController paymentTransactionController;

    @FXML
    private Label titleLabel;
    @FXML
    private TitledPane memoPane;
    @FXML
    private TitledPane paymentInfoPane;
    @FXML
    private TitledPane basicInfoPane;
    @FXML
    private AnchorPane transactionPane;
    @FXML
    private TitledPane summaryPane;

    public GeneratePaymentRootController(){
        this.executor = Executors.newCachedThreadPool(r -> {
            Thread t = new Thread(r);
            t.setDaemon(true);
            return t;
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {}

    @FXML
    private void handleCancelButton(){
        dialogStage.close();
    }

    @FXML
    private void handleConfirmButton(){}

    private void initPayment(PaymentDTO payment, Payment.PaymentType paymentType){
        if(payment == null){
            this.payment = new PaymentDTO();
            this.payment.setPresentId(IDGenerator.createPaymentID());
            this.payment.setDateCreated(DateTime.now());
            this.payment.setDateModified(DateTime.now());
            this.payment.setStaff(VistaNavigator.getGlobalStaff());
            this.payment.setPaymentType(paymentType);
        }else{
            this.payment = payment;
        }
    }

    private void initPaymentTransactionPanel(){
        try {
            FXMLLoader fXMLLoader = new FXMLLoader();
            AnchorPane root = fXMLLoader.load(this.getClass().getResource("/fxml/payment/PaymentTransaction.fxml").openStream());
            root.prefHeightProperty().bind(transactionPane.heightProperty());
            root.prefWidthProperty().bind(transactionPane.widthProperty());
            this.paymentTransactionController = fXMLLoader.getController();
            this.paymentTransactionController.setPayment(payment);
            transactionPane.getChildren().addAll(root);
        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    private void initPaymentAmountInformationPanel(){
        try {
            FXMLLoader fXMLLoader = new FXMLLoader();
            AnchorPane root = fXMLLoader.load(this.getClass().getResource("/fxml/payment/PaymentAmountInformation.fxml").openStream());
            root.prefHeightProperty().bind(paymentInfoPane.heightProperty());
            root.prefWidthProperty().bind(paymentInfoPane.widthProperty());
            this.paymentAmountInformationController = fXMLLoader.getController();
            this.paymentAmountInformationController.setPayment(payment);
            paymentInfoPane.setContent(root);

        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    private void initBasicInformationPanel(){
        try {
            FXMLLoader fXMLLoader = new FXMLLoader();
            AnchorPane root = fXMLLoader.load(this.getClass().getResource("/fxml/payment/PaymentBasicInformation.fxml").openStream());
            root.prefHeightProperty().bind(basicInfoPane.heightProperty());
            root.prefWidthProperty().bind(basicInfoPane.widthProperty());
            this.paymentBasicInformationController = fXMLLoader.getController();
            this.paymentBasicInformationController.setPayment(payment);
            basicInfoPane.setContent(root);

        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    private void initMemoPanel(){
        try {
            FXMLLoader fXMLLoader = new FXMLLoader();
            AnchorPane root = fXMLLoader.load(this.getClass().getResource("/fxml/payment/PaymentMemo.fxml").openStream());
            root.prefHeightProperty().bind(memoPane.heightProperty());
            root.prefWidthProperty().bind(memoPane.widthProperty());
            this.paymentMemoController = fXMLLoader.getController();
            this.paymentMemoController.setPayment(payment);
            memoPane.setContent(root);
        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    private void initSummaryPanel(){
        try {
            FXMLLoader fXMLLoader = new FXMLLoader();
            AnchorPane root = fXMLLoader.load(this.getClass().getResource("/fxml/payment/PaymentSummary.fxml").openStream());
            root.prefHeightProperty().bind(memoPane.heightProperty());
            root.prefWidthProperty().bind(memoPane.widthProperty());
            this.paymentSummaryController = fXMLLoader.getController();
//            this.paymentSummaryController
            summaryPane.setContent(root);

        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    private void initModeLabel(Payment.PaymentType type){
        titleLabel.setText(type.getValue());
    }

    public void init(Payment.PaymentType type, PaymentDTO payment){
        //initialize PaymentDTO
        initPayment(payment, type);
        //initialize mode label
        initModeLabel(type);
        initBasicInformationPanel();
        initMemoPanel();
        initPaymentAmountInformationPanel();
        initPaymentTransactionPanel();
        initSummaryPanel();
    }

    public Stage getDialogStage() {
        return dialogStage;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public PaymentDTO getPayment() {
        return payment;
    }

    public void setPayment(PaymentDTO payment) {
        this.payment = payment;
    }
}
