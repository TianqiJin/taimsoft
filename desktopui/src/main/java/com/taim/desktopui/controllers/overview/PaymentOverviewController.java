package com.taim.desktopui.controllers.overview;

import com.jfoenix.controls.JFXComboBox;
import com.taim.client.IClient;
import com.taim.client.PaymentClient;
import com.taim.desktopui.util.RestClientFactory;
import com.taim.dto.PaymentDTO;
import com.taim.model.Payment;
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
        actionCol.setCellValueFactory(new PropertyValueFactory<>("action"));
        List<String> paymentTypeList = Arrays.stream(Payment.PaymentType.values()).map(Payment.PaymentType::getValue).collect(Collectors.toList());
        createNewPaymentComboBox.setItems(FXCollections.observableArrayList(paymentTypeList));
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
