package com.taim.desktopui.controllers.overview;


import com.taim.client.IClient;
import com.taim.desktopui.util.RestClientFactory;
import com.taim.dto.TransactionDTO;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by Tjin on 9/5/2017.
 */
public abstract class IOverviewController<T>{
    private static final Logger logger = LoggerFactory.getLogger(IOverviewController.class);
    private Executor executor;
    private List<T> overviewDTOList;
    private boolean fetchTransactions;
    private List<TransactionDTO> transactionList;
    private Stage stage;

    @FXML
    private TableView<T> overviewTable;
    @FXML
    private AnchorPane rootPane;

    enum SummaryLabelMode{
        QUOTED,
        INVOICE_UNPAID,
        INVOICE_PAID,
        BILL_UNPAID,
        BILL_PAID
    }

    public IOverviewController(){
        fetchTransactions = false;
        transactionList = new ArrayList<>();
        executor = Executors.newCachedThreadPool(r -> {
            Thread t = new Thread(r);
            t.setDaemon(true);
            return t;
        });
    }

    public abstract void initSearchField();

    public abstract IClient<T> getOverviewClient();

    public abstract void initSummaryLabel();

    public void initOverviewData(IClient<T> client){
        Task<List<T>> overviewTask = new Task<List<T>>() {
            @Override
            protected List<T> call() throws Exception {
                return client.getList();
            }
        };
        overviewTask.setOnSucceeded(event -> {
            overviewDTOList = overviewTask.getValue();
            overviewTable.setItems(FXCollections.observableArrayList(overviewDTOList));
            initSearchField();
            initSummaryLabel();
        });

        overviewTask.setOnFailed(event -> {
            logger.error(overviewTask.getException().getMessage(), overviewTask.getException());
        });

        if(fetchTransactions){
            Task<List<TransactionDTO>> transactionTask = new Task<List<TransactionDTO>>() {
                @Override
                protected List<TransactionDTO> call() throws Exception {
                    return RestClientFactory.getTransactionClient().getList();
                }
            };

            transactionTask.setOnSucceeded(event -> {
                transactionList = transactionTask.getValue();
                executor.execute(overviewTask);
            });
            transactionTask.setOnFailed(event -> logger.error(transactionTask.getException().getMessage(), transactionTask.getException()));
            executor.execute(transactionTask);
        }else{
            executor.execute(overviewTask);
        }
    }

    public void setStage(Stage stage){
        this.stage = stage;
    }

    public Stage getStage() {
        return stage;
    }

    public List<T> getOverviewDTOList() {
        return overviewDTOList;
    }

    public void setOverviewDTOList(List<T> overviewDTOList) {
        this.overviewDTOList = overviewDTOList;
    }

    public Executor getExecutor() {
        return executor;
    }

    public TableView<T> getOverviewTable() {
        return overviewTable;
    }

    public boolean isFetchTransactions() {
        return fetchTransactions;
    }

    public void setFetchTransactions(boolean fetchTransactions) {
        this.fetchTransactions = fetchTransactions;
    }

    public List<TransactionDTO> getTransactionList() {
        return transactionList;
    }

    public void setTransactionList(List<TransactionDTO> transactionList) {
        this.transactionList = transactionList;
    }

    public AnchorPane getRootPane() {
        return rootPane;
    }
}
