package com.taimsoft.desktopui.controllers.details;

import com.taim.dto.TransactionDTO;
import com.taim.model.Transaction;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;

public class TransactionTableViewController {
    private static final DateTimeFormatter dtf = DateTimeFormat.forPattern("MMM-dd-yyyy");

    @FXML
    private TableView<TransactionDTO> transactionTableView;
    @FXML
    private TableColumn<TransactionDTO, String> transactionIDCol;
    @FXML
    private TableColumn<TransactionDTO, String> dateCreatedCol;
    @FXML
    private TableColumn<TransactionDTO, String> staffCol;
    @FXML
    private TableColumn<TransactionDTO, String> cuVeCol;
    @FXML
    private TableColumn<TransactionDTO, Number> saleAmountCol;
    @FXML
    private TableColumn<TransactionDTO, String> deliveryStatusCol;
    @FXML
    private TableColumn<TransactionDTO, String> paymentStatusCol;
    @FXML
    private TableColumn<TransactionDTO, String> finalizedCol;

    @FXML
    public void initialize(){
        transactionIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        dateCreatedCol.setCellValueFactory(param -> new SimpleStringProperty(dtf.print(param.getValue().getDateCreated())));
        staffCol.setCellValueFactory(param -> param.getValue().getStaff().fullnameProperty());
        cuVeCol.setCellValueFactory(param -> {
            if(param.getValue().getTransactionType().equals(Transaction.TransactionType.INVOICE)){
                if(param.getValue().getVendor() != null){
                    return param.getValue().getVendor().fullnameProperty();
                }

            }else{
                if(param.getValue().getCustomer() != null){
                    return param.getValue().getCustomer().fullnameProperty();
                }
            }
            return new SimpleStringProperty();
        });
        saleAmountCol.setCellValueFactory(new PropertyValueFactory<>("saleAmount"));
        deliveryStatusCol.setCellValueFactory(param -> {
            if (param.getValue().getDeliveryStatus() != null) {
                return new SimpleStringProperty(param.getValue().getDeliveryStatus().getStatus().getValue());
            }
            return null;
        });
        paymentStatusCol.setCellValueFactory(param -> {
            if(param.getValue().getPaymentStatus() != null){
                return new SimpleStringProperty(param.getValue().getPaymentStatus().getValue());
            }

            return null;
        });
        finalizedCol.setCellValueFactory(param -> {
            if(param.getValue().isFinalized()){
                return new SimpleStringProperty("YES");
            }else{
                return new SimpleStringProperty("NO");
            }
        });
    }

    public void initTableData(List<TransactionDTO> transactionDTOList){
        transactionTableView.setItems(FXCollections.observableArrayList(transactionDTOList));
    }

}
