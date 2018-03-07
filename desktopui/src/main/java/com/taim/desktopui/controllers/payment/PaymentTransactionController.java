package com.taim.desktopui.controllers.payment;

import com.taim.dto.PaymentDTO;
import com.taim.dto.TransactionDTO;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class PaymentTransactionController {
    @FXML
    private TableView<TransactionDTO> transactionTableView;
    @FXML
    private TableColumn<TransactionDTO, Integer> idCol;
    @FXML
    private TableColumn<TransactionDTO, String> dateCol;
    @FXML
    private TableColumn<TransactionDTO, String> typeCol;
    @FXML
    private TableColumn<TransactionDTO, Double> amountCol;
    @FXML
    private TableColumn<TransactionDTO, Double> balanceCol;
    @FXML
    private TableColumn<TransactionDTO, Double> paymentCol;
    @FXML
    private TableColumn deleteCol;

    @FXML
    private void initialize(){}

    @FXML
    private void handleFindTransactionsButton(){}
}
