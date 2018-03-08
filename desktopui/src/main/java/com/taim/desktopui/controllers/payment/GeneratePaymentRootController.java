package com.taim.desktopui.controllers.payment;

import com.taim.desktopui.util.VistaNavigator;
import com.taim.dto.PaymentDTO;
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

    enum PaymentMode{
        VENDOR("Vendor"),
        CUSTOMER("Customer");

        String value;

        PaymentMode(String vvalue){this.value = vvalue;}

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public static PaymentMode getValue(String value){
            for (PaymentMode p: PaymentMode.values()){
                if (p.name().equalsIgnoreCase(value)){
                    return p;
                }
            }
            return null;
        }
    }

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

    private void initPayment(PaymentDTO payment){
        if(payment == null){
            this.payment = new PaymentDTO();
            payment.setDateCreated(DateTime.now());
            payment.setDateModified(DateTime.now());
            payment.setStaff(VistaNavigator.getGlobalStaff());
        }else{
            this.payment = payment;
        }
    }

    private void initPaymentTransactionPanel(){
        try {
            FXMLLoader fXMLLoader = new FXMLLoader();
            AnchorPane root = fXMLLoader.load(this.getClass().getResource("/fxml/payment/PaymentTransaction.fxml").openStream());
            root.prefHeightProperty().bind(memoPane.heightProperty());
            root.prefWidthProperty().bind(memoPane.widthProperty());
            this.paymentTransactionController = fXMLLoader.getController();
            transactionPane.getChildren().addAll(root);
        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    private void initPaymentAmountInformationPanel(){
        try {
            FXMLLoader fXMLLoader = new FXMLLoader();
            AnchorPane root = fXMLLoader.load(this.getClass().getResource("/fxml/payment/PaymentAmountInformation.fxml").openStream());
            root.prefHeightProperty().bind(memoPane.heightProperty());
            root.prefWidthProperty().bind(memoPane.widthProperty());
            this.paymentAmountInformationController = fXMLLoader.getController();
            paymentInfoPane.setContent(root);

        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    private void initBasicInformationPanel(){
        try {
            FXMLLoader fXMLLoader = new FXMLLoader();
            AnchorPane root = fXMLLoader.load(this.getClass().getResource("/fxml/payment/PaymentBasicInformation.fxml").openStream());
            root.prefHeightProperty().bind(memoPane.heightProperty());
            root.prefWidthProperty().bind(memoPane.widthProperty());
            this.paymentBasicInformationController = fXMLLoader.getController();
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
            summaryPane.setContent(root);

        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    private void initModeLabel(PaymentMode mode){
        titleLabel.setText(mode.getValue());
    }

    public void init(PaymentMode mode, PaymentDTO payment){
        //initialize PaymentDTO
        initPayment(payment);
        //initialize mode label
        initModeLabel(mode);
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
}
