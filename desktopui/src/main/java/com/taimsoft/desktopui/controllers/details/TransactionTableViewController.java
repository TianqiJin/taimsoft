package com.taimsoft.desktopui.controllers.details;

import com.taim.dto.TransactionDTO;
import com.taim.dto.TransactionDetailDTO;
import com.taim.model.Transaction;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class TransactionTableViewController {
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
    private TableColumn<TransactionDTO, Number> deliveryStatusCol;
    @FXML
    private TableColumn<TransactionDTO, Number> paymentStatusCol;
    @FXML
    private TableColumn<TransactionDTO, String> finalizedCol;

    @FXML
    public void initialize(){
        transactionIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        dateCreatedCol.setCellValueFactory(new PropertyValueFactory<>("dateCreated"));
        staffCol.setCellValueFactory(param -> param.getValue().getStaff().fullnameProperty());
        cuVeCol.setCellValueFactory(param -> {
            if(param.getValue().getTransactionType().equals(Transaction.TransactionType.INVOICE)){
                return param.getValue().getVendor().fullnameProperty();
            }else{
                return param.getValue().getCustomer().fullnameProperty();
            }
        });
        saleAmountCol.setCellValueFactory(new PropertyValueFactory<>("saleAmount"));
        deliveryStatusCol.setCellValueFactory(new PropertyValueFactory<>("deliveryStatus"));
        paymentStatusCol.setCellValueFactory(new PropertyValueFactory<>("paymentStatus"));
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
