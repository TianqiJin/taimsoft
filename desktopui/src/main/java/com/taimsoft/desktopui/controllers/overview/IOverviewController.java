package com.taimsoft.desktopui.controllers.overview;


import com.taim.client.IClient;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Tjin on 9/5/2017.
 */
public abstract class IOverviewController<T>{
    private Executor executor;
    private List<T> overviewDTOList;
    @FXML
    private TableView<T> overviewTable;

    enum SummaryLabelMode{
        QUOTED,
        INVOICE_UNPAID,
        INVOICE_PAID,
        STOCK_UNPAID,
        STOCK_PAID,
        RETURN_UNPAID,
        RETURN_PAID;
    }

    public IOverviewController(){
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
        Task<List<T>> task = new Task<List<T>>() {
            @Override
            protected List<T> call() throws Exception {
                return client.getList();
            }
        };

        task.setOnSucceeded(event -> {
            overviewDTOList = task.getValue();
            overviewTable.setItems(FXCollections.observableArrayList(overviewDTOList));
            initSearchField();
            initSummaryLabel();
        });

        executor.execute(task);
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
}
