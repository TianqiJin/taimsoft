package com.taimsoft.desktopui.controllers.overview;

import com.taim.client.VendorClient;
import com.taim.dto.VendorDTO;
import com.taimsoft.desktopui.uicomponents.LiveComboBoxTableCell;
import com.taimsoft.desktopui.util.RestClientFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Created by Tjin on 8/30/2017.
 */
public class VendorOverviewController implements OverviewController<VendorDTO>{
    private ObservableList<VendorDTO> vendorDTOS;
    private VendorClient vendorClient;

    @FXML
    private TableView<VendorDTO> vendorTable;
    @FXML
    private TableColumn<VendorDTO, String> nameCol;
    @FXML
    private TableColumn<VendorDTO, String> phoneCol;
    @FXML
    private TableColumn<VendorDTO, String> emailCol;
    @FXML
    private TableColumn<VendorDTO, String> statusCol;
    @FXML
    private TableColumn<VendorDTO, String> actionCol;
    @FXML
    private TableColumn<VendorDTO, Boolean> checkedCol;

    public VendorOverviewController(){
        this.vendorClient = RestClientFactory.getVendorClient();
    }

    @FXML
    public void initialize(){
        nameCol.setCellValueFactory(new PropertyValueFactory<>("fullname"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        checkedCol.setCellValueFactory(new PropertyValueFactory<>("isChecked"));
        actionCol.setCellValueFactory(new PropertyValueFactory<>("action"));
        actionCol.setCellFactory(param -> new LiveComboBoxTableCell<>(FXCollections.observableArrayList("Edit", "Delete")));
    }

    @Override
    public void loadData() {
        vendorDTOS = FXCollections.observableArrayList(vendorClient.getVendorList());
        vendorTable.setItems(vendorDTOS);
    }

    @Override
    public void initSearchField() {

    }
}
