package com.taimsoft.desktopui.controllers.overview;

import com.taim.client.TransactionClient;
import com.taim.Main.Payment;
import com.taim.Main.Transaction;
import com.taimsoft.desktopui.uicomponents.LiveComboBoxTableCell;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Tjin on 8/28/2017.
 */
public class TransactionOverviewController extends OverviewController<Transaction> {

    private List<Transaction> transactions;
    private TransactionClient transactionClient;

    @FXML
    private TableColumn<Transaction, String> dateCol;
    @FXML
    private TableColumn<Transaction, String> typeCol;
    @FXML
    private TableColumn<Transaction, String> idCol;
    @FXML
    private TableColumn<Transaction, Double> totalCol;
    @FXML
    private TableColumn<Transaction, Double> balanceCol;
    @FXML
    private TableColumn<Transaction, String> statusCol;

    @FXML
    @SuppressWarnings("unchecked")
    public void initialize(){
        dateCol.setCellValueFactory(new PropertyValueFactory<>("dateCreated"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("transactionType"));
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        totalCol.setCellValueFactory(new PropertyValueFactory<>("saleAmount"));
        balanceCol.setCellValueFactory((TableColumn.CellDataFeatures<Transaction, Double> param) -> {
            BigDecimal roundedBalance = new BigDecimal(param.getValue().getSaleAmount());
            for(Payment payment: param.getValue().getPayments()){
                roundedBalance = roundedBalance.subtract(new BigDecimal(payment.getPaymentAmount()));
            }
            return new SimpleDoubleProperty(roundedBalance.setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue()).asObject();
        });
        statusCol.setCellValueFactory(new PropertyValueFactory<>("paymentStatus"));

        getActionCol().setCellValueFactory(new PropertyValueFactory<>("action"));
        getActionCol().setCellFactory(param -> new LiveComboBoxTableCell<>(FXCollections.observableArrayList("Edit", "Delete")));
    }

    @Override
    public void loadData(){}

}
