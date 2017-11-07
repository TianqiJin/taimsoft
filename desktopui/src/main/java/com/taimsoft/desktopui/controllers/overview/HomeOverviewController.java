package com.taimsoft.desktopui.controllers.overview;

import com.taim.client.TransactionClient;
import com.taim.dto.TransactionDTO;
import com.taim.model.Transaction;
import com.taimsoft.desktopui.util.RestClientFactory;
import com.taimsoft.desktopui.util.VistaNavigator;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.text.DateFormatSymbols;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static com.taimsoft.desktopui.controllers.overview.IOverviewController.SummaryLabelMode.*;
import static com.taimsoft.desktopui.controllers.overview.IOverviewController.SummaryLabelMode.STOCK_UNPAID;

public class HomeOverviewController {
    private static final int YEAR_RANGE = 20;
    private TransactionClient transactionClient;
    private List<TransactionDTO> transactions;
    private ObservableList<TransactionDTO> totalTransactions;
    private Executor executor;
    private XYChart.Series<String, Double> incomeSeries;
    private XYChart.Series<String, Double> expenseSeries;

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
    private ComboBox<Integer> yearComboBox;
    @FXML
    private BarChart<String, Double> incomeExpenseBarChart;
    @FXML
    private CategoryAxis monthAxis;
    @FXML
    private NumberAxis numberAxis;

    public HomeOverviewController(){
        executor = Executors.newCachedThreadPool(r -> {
            Thread t = new Thread(r);
            t.setDaemon(true);
            return t;
        });
        this.transactionClient = RestClientFactory.getTransactionClient();
    }

    @FXML
    private void initialize(){
        companyNameLabel.textProperty().bind(VistaNavigator.getGlobalProperty().companyNameProperty());
        dateLabel.textProperty().bind(new SimpleStringProperty(DateTimeFormat.forPattern("EEEE, MMMM dd yyyy").print(DateTime.now())));
        incomeExpenseBarChart.setAnimated(false);
        incomeSeries = new XYChart.Series<>();
        incomeSeries.setName("Income");
        expenseSeries = new XYChart.Series<>();
        expenseSeries.setName("Expense");
    }

    public void initTransactionData(){
        Task<List<TransactionDTO>> task = new Task<List<TransactionDTO>>() {
            @Override
            protected List<TransactionDTO> call() throws Exception {
                return transactionClient.getList();
            }
        };

        task.setOnSucceeded(event -> {
            transactions = task.getValue();
            totalTransactions = FXCollections.observableArrayList(transactions);
            initSummaryLabel();
            int currentYear = Year.now().getValue();
            List<Integer> yearList = new ArrayList<Integer>(){{add(currentYear);}};
            for(int i = 1; i <= YEAR_RANGE; i++){
                yearList.add(currentYear - i);
            }
            yearComboBox.setItems(FXCollections.observableArrayList(yearList));
            yearComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
                initOverviewBarChart(newValue);
            });
            yearComboBox.getSelectionModel().selectFirst();
        });

        executor.execute(task);
    }

    private void initSummaryLabel() {
        bindSummaryLabel(totalQuotedLabel, QUOTED);
        bindSummaryLabel(totalInvoicePaidLabel, INVOICE_PAID);
        bindSummaryLabel(totalInvoiceUnpaidLabel, INVOICE_UNPAID);
        bindSummaryLabel(totalStockPaidLabel, STOCK_PAID);
        bindSummaryLabel(totalStockUnpaidLabel, STOCK_UNPAID);
    }

    private void bindSummaryLabel(Label label, IOverviewController.SummaryLabelMode mode){
        DoubleBinding numberBinding = Bindings.createDoubleBinding(() -> {
                    double totalValue = 0 ;
                    switch(mode){
                        case QUOTED:
                            for (TransactionDTO item : transactions) {
                                if(item.getTransactionType().equals(Transaction.TransactionType.QUOTATION) && !item.isFinalized()){
                                    totalValue = totalValue + item.getSaleAmount();
                                }
                            }
                            break;
                        case INVOICE_PAID:
                            for (TransactionDTO item : transactions) {
                                if(item.getTransactionType().equals(Transaction.TransactionType.INVOICE) &&
                                        item.getPaymentStatus().equals(Transaction.PaymentStatus.PAID)){
                                    totalValue = totalValue + item.getSaleAmount();
                                }
                            }
                            break;
                        case INVOICE_UNPAID:
                            for (TransactionDTO item : transactions) {
                                if(item.getTransactionType().equals(Transaction.TransactionType.INVOICE) &&
                                        item.getPaymentStatus().equals(Transaction.PaymentStatus.UNPAID)){
                                    totalValue = totalValue + item.getSaleAmount();
                                }
                            }
                            break;
                        case STOCK_PAID:
                            for (TransactionDTO item : transactions) {
                                if(item.getTransactionType().equals(Transaction.TransactionType.STOCK) &&
                                        item.getPaymentStatus().equals(Transaction.PaymentStatus.PAID)){
                                    totalValue = totalValue + item.getSaleAmount();
                                }
                            }
                            break;
                        case STOCK_UNPAID:
                            for (TransactionDTO item : transactions) {
                                if(item.getTransactionType().equals(Transaction.TransactionType.STOCK) &&
                                        item.getPaymentStatus().equals(Transaction.PaymentStatus.UNPAID)){
                                    totalValue = totalValue + item.getSaleAmount();
                                }
                            }
                            break;
                        default:
                            break;
                    }

                    return totalValue ;
                },totalTransactions);
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
                    transactionDTO.getDateCreated().isAfter(startDate) && transactionDTO.getDateCreated().isBefore(endDate))
                    .collect(Collectors.toList());

            double income = monthTransactions.stream()
                    .filter(transactionDTO -> transactionDTO.getTransactionType().equals(Transaction.TransactionType.INVOICE))
                    .mapToDouble(TransactionDTO::getSaleAmount).sum();
            double expense = monthTransactions.stream()
                    .filter(transactionDTO -> transactionDTO.getTransactionType().equals(Transaction.TransactionType.STOCK))
                    .mapToDouble(TransactionDTO::getSaleAmount).sum();

            incomeSeries.getData().add(new XYChart.Data<>(month, income));
            expenseSeries.getData().add(new XYChart.Data<>(month, expense));
        }
        incomeExpenseBarChart.getData().clear();
        incomeExpenseBarChart.getData().add(incomeSeries);
        incomeExpenseBarChart.getData().add(expenseSeries);
    }

}
