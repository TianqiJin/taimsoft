package com.taim.desktopui.controllers.overview;

import com.jfoenix.controls.JFXComboBox;
import com.taim.client.IClient;
import com.taim.client.PaymentClient;
import com.taim.desktopui.controllers.payment.GeneratePaymentRootController;
import com.taim.desktopui.util.PaymentPaneLoader;
import com.taim.desktopui.util.RestClientFactory;
import com.taim.dto.PaymentDTO;
import com.taim.dto.PaymentRecordDTO;
import com.taim.dto.TransactionDTO;
import com.taim.model.Payment;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class PaymentOverviewController extends IOverviewController<PaymentDTO> implements Initializable {
    private PaymentClient paymentClient;
    private static final DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd");

    @FXML
    private TableColumn<PaymentDTO, Number> idCol;
    @FXML
    private TableColumn<PaymentDTO, String> dateCol;
    @FXML
    private TableColumn<PaymentDTO, String> typeCol;
    @FXML
    private TableColumn<PaymentDTO, String> methodCol;
    @FXML
    private TableColumn<PaymentDTO, String> vendorCustomerCol;
    @FXML
    private TableColumn<PaymentDTO, Number> totalCol;
    @FXML
    private TableColumn<PaymentDTO, Number> balanceCol;
    @FXML
    private TableColumn<PaymentDTO, String> actionCol;
    @FXML
    private JFXComboBox<String> createNewPaymentComboBox;

    public PaymentOverviewController(){
        this.paymentClient = RestClientFactory.getPaymentClient();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        dateCol.setCellValueFactory(param -> new SimpleStringProperty(dtf.print(param.getValue().getDateCreated())));
        typeCol.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getPaymentType().getValue()));
        methodCol.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getPaymentMethod().getMethod()));
        totalCol.setCellValueFactory(new PropertyValueFactory<>("paymentAmount"));
        vendorCustomerCol.setCellValueFactory(param -> {
            if(param.getValue().getPaymentType().equals(Payment.PaymentType.CUSTOMER_PAYMENT)){
                return param.getValue().getCustomer().fullnameProperty();
            }else{
                return param.getValue().getVendor().fullnameProperty();
            }
        });
        balanceCol.setCellValueFactory(param -> {
            PaymentDTO payment = param.getValue();
            double balance = payment.getPaymentAmount();
            for(TransactionDTO transaction: this.getTransactionList()){
                for(PaymentRecordDTO paymentRecordDTO: transaction.getPaymentRecords()){
                    if(paymentRecordDTO.getId() == payment.getId()){
                        balance -= paymentRecordDTO.getAmount();
                    }
                }
            }
            return new SimpleDoubleProperty(balance);
        });
        actionCol.setCellValueFactory(new PropertyValueFactory<>("action"));
        List<String> paymentTypeList = Arrays.stream(Payment.PaymentType.values()).map(Payment.PaymentType::getValue).collect(Collectors.toList());
        createNewPaymentComboBox.setItems(FXCollections.observableArrayList(paymentTypeList));
        createNewPaymentComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null && Payment.PaymentType.getType(newValue) != null){
                if(Payment.PaymentType.getType(newValue).equals(Payment.PaymentType.CUSTOMER_PAYMENT)){
                    PaymentPaneLoader.loadCustomerPayment(null, Payment.PaymentType.CUSTOMER_PAYMENT);
                }else{
                    PaymentPaneLoader.loadCustomerPayment(null, Payment.PaymentType.VENDOR_PAYMENT);
                }
            }
        });
    }

    @Override
    public void initSearchField() {}

    @Override
    public IClient<PaymentDTO> getOverviewClient() {
        return this.paymentClient;
    }

    @Override
    public void initSummaryLabel() {}
}
