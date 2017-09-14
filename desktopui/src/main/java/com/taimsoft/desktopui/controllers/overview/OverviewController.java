package com.taimsoft.desktopui.controllers.overview;

import com.taim.Main.Transaction;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * Created by Tjin on 9/5/2017.
 */
public abstract class OverviewController<T> {
    @FXML
    private TableView<T> globalTableView;
    @FXML
    private TableColumn<T, String> actionCol;
    @FXML
    private TableColumn<T, Boolean> checkedCol;
    public abstract void loadData();

    public TableView<T> getGlobalTableView() {
        return globalTableView;
    }

    public void setGlobalTableView(TableView<T> globalTableView) {
        this.globalTableView = globalTableView;
    }

    public TableColumn<T, String> getActionCol() {
        return actionCol;
    }

    public void setActionCol(TableColumn<T, String> actionCol) {
        this.actionCol = actionCol;
    }
}
