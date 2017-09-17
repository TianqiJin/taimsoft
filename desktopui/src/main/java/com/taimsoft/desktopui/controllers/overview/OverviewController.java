package com.taimsoft.desktopui.controllers.overview;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * Created by Tjin on 9/5/2017.
 */
public abstract class OverviewController<T> {
    @FXML
    private TableView<T> globalTable;
    @FXML
    private TableColumn<T, String> actionCol;
    @FXML
    private TableColumn<T, Boolean> checkedCol;

    public abstract void loadData();

    public TableView<T> getGlobalTable() {
        return globalTable;
    }

    public void setGlobalTable(TableView<T> globalTable) {
        this.globalTable = globalTable;
    }

    public TableColumn<T, String> getActionCol() {
        return actionCol;
    }

    public void setActionCol(TableColumn<T, String> actionCol) {
        this.actionCol = actionCol;
    }

    public TableColumn<T, Boolean> getCheckedCol() {
        return checkedCol;
    }

    public void setCheckedCol(TableColumn<T, Boolean> checkedCol) {
        this.checkedCol = checkedCol;
    }
}
