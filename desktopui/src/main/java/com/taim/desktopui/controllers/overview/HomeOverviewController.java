package com.taim.desktopui.controllers.overview;

import com.jfoenix.controls.JFXComboBox;
import com.taim.client.TransactionClient;
import com.taim.desktopui.util.TransactionPanelLoader;
import com.taim.dto.PaymentDTO;
import com.taim.dto.PaymentRecordDTO;
import com.taim.dto.TransactionDTO;
import com.taim.model.PaymentRecord;
import com.taim.model.Transaction;
import com.taim.desktopui.util.RestClientFactory;
import com.taim.desktopui.util.VistaNavigator;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.DateFormatSymbols;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static com.taim.desktopui.controllers.overview.IOverviewController.SummaryLabelMode.*;

public class HomeOverviewController {
    private static final int YEAR_RANGE = 20;
    private static final DateTimeFormatter dtf = DateTimeFormat.forPattern("MMM-dd-yyyy");
    private TransactionClient transactionClient;
    private ObservableList<TransactionDTO> yearlyTransactions;
    private List<TransactionDTO> transactions;
    private Executor executor;
    private XYChart.Series<String, Double> incomeSeries;
    private XYChart.Series<String, Double> expenseSeries;
    private XYChart.Series<String, Double> quoteSeries;
    private XYChart.Series<String, Double> revenueSeries;

    @FXML
    private Label companyNameLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private Label totalQuotedLabel;
    @FXML
    private Label totalInvoiceUnpaidLabel;
    @FXML
    private Label totalInvoicePaidLabel;
    @FXML
    private Label totalStockUnpaidLabel;
    @FXML
    private Label totalStockPaidLabel;
    @FXML
    private JFXComboBox<Integer> yearComboBox;
    @FXML
    private BarChart<String, Double> incomeExpenseBarChart;
    @FXML
    private LineChart<String, Double> revenueLineChart;
    @FXML
    private SplitPane summarySplitPane;
    @FXML
    private TableView<TransactionDTO> paymentDueTable;
    @FXML
    private TableColumn<TransactionDTO, String> paymentDueActionCol;
    @FXML
    private TableColumn<TransactionDTO, Integer> paymentDueIDCol;
    @FXML
    private TableColumn<TransactionDTO, String> paymentDueDateCol;
    @FXML
    private TableColumn<TransactionDTO, String> paymentStatusCol;
    @FXML
    private TableView<TransactionDTO> deliveryDueTable;
    @FXML
    private TableColumn<TransactionDTO, String> deliveryDueActionCol;
    @FXML
    private TableColumn<TransactionDTO, Integer> deliveryDueIDCol;
    @FXML
    private TableColumn<TransactionDTO, String> deliveryDueDateCol;
    @FXML
    private TableColumn<TransactionDTO, String> deliveryStatusCol;

    public HomeOverviewController(){
        executor = Executors.newCachedThreadPool(r -> {
            Thread t = new Thread(r);
            t.setDaemon(true);
            return t;
        });
        this.transactionClient = RestClientFactory.getTransactionClient();
        this.yearlyTransactions = FXCollections.observableArrayList();
    }

    @FXML
    private void initialize(){
        paymentDueIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        paymentDueDateCol.setCellValueFactory(param -> new SimpleStringProperty(dtf.print(param.getValue().getPaymentDueDate())));
        paymentStatusCol.setCellValueFactory(param -> {
            if(param.getValue().getPaymentStatus() != null){
                return new SimpleStringProperty(param.getValue().getPaymentStatus().getValue());
            }
            return null;
        });
        paymentDueActionCol.setCellValueFactory(new PropertyValueFactory<>("action"));
        paymentDueActionCol.setCellFactory(param -> generateActionTableCell());
        deliveryDueIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        deliveryDueDateCol.setCellValueFactory(param -> new SimpleStringProperty(dtf.print(param.getValue().getDeliveryDueDate())));
        deliveryStatusCol.setCellValueFactory(param -> {
            if(param.getValue().getDeliveryStatus() != null){
                return new SimpleStringProperty(param.getValue().getDeliveryStatus().getValue());
            }
            return null;
        });
        deliveryDueActionCol.setCellValueFactory(new PropertyValueFactory<>("action"));
        deliveryDueActionCol.setCellFactory(param -> generateActionTableCell());
        companyNameLabel.textProperty().bind(VistaNavigator.getGlobalProperty().companyNameProperty());
        dateLabel.textProperty().bind(new SimpleStringProperty(DateTimeFormat.forPattern("EEEE, MMMM dd yyyy").print(DateTime.now())));
//        incomeExpenseBarChart.setAnimated(false);
        incomeSeries = new XYChart.Series<>();
        incomeSeries.setName("Income");
        expenseSeries = new XYChart.Series<>();
        expenseSeries.setName("Expense");
        quoteSeries = new XYChart.Series<>();
        quoteSeries.setName("Quotation");
        revenueSeries = new XYChart.Series<>();
        revenueSeries.setName("Revenue");
    }

    public void initTransactionData(){
        Task<List<TransactionDTO>> task = new Task<List<TransactionDTO>>() {
            @Override
            protected List<TransactionDTO> call() throws Exception {
                return transactionClient.getList();
            }
        };

        task.setOnSucceeded(event -> {
            transactions  = task.getValue();
            initActivitiesList();
            initSummaryLabel();
            int currentYear = Year.now().getValue();
            List<Integer> yearList = new ArrayList<Integer>(){{add(currentYear);}};
            for(int i = 1; i <= YEAR_RANGE; i++){
                yearList.add(currentYear - i);
            }
            yearComboBox.setItems(FXCollections.observableArrayList(yearList));
            yearComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
                yearlyTransactions.clear();
                yearlyTransactions.addAll(transactions.stream()
                        .filter(transactionDTO -> transactionDTO.getDateCreated().year().get() == new DateTime().withYear(newValue).year().get())
                        .collect(Collectors.toList()));
                initOverviewBarChart(newValue);
                initRevenueBarChart(newValue);
            });
            yearComboBox.getSelectionModel().selectFirst();
        });

        executor.execute(task);
    }

    private void initSummaryLabel() {
        bindSummaryLabel(totalQuotedLabel, QUOTED);
        bindSummaryLabel(totalInvoicePaidLabel, INVOICE_PAID);
        bindSummaryLabel(totalInvoiceUnpaidLabel, INVOICE_UNPAID);
        bindSummaryLabel(totalStockPaidLabel, BILL_PAID);
        bindSummaryLabel(totalStockUnpaidLabel, BILL_UNPAID);
    }

    private void bindSummaryLabel(Label label, IOverviewController.SummaryLabelMode mode){
        DoubleBinding numberBinding = Bindings.createDoubleBinding(() -> {
                    double totalValue = 0 ;
                    switch(mode){
                        case QUOTED:
                            for (TransactionDTO item : yearlyTransactions) {
                                if(item.getTransactionType().equals(Transaction.TransactionType.QUOTATION) && !item.isFinalized()){
                                    totalValue = totalValue + item.getSaleAmount();
                                }
                            }
                            break;
                        case INVOICE_PAID:
                            for (TransactionDTO item : yearlyTransactions) {
                                if(item.getTransactionType().equals(Transaction.TransactionType.INVOICE) &&
                                        item.getPaymentStatus().equals(Transaction.PaymentStatus.PAID)){
                                    totalValue = totalValue + item.getSaleAmount();
                                }else if(item.getTransactionType().equals(Transaction.TransactionType.INVOICE) &&
                                        item.getPaymentStatus().equals(Transaction.PaymentStatus.PARTIALLY_PAID)){
                                    for(PaymentRecordDTO payment: item.getPaymentRecords()){
                                        totalValue += payment.getAmount();
                                    }
                                }
                            }
                            break;
                        case INVOICE_UNPAID:
                            for (TransactionDTO item : yearlyTransactions) {
                                if(item.getTransactionType().equals(Transaction.TransactionType.INVOICE) &&
                                        item.getPaymentStatus().equals(Transaction.PaymentStatus.UNPAID)){
                                    totalValue = totalValue + item.getSaleAmount();
                                }else if(item.getTransactionType().equals(Transaction.TransactionType.INVOICE) &&
                                        item.getPaymentStatus().equals(Transaction.PaymentStatus.PARTIALLY_PAID)){
                                    double unpaid = 0;
                                    for(PaymentRecordDTO payment: item.getPaymentRecords()){
                                        unpaid += payment.getAmount();
                                    }
                                    totalValue += unpaid;
                                }
                            }
                            break;
                        case BILL_PAID:
                            for (TransactionDTO item : yearlyTransactions) {
                                if(item.getTransactionType().equals(Transaction.TransactionType.BILL) &&
                                        item.getPaymentStatus().equals(Transaction.PaymentStatus.PAID)){
                                    totalValue = totalValue + item.getSaleAmount();
                                }else if(item.getTransactionType().equals(Transaction.TransactionType.BILL) &&
                                        item.getPaymentStatus().equals(Transaction.PaymentStatus.PARTIALLY_PAID)){
                                    for(PaymentRecordDTO payment: item.getPaymentRecords()){
                                        totalValue += payment.getAmount();
                                    }
                                }
                            }
                            break;
                        case BILL_UNPAID:
                            for (TransactionDTO item : yearlyTransactions) {
                                if(item.getTransactionType().equals(Transaction.TransactionType.BILL) &&
                                        item.getPaymentStatus().equals(Transaction.PaymentStatus.UNPAID)){
                                    totalValue = totalValue + item.getSaleAmount();
                                }else if(item.getTransactionType().equals(Transaction.TransactionType.BILL) &&
                                        item.getPaymentStatus().equals(Transaction.PaymentStatus.PARTIALLY_PAID)){
                                    double unpaid = 0;
                                    for(PaymentRecordDTO payment: item.getPaymentRecords()){
                                        unpaid += payment.getAmount();
                                    }
                                    totalValue += unpaid;
                                }
                            }
                            break;
                        default:
                            break;
                    }

                    return totalValue ;
                },yearlyTransactions);
        label.textProperty().bind(Bindings.format("%s%.2f", "$", numberBinding));
    }

    private void initOverviewBarChart(int year){
        incomeSeries.getData().clear();
        expenseSeries.getData().clear();

        for(int i = 1; i <= 12; i++){
            String month = new DateFormatSymbols().getShortMonths()[i - 1];
            DateTime startDate = new DateTime().withYear(year).withMonthOfYear(i).withDayOfMonth(1);
            DateTime endDate = startDate.plusMonths(1).minusDays(1);
            List<TransactionDTO> monthTransactions = transactions.stream().filter(transactionDTO ->
                    !transactionDTO.getDateCreated().isBefore(startDate) && !transactionDTO.getDateCreated().isAfter(endDate))
                    .collect(Collectors.toList());

            double income = monthTransactions.stream()
                    .filter(transactionDTO -> transactionDTO.getTransactionType().equals(Transaction.TransactionType.INVOICE))
                    .mapToDouble(TransactionDTO::getSaleAmount).sum();
//            double expense = monthTransactions.stream()
//                    .filter(transactionDTO -> transactionDTO.getTransactionType().equals(Transaction.TransactionType.STOCK))
//                    .mapToDouble(TransactionDTO::getSaleAmount).sum();
            double quotation = monthTransactions.stream()
                    .filter(transactionDTO -> transactionDTO.getTransactionType().equals(Transaction.TransactionType.QUOTATION) && !transactionDTO.isFinalized())
                    .mapToDouble(TransactionDTO::getSaleAmount).sum();

            incomeSeries.getData().add(new XYChart.Data<>(month, income));
//            expenseSeries.getData().add(new XYChart.Data<>(month, expense));
            quoteSeries.getData().add(new XYChart.Data<>(month, quotation));
        }
        incomeExpenseBarChart.getData().clear();
        incomeExpenseBarChart.getData().add(quoteSeries);
        incomeExpenseBarChart.getData().add(incomeSeries);
        incomeExpenseBarChart.getData().add(expenseSeries);
        incomeExpenseBarChart.setTitle("Income Expense Graph For " + year);
    }

    private void initRevenueBarChart(int year){
        revenueSeries.getData().clear();
        for(int i = 1; i <= 12; i++){
            String month = new DateFormatSymbols().getShortMonths()[i - 1];
            DateTime startDate = new DateTime().withYear(year).withMonthOfYear(i).withDayOfMonth(1);
            DateTime endDate = startDate.plusMonths(1).minusDays(1);
            List<TransactionDTO> monthTransactions = transactions.stream().filter(transactionDTO ->
                    !transactionDTO.getDateCreated().isBefore(startDate) && !transactionDTO.getDateCreated().isAfter(endDate))
                    .collect(Collectors.toList());

            double income = monthTransactions.stream()
                    .filter(transactionDTO -> transactionDTO.getTransactionType().equals(Transaction.TransactionType.INVOICE))
                    .mapToDouble(TransactionDTO::getSaleAmount).sum();
//            double expense = monthTransactions.stream()
//                    .filter(transactionDTO -> transactionDTO.getTransactionType().equals(Transaction.TransactionType.STOCK))
//                    .mapToDouble(TransactionDTO::getSaleAmount).sum();

//            revenueSeries.getData().add(new XYChart.Data<>(month, (income - expense)));

        }

        revenueLineChart.getData().clear();
        revenueLineChart.getData().add(revenueSeries);
        revenueLineChart.setTitle("Revenue Graph For " + year);
    }

    private void initActivitiesList(){
        Task<List<TransactionDTO>> filterPayDueListTask = new Task<List<TransactionDTO>>() {
            @Override
            protected List<TransactionDTO> call() throws Exception {
                return transactions.stream()
                        .filter(transactionDTO -> !transactionDTO.getTransactionType().equals(Transaction.TransactionType.QUOTATION)
                                && transactionDTO.getPaymentDueDate().isBefore(DateTime.now())
                                && !transactionDTO.getPaymentStatus().equals(Transaction.PaymentStatus.PAID))
                        .collect(Collectors.toList());
            }
        };

        Task<List<TransactionDTO>> filterDeliveryDueListTask = new Task<List<TransactionDTO>>() {
            @Override
            protected List<TransactionDTO> call() throws Exception {
                return transactions.stream()
                        .filter(transactionDTO -> !transactionDTO.getTransactionType().equals(Transaction.TransactionType.QUOTATION)
                                && transactionDTO.getDeliveryDueDate().isBefore(DateTime.now())
                                && !transactionDTO.getDeliveryStatus().equals(Transaction.DeliveryStatus.DELIVERED))
                        .collect(Collectors.toList());
            }
        };

        filterPayDueListTask.setOnSucceeded(event -> paymentDueTable.setItems(FXCollections.observableArrayList(filterPayDueListTask.getValue())));
        filterDeliveryDueListTask.setOnSucceeded(event -> deliveryDueTable.setItems(FXCollections.observableArrayList(filterDeliveryDueListTask.getValue())));

        executor.execute(filterDeliveryDueListTask);
        executor.execute(filterPayDueListTask);
    }

    private TableCell<TransactionDTO, String> generateActionTableCell(){
        return new TableCell<TransactionDTO, String>(){
            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    JFXComboBox<String> comboBox = new JFXComboBox<>();
                    comboBox.setPromptText("SET ACTION");
                    comboBox.prefWidthProperty().bind(this.widthProperty());
                    TransactionDTO transactionDTO = getTableView().getItems().get(getIndex());
                    comboBox.setItems(FXCollections.observableArrayList("VIEW DETAILS", "EDIT"));
                    comboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
                        if(newValue.equals("VIEW DETAILS")){
                            VistaNavigator.loadDetailVista(VistaNavigator.VISTA_TRANSACTION_DETAIL, transactionDTO);
                        }else if(newValue.equals("EDIT")){
                            switch(transactionDTO.getTransactionType()) {
                                case INVOICE:
                                    TransactionPanelLoader.loadInvoice(transactionDTO);
//                                case CONTRACT:
//                                    TransactionPanelLoader.loadQuotation(transactionDTO);
//                                case STOCK:
//                                    TransactionPanelLoader.loadStock(transactionDTO);
                                default:
                                    break;
                            }
                        }
                    });
                    comboBox.setValue(item);
                    setGraphic(comboBox);
                }
            }

        };
    }

}
