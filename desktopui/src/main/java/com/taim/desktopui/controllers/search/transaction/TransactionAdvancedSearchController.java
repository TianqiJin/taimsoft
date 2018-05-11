package com.taim.desktopui.controllers.search.transaction;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.taim.client.TransactionClient;
import com.taim.desktopui.util.RestClientFactory;
import com.taim.dto.TransactionDTO;
import com.taim.model.Transaction;
import com.taim.model.search.TransactionSearch;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TransactionAdvancedSearchController implements Initializable {
    private Stage dialogStage;
    private TransactionSearch transactionSearch;

    @FXML
    private JFXTextField idTextField;
    @FXML
    private TableView<TransactionDTO> transactionTableView;
    @FXML
    private JFXComboBox<String> transactionTypeComboBox;
    @FXML
    private JFXComboBox<String> transactionCategoryComboBox;
    @FXML
    private JFXDatePicker fromdDatePicker;
    @FXML
    private JFXDatePicker toDatePicker;
    @FXML
    private JFXTextField customerNameTextField;
    @FXML
    private JFXTextField vendorNameTextField;
    @FXML
    private JFXTextField staffNameTextField;
    @FXML
    private JFXComboBox<String> paymentStatusComboBox;
    @FXML
    private JFXComboBox<String> deliveryStatusComboBox;
    @FXML
    private JFXTextField notesTextField;
    @FXML
    private JFXCheckBox paymentOverDueCheckBox;
    @FXML
    private JFXCheckBox deliveryOverDueCheckBox;
    @FXML
    private JFXCheckBox finalizedCheckBox;

    public TransactionAdvancedSearchController(){}

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        paymentStatusComboBox.setItems(FXCollections.observableArrayList(
                Stream.of(Transaction.PaymentStatus.values()).map(Transaction.PaymentStatus::name).collect(Collectors.toList())));
        deliveryStatusComboBox.setItems(FXCollections.observableArrayList(
                Stream.of(Transaction.DeliveryStatus.values()).map(Transaction.DeliveryStatus::name).collect(Collectors.toList())));
        transactionTypeComboBox.setItems(FXCollections.observableArrayList(
                Stream.of(Transaction.TransactionType.values()).map(Transaction.TransactionType::name).collect(Collectors.toList())));
        transactionCategoryComboBox.setItems(FXCollections.observableArrayList(
                Stream.of(Transaction.TransactionCategory.values()).map(Transaction.TransactionCategory::name).collect(Collectors.toList())));
    }
}
