package com.taimsoft.desktopui.controllers.overview;

import com.taim.client.VendorClient;
import com.taim.dto.VendorDTO;
import com.taimsoft.desktopui.util.RestClientFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Created by Tjin on 8/30/2017.
 */
public class VendorOverviewController extends OverviewController<VendorDTO>{
    private ObservableList<VendorDTO> vendorDTOS;
    private VendorClient vendorClient;

    @FXML
    private TableColumn<VendorDTO, String> nameCol;
    @FXML
    private TableColumn<VendorDTO, String> phoneCol;
    @FXML
    private TableColumn<VendorDTO, String> emailCol;

    public VendorOverviewController(){
        this.vendorClient = RestClientFactory.getVendorClient();
    }

    @FXML
    public void initialize(){
        nameCol.setCellValueFactory(new PropertyValueFactory<VendorDTO, String>("fullname"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<VendorDTO, String>("phone"));
        emailCol.setCellValueFactory(new PropertyValueFactory<VendorDTO, String>("email"));
        initFunctionalCols();
    }

    @Override
    public void loadData() {
        vendorDTOS = FXCollections.observableArrayList(vendorClient.getVendorList());
        getGlobalTable().setItems(vendorDTOS);
    }
}
