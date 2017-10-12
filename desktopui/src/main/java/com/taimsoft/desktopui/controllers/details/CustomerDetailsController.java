package com.taimsoft.desktopui.controllers.details;

import com.taim.dto.CustomerDTO;
import com.taim.dto.TransactionDTO;
import com.taim.model.Transaction;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableStringValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CustomerDetailsController implements IDetailController<CustomerDTO>{
    private CustomerDTO customerDTO;
    private List<TransactionDTO> quotationList;
    private List<TransactionDTO> invoiceList;
    private List<TransactionDTO> returnList;

    @FXML
    private Label storeCreditLabel;
    @FXML
    private Label customerClassLabel;
    @FXML
    private Label fullNameLabel;
    @FXML
    private Label phoneLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label dateCreatedLabel;
    @FXML
    private ComboBox<String> actionComboBox;
    @FXML
    private TabPane transactionTabPane;


    public CustomerDetailsController(){
        quotationList = new ArrayList<>();
        invoiceList = new ArrayList<>();
        returnList = new ArrayList<>();
    }

    @FXML
    public void initialize(){
        actionComboBox.setItems(FXCollections.observableArrayList("EDIT", "DELETE"));
    }

    @Override
    public void initDetailData(CustomerDTO obj) {
        this.customerDTO = obj;
        invoiceList = this.customerDTO.getTransactionList().stream().filter(t -> t.getTransactionType().equals(Transaction.TransactionType.INVOICE)).collect(Collectors.toList());
        quotationList = this.customerDTO.getTransactionList().stream().filter(t -> t.getTransactionType().equals(Transaction.TransactionType.QUOTATION)).collect(Collectors.toList());
        returnList = this.customerDTO.getTransactionList().stream().filter(t -> t.getTransactionType().equals(Transaction.TransactionType.RETURN)).collect(Collectors.toList());
        initTransactionTabPane();
        bindCustomerInfoLabels();
    }

    private void initTransactionTabPane(){
        transactionTabPane.getSelectionModel().clearSelection();
        transactionTabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {
                FXMLLoader fXMLLoader = new FXMLLoader();
                AnchorPane root = fXMLLoader.load(this.getClass().getResource("/fxml/details/TransactionTableView.fxml").openStream());
                root.prefHeightProperty().bind(transactionTabPane.heightProperty());
                root.prefWidthProperty().bind(transactionTabPane.widthProperty());
                TransactionTableViewController controller = fXMLLoader.getController();
                if(newValue.getText().equals("Quotation Transactions")){
                    controller.initTableData(quotationList);
                }else if(newValue.getText().equals("Invoice Transactions")){
                    controller.initTableData(invoiceList);
                }else if(newValue.getText().equals("Return Transactions")){
                    controller.initTableData(returnList);
                }
                newValue.setContent(root);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        transactionTabPane.getSelectionModel().selectFirst();
    }

    private void bindCustomerInfoLabels(){
        if(customerDTO != null) {
            storeCreditLabel.textProperty().bind(initStringBinding(Bindings.isNull(customerDTO.storeCreditProperty().asObject()),
                    "", customerDTO.storeCreditProperty().asString()));
            customerClassLabel.textProperty().bind(initStringBinding(customerDTO.customerClassProperty().isNull(),
                    "", customerDTO.customerClassProperty().asString()));
            dateCreatedLabel.textProperty().bind(initStringBinding(customerDTO.dateCreatedProperty().isNull(),
                    "", customerDTO.dateCreatedProperty().asString()));
            fullNameLabel.textProperty().bind(initStringBinding(customerDTO.fullnameProperty().isNull(),
                    "", customerDTO.fullnameProperty()));
            emailLabel.textProperty().bind(initStringBinding(customerDTO.emailProperty().isNull(),
                    "", customerDTO.emailProperty()));
            phoneLabel.textProperty().bind(initStringBinding(customerDTO.phoneProperty().isNull(),
                    "", customerDTO.phoneProperty()));
        }
    }

    private StringBinding initStringBinding(ObservableBooleanValue condition, String thenVal, ObservableStringValue
            otherwiseVal){
        return Bindings.when(condition).then(thenVal).otherwise(otherwiseVal);
    }
}
