package com.taimsoft.desktopui.controllers.overview;

import com.taim.client.IClient;
import com.taim.client.StaffClient;
import com.taim.dto.CustomerDTO;
import com.taim.dto.ProductDTO;
import com.taim.dto.StaffDTO;
import com.taim.dto.TransactionDTO;
import com.taim.model.Staff;
import com.taimsoft.desktopui.controllers.overview.OverviewController;
import com.taimsoft.desktopui.uicomponents.LiveComboBoxTableCell;
import com.taimsoft.desktopui.util.RestClientFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by Tjin on 8/30/2017.
 */
public class StaffOverviewController extends OverviewController<StaffDTO> {
    private StaffClient staffClient;

    @FXML
    private TableView<StaffDTO> staffTable;
    @FXML
    private TableColumn<StaffDTO, String> nameCol;
    @FXML
    private TableColumn<StaffDTO, String> phoneCol;
    @FXML
    private TableColumn<StaffDTO, String> emailCol;
    @FXML
    private TableColumn<StaffDTO, Staff.Position> titleCol;
    @FXML
    private TableColumn<StaffDTO, String> orgCol;
    @FXML
    private TableColumn<StaffDTO, String> actionCol;
    @FXML
    private TableColumn<StaffDTO, Boolean> checkedCol;

    public StaffOverviewController(){
        this.staffClient = RestClientFactory.getStaffClient();
    }

    @FXML
    public void initialize(){
        nameCol.setCellValueFactory(new PropertyValueFactory<>("fullname"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("position"));
        orgCol.setCellValueFactory(param-> param.getValue().getOrganization().orgNameProperty());
        checkedCol.setCellValueFactory(new PropertyValueFactory<>("isChecked"));
        checkedCol.setCellFactory(CheckBoxTableCell.forTableColumn(checkedCol));
        actionCol.setCellValueFactory(new PropertyValueFactory<>("action"));
        actionCol.setCellFactory(param -> {
            LiveComboBoxTableCell<StaffDTO, String> liveComboBoxTableCell = new LiveComboBoxTableCell<>(
                    FXCollections.observableArrayList("VIEW DETAILS", "EDIT", "DELETE"));
            return liveComboBoxTableCell;
        });
    }

    @Override
    public void initSearchField() {

    }

    @Override
    public IClient<StaffDTO> getOverviewClient(){
        return this.staffClient;
    }
}
