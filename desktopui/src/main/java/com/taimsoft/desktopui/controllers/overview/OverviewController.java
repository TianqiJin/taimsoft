package com.taimsoft.desktopui.controllers.overview;

import com.taimsoft.desktopui.uicomponents.LiveComboBoxTableCell;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

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

    public void initFunctionalCols(){
        checkedCol.setCellValueFactory(new PropertyValueFactory<T, Boolean>("isChecked"));
        actionCol.setCellValueFactory(new PropertyValueFactory<>("action"));
        actionCol.setCellFactory(param -> new LiveComboBoxTableCell<>(FXCollections.observableArrayList("Edit", "Delete")));
    }

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
