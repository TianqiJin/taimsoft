package com.taimsoft.desktopui.controllers.overview;

import com.taim.model.Payment;
import com.taim.model.Transaction;
import com.taimsoft.desktopui.controllers.overview.OverviewController;
import com.taimsoft.desktopui.uicomponents.LiveComboBoxTableCell;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import org.hibernate.annotations.Check;
import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tjin on 8/28/2017.
 */
public class TransactionOverviewController implements OverviewController {

    private List<Transaction> transactions;
    private List<BooleanProperty> selectedRowList = new ArrayList<>();

    @FXML
    private TableView<Transaction> transactionTableView;
    @FXML
    private TableColumn<Transaction, String> dateTableColumn;
    @FXML
    private TableColumn<Transaction, String> typeTableColumn;
    @FXML
    private TableColumn<Transaction, String> idTableColumn;
    @FXML
    private TableColumn<Transaction, Double> totalTableColumn;
//    @FXML
//    private TableColumn<Transaction, Double> balanceTableColumn;
    @FXML
    private TableColumn<Transaction, String> statusTableColumn;
    @FXML
    private TableColumn<Transaction, String> actionTableColumn;


    @FXML
    @SuppressWarnings("unchecked")
    public void initialize(){
        dateTableColumn.setCellValueFactory(new PropertyValueFactory<>("dateCreated"));
        typeTableColumn.setCellValueFactory(new PropertyValueFactory<>("transactionType"));
        idTableColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        totalTableColumn.setCellValueFactory(new PropertyValueFactory<>("saleAmount"));
//        balanceTableColumn.setCellValueFactory((TableColumn.CellDataFeatures<Transaction, Double> param) -> {
//            BigDecimal roundedBalance = new BigDecimal(param.getValue().getSaleAmount());
//            for(Payment payment: param.getValue().getPayments()){
//                roundedBalance = roundedBalance.subtract(new BigDecimal(payment.getPaymentAmount()));
//            }
//
//            return new SimpleDoubleProperty(roundedBalance.setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue()).asObject();
//        });
        statusTableColumn.setCellValueFactory(new PropertyValueFactory<>("paymentStatus"));

        actionTableColumn.setCellValueFactory(new PropertyValueFactory<>("action"));
        actionTableColumn.setCellFactory(param -> new LiveComboBoxTableCell<>(FXCollections.observableArrayList("Edit", "Delete")));


    }

    @Override
    public void loadData(){
        Transaction transaction = new Transaction();
        transaction.setDateCreated(DateTime.now());
        transaction.setTransactionType(Transaction.TransactionType.SELL);
        transaction.setId(1);
        transaction.setSaleAmount(123);
        transaction.setPaymentStatus(Transaction.PaymentStatus.UNPAID);
        transactions = new ArrayList<>();
        transactions.add(transaction);
        transactionTableView.setItems(FXCollections.observableArrayList(transactions));
    }

    @FXML
    public void testButton(){
        System.out.println(transactions.get(0).isSelected());
    }

}
