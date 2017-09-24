package com.taimsoft.desktopui.controllers.overview;

import com.taim.client.TransactionClient;
import com.taim.dto.PaymentDTO;
import com.taim.dto.TransactionDTO;
import com.taimsoft.desktopui.uicomponents.LiveComboBoxTableCell;
import com.taimsoft.desktopui.util.RestClientFactory;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by Tjin on 8/28/2017.
 */
public class TransactionOverviewController extends OverviewController<TransactionDTO> {

    private ObservableList<TransactionDTO> transactionDTOS;
    private TransactionClient transactionClient;
    private Executor executor;

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
    @FXML
    private Label totalQuotedLabel;
    @FXML
    private Label totalUnpaidLabel;
    @FXML
    private Label totalPaidLabel;

    public TransactionOverviewController(){
        this.transactionClient = RestClientFactory.getTransactionClient();
        this.executor = Executors.newCachedThreadPool(r->{
            Thread t = new Thread();
            t.setDaemon(true);
            return t;
        });
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
        Task<List<TransactionDTO>> transactionTask = new Task<List<TransactionDTO>>() {
            @Override
            protected List<TransactionDTO> call() throws Exception {
                return transactionClient.getTransactionList();
            }
        };

        transactionTask.setOnSucceeded(event -> getGlobalTable().setItems(
                FXCollections.observableArrayList(transactionTask.getValue())));

        executor.execute(transactionTask);
    }

    private void bindLabel(Label label){
        DoubleBinding numberBinding = Bindings.createDoubleBinding(() -> {
                    double totalValue = 0 ;
                    for (TransactionDTO item : this.getGlobalTable().getItems()) {
                        totalValue = totalValue + item.getSaleAmount();
                    }
                    return totalValue ;
        },
        this.getGlobalTable().getItems());
        label.textProperty().bind(Bindings.format("%.2f", numberBinding));
    }

}
