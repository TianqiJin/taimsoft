package com.taim.desktopui.controllers.payment;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.taim.client.CustomerClient;
import com.taim.client.PaymentClient;
import com.taim.desktopui.util.IDGenerator;
import com.taim.desktopui.util.RestClientFactory;
import com.taim.desktopui.util.VistaNavigator;
import com.taim.dto.CustomerDTO;
import com.taim.dto.PaymentDTO;
import com.taim.dto.TransactionDTO;
import com.taim.model.Payment;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class GeneratePaymentRootController implements Initializable{
    private static final Logger logger = LoggerFactory.getLogger(GeneratePaymentRootController.class);
    private CustomerClient customerClient;
    private PaymentClient paymentClient;
    private List<CustomerDTO> customerList;
    private List<String> customerNameList;
    private List<String> customerEmailList;
    private List<String> customerPhoneList;
    private ObservableList<TransactionDTO> transactionList;
    private Executor executor;
    private PaymentDTO payment;
    private Stage dialogStage;

    @FXML
    private Label titleLabel;
    @FXML
    private JFXComboBox<String> customerComboBox;
    @FXML
    private JFXComboBox<String> customerSearchTypeComboBox;
    @FXML
    private JFXTextField paymentAmountTextField;
    @FXML
    private Label paymentTypeLabel;
    @FXML
    private JFXTextField paymentIDTextField;
    @FXML
    private Label typeLabel;
    @FXML
    private JFXDatePicker paymentDatePicker;
    @FXML
    private Label staffNameLabel;
    @FXML
    private JFXTextField memoTextField;
    @FXML
    private Label amountDueLabel;
    @FXML
    private Label appliedAmountLabel;
    @FXML
    private Label discountLabel;
    @FXML
    private TableView<TransactionDTO> transactionTableView;
    @FXML
    private TableColumn<TransactionDTO, Number> idCol;
    @FXML
    private TableColumn<TransactionDTO, String> dateCol;
    @FXML
    private TableColumn<TransactionDTO, String> typeCol;
    @FXML
    private TableColumn<TransactionDTO, Number> amountCol;
    @FXML
    private TableColumn<TransactionDTO, Number> balanceCol;
    @FXML
    private TableColumn<TransactionDTO, Number> paymentCol;
    @FXML
    private TableColumn deleteCol;


    public GeneratePaymentRootController(){
        this.executor = Executors.newCachedThreadPool(r -> {
            Thread t = new Thread(r);
            t.setDaemon(true);
            return t;
        });
        customerClient = RestClientFactory.getCustomerClient();
        paymentClient = RestClientFactory.getPaymentClient();
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

    public void init(Payment.PaymentType type, PaymentDTO payment){
        //initialize PaymentDTO
        initPayment(payment, type);
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
