package com.taim.desktopui.controllers.payment;

import com.taim.desktopui.util.ButtonCell;
import com.taim.dto.PaymentDTO;
import com.taim.dto.PaymentRecordDTO;
import com.taim.dto.TransactionDTO;
import com.taim.dto.TransactionDetailDTO;
import com.taim.model.PaymentRecord;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class PaymentTransactionController implements Initializable{
    private static final DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd");
    private PaymentDTO payment;
    private Executor executor;
    private ObservableList<TransactionDTO> transactionList;

    @FXML
    private TableView<TransactionDTO> transactionTableView;
    @FXML
    private TableColumn<TransactionDTO, Number> idCol;
    @FXML
    private TableColumn<TransactionDTO, String> dateCol;
    @FXML
    private TableColumn<TransactionDTO, String> typeCol;
    @FXML
    private TableColumn<TransactionDTO, Number> amountCol;
    @FXML
    private TableColumn<TransactionDTO, Number> balanceCol;
    @FXML
    private TableColumn<TransactionDTO, Number> paymentCol;
    @FXML
    private TableColumn deleteCol;

    public PaymentTransactionController(){
        executor = Executors.newCachedThreadPool(r -> {
            Thread t = new Thread(r);
            t.setDaemon(true);
            return t;
        });
    }


    @FXML
    private void handleFindTransactionsButton(){}

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        dateCol.setCellValueFactory(param -> new SimpleStringProperty(dtf.print(param.getValue().getDateCreated())));
        typeCol.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getTransactionType().getValue()));
        amountCol.setCellValueFactory(new PropertyValueFactory<>("saleAmount"));
        balanceCol.setCellValueFactory(param -> {
            BigDecimal total = new BigDecimal(param.getValue().getSaleAmount());
            for(PaymentRecordDTO paymentRecord: param.getValue().getPaymentRecords()){
                total = total.subtract(new BigDecimal(paymentRecord.getAmount()));
            }

            return new SimpleDoubleProperty(total.setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue());
        });
        paymentCol.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Number>() {
            @Override
            public String toString(Number object) {
                return String.valueOf(object);
            }

            @Override
            public Double fromString(String string) {
                return Double.valueOf(string);
            }
        }));
        paymentCol.setOnEditCommit(event -> {
            TransactionDTO transaction = event.getTableView().getItems().get(event.getTablePosition().getRow());
            PaymentRecordDTO paymentRecordDTO = new PaymentRecordDTO();
            paymentRecordDTO.setDateCreated(DateTime.now());
            paymentRecordDTO.setDateModified(DateTime.now());
            paymentRecordDTO.setPayment(this.payment);
            paymentRecordDTO.setAmount((Double)event.getNewValue());

            transaction.getPaymentRecords().add(paymentRecordDTO);
        });
    }

    public PaymentDTO getPayment() {
        return payment;
    }

    public void setPayment(PaymentDTO payment) {
        this.payment = payment;
    }
}
