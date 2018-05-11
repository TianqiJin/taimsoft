package com.taim.desktopui.controllers.payment;

import com.jfoenix.controls.JFXCheckBox;
import com.taim.client.TransactionClient;
import com.taim.desktopui.util.AlertBuilder;
import com.taim.desktopui.util.RestClientFactory;
import com.taim.dto.CustomerDTO;
import com.taim.dto.PaymentDetailDTO;
import com.taim.dto.TransactionDTO;
import com.taim.model.Payment;
import com.taim.model.Transaction;
import com.taim.model.search.TransactionSearch;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class FindTransactionController implements Initializable {
    private static final DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd");
    private Stage stage;
    private List<TransactionDTO> transactionResultList;
    private Executor executor;
    private TransactionClient transactionClient;

    @FXML
    private TableView<TransactionDTO> transactionTableView;
    @FXML
    private TableColumn<TransactionDTO, Number> idCol;
    @FXML
    private TableColumn<TransactionDTO, String> dateCol;
    @FXML
    private TableColumn<TransactionDTO, String> typeCol;
    @FXML
    private TableColumn<TransactionDTO, String> categoryCol;
    @FXML
    private TableColumn<TransactionDTO, Number> amountCol;
    @FXML
    private TableColumn<TransactionDTO, Number> balanceCol;
    @FXML
    private TableColumn<TransactionDTO, String> nameCol;
    @FXML
    private TableColumn<TransactionDTO, Boolean> checkedCol;

    public FindTransactionController(){
        executor = Executors.newCachedThreadPool(r -> {
            Thread thread = new Thread(r);
            thread.setDaemon(true);
            return thread;
        });

        transactionClient = RestClientFactory.getTransactionClient();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        dateCol.setCellValueFactory(param -> new SimpleStringProperty(dtf.print(param.getValue().getDateCreated())));
        typeCol.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getTransactionType().getValue()));
        categoryCol.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getTransactionCategory().getValue()));
        amountCol.setCellValueFactory(new PropertyValueFactory<>("saleAmount"));
        balanceCol.setCellValueFactory(new PropertyValueFactory<>("balance"));
        nameCol.setCellValueFactory(param -> {
            TransactionDTO transactionDTO = param.getValue();
            if(transactionDTO.getCustomer() != null){
                return new SimpleStringProperty(transactionDTO.getCustomer().getFullname());
            }else{
                return new SimpleStringProperty(transactionDTO.getVendor().getFullname());
            }
        });
        checkedCol.setCellValueFactory(new PropertyValueFactory<>("checked"));
        checkedCol.setCellFactory(new Callback<TableColumn<TransactionDTO, Boolean>, TableCell<TransactionDTO, Boolean>>() {
            @Override
            public TableCell<TransactionDTO, Boolean> call(TableColumn<TransactionDTO, Boolean> param) {
                JFXCheckBox checkBox = new JFXCheckBox();
                return new TableCell<TransactionDTO, Boolean>(){
                    @Override
                    public void updateItem(Boolean item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                                TransactionDTO transactionDTO = getTableView().getItems().get(getIndex());
                                transactionDTO.setChecked(newValue);
                            });
                            setGraphic(checkBox);
                        }
                    }
                };
            }
        });

        TransactionDTO transactionDTO = new TransactionDTO();
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setFullname("test");
        transactionDTO.setDateCreated(DateTime.now());
        transactionDTO.setTransactionCategory(Transaction.TransactionCategory.OUT);
        transactionDTO.setTransactionType(Transaction.TransactionType.INVOICE);
        transactionDTO.setCustomer(customerDTO);
        transactionTableView.setItems(FXCollections.observableArrayList(new ArrayList<TransactionDTO>(){{add(transactionDTO);}}));
    }

    @FXML
    private void handleAddButton(){
        transactionTableView.getItems().forEach(t -> {
            if(t.isChecked()){
                transactionResultList.add(t);
            }
        });

        stage.close();
    }

    @FXML
    private void handleCancelButton(){
        stage.close();
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void fetchUnpaidInvoices(Payment.PaymentType type, long id, List<PaymentDetailDTO> existingPaymentDetails){
        TransactionSearch transactionSearch = new TransactionSearch();
        transactionSearch.setTransactionType(Transaction.TransactionType.INVOICE);
        if(type.equals(Payment.PaymentType.CUSTOMER_PAYMENT)){
            transactionSearch.setCustomerID(id);
        }else{
            transactionSearch.setVendorID(id);
        }
        transactionSearch.setFinalized(false);

        Task<List<TransactionDTO>> fetchTransactionsTask = new Task<List<TransactionDTO>>() {
            @Override
            protected List<TransactionDTO> call(){
                return transactionClient.getFilteredList(transactionSearch);
            }
        };

        fetchTransactionsTask.setOnSucceeded(event -> {
            List<TransactionDTO> result = fetchTransactionsTask.getValue().stream()
                    .filter(t -> isTransactionExisted(existingPaymentDetails, t.getId())).collect(Collectors.toList());
            transactionTableView.setItems(FXCollections.observableArrayList(result));
        });
        fetchTransactionsTask.setOnFailed(event -> {
            new AlertBuilder(stage)
                    .alertType(Alert.AlertType.ERROR)
                    .alertContentText("Failed to retrieve invoices.")
                    .build()
                    .showAndWait();
        });

        executor.execute(fetchTransactionsTask);
    }

    public List<TransactionDTO> getTransactionResultList() {
        return transactionResultList;
    }

    private boolean isTransactionExisted(List<PaymentDetailDTO> input, long id){
        for(PaymentDetailDTO paymentDetailDTO: input){
            if(paymentDetailDTO.getTransaction().getId() == id){
                return true;
            }
        }

        return false;
    }
}
