package com.taimsoft.desktopui.controllers.overview;

import com.taim.client.TransactionClient;
import com.taim.dto.PaymentDTO;
import com.taim.dto.TransactionDTO;
import com.taimsoft.desktopui.uicomponents.LiveComboBoxTableCell;
import com.taimsoft.desktopui.util.RestClientFactory;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Tjin on 8/28/2017.
 */
public class TransactionOverviewController extends OverviewController<TransactionDTO> {

    private ObservableList<TransactionDTO> transactionDTOS;
    private TransactionClient transactionClient;

    @FXML
    private TableColumn<TransactionDTO, String> dateCol;
    @FXML
    private TableColumn<TransactionDTO, String> typeCol;
    @FXML
    private TableColumn<TransactionDTO, String> idCol;
    @FXML
    private TableColumn<TransactionDTO, Double> totalCol;
    @FXML
    private TableColumn<TransactionDTO, Double> balanceCol;
    @FXML
    private TableColumn<TransactionDTO, String> statusCol;

    public TransactionOverviewController(){
        this.transactionClient = RestClientFactory.getTransactionClient();
    }

    @FXML
    @SuppressWarnings("unchecked")
    public void initialize(){
        dateCol.setCellValueFactory(new PropertyValueFactory<>("dateCreated"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("transactionType"));
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        totalCol.setCellValueFactory(new PropertyValueFactory<>("saleAmount"));
        balanceCol.setCellValueFactory((TableColumn.CellDataFeatures<TransactionDTO, Double> param) -> {
            BigDecimal roundedBalance = new BigDecimal(param.getValue().getSaleAmount());
            for(PaymentDTO payment: param.getValue().getPayments()){
                roundedBalance = roundedBalance.subtract(new BigDecimal(payment.getPaymentAmount()));
            }
            return new SimpleDoubleProperty(roundedBalance.setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue()).asObject();
        });
        statusCol.setCellValueFactory(new PropertyValueFactory<>("paymentStatus"));
        initFunctionalCols();
    }

    @Override
    public void loadData(){
         transactionDTOS = FXCollections.observableArrayList(transactionClient.getTransactionList());
         getGlobalTable().setItems(transactionDTOS);
    }

}
