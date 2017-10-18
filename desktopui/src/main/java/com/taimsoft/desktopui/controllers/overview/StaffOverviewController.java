package com.taimsoft.desktopui.controllers.overview;

import com.taim.client.IClient;
import com.taim.client.StaffClient;
import com.taim.dto.StaffDTO;
import com.taim.model.Staff;
import com.taimsoft.desktopui.uicomponents.LiveComboBoxTableCell;
import com.taimsoft.desktopui.util.RestClientFactory;
import com.taimsoft.desktopui.util.VistaNavigator;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

/**
 * Created by Tjin on 8/30/2017.
 */
public class StaffOverviewController extends IOverviewController<StaffDTO> {
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
    @FXML
    private TableColumn<StaffDTO, String> actionCol;
    @FXML
    private TableColumn<StaffDTO, Boolean> checkedCol;
    @FXML
    private SplitPane summarySplitPane;

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
        actionCol.setCellFactory(new Callback<TableColumn<StaffDTO, String>, TableCell<StaffDTO, String>>() {
            @Override
            public TableCell<StaffDTO, String> call(TableColumn<StaffDTO, String> param) {
                return new TableCell<StaffDTO, String>(){
                    ComboBox<String> comboBox = new ComboBox<>();
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            comboBox.setPromptText("SET ACTION");
                            comboBox.prefWidthProperty().bind(this.widthProperty());
                            StaffDTO staffDTO = getTableView().getItems().get(getIndex());
                            comboBox.setItems(FXCollections.observableArrayList("VIEW DETAILS", "EDIT", "DELETE"));
                            comboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
                                if(newValue.equals("VIEW DETAILS")){
                                    VistaNavigator.loadDetailVista(VistaNavigator.VISTA_STAFF_DETAIL, staffDTO);
                                }
                            });
                            comboBox.setValue(item);
                            setGraphic(comboBox);
                        }
                    }
                };
            }
        });
    }

    @Override
    public void initSearchField() {}

    @Override
    public void initSummaryLabel() {}

    @Override
    public IClient<StaffDTO> getOverviewClient(){
        return this.staffClient;
    }
}
