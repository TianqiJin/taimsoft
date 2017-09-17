package com.taimsoft.desktopui.controllers.overview;

import com.taim.client.StaffClient;
import com.taim.dto.StaffDTO;
import com.taim.model.Staff;
import com.taimsoft.desktopui.controllers.overview.OverviewController;
import com.taimsoft.desktopui.util.RestClientFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Created by Tjin on 8/30/2017.
 */
public class StaffOverviewController extends OverviewController<StaffDTO> {
    private ObservableList<StaffDTO> staffDTOS;
    private StaffClient staffClient;

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

    public StaffOverviewController(){
        this.staffClient = RestClientFactory.getStaffClient();
    }

    @FXML
    public void initialize(){
        nameCol.setCellValueFactory(new PropertyValueFactory<StaffDTO, String>("fullname"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<StaffDTO, String>("phone"));
        emailCol.setCellValueFactory(new PropertyValueFactory<StaffDTO, String>("email"));
        titleCol.setCellValueFactory(new PropertyValueFactory<StaffDTO, Staff.Position>("position"));
        orgCol.setCellValueFactory(param-> param.getValue().getOrganization().orgNameProperty());
        initFunctionalCols();
    }

    @Override
    public void loadData() {
        staffDTOS = FXCollections.observableArrayList(staffClient.getStaffList());
        getGlobalTable().setItems(staffDTOS);
    }
}
