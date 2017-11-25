package com.taimsoft.desktopui.controllers.overview;

import com.taim.client.IClient;
import com.taim.client.StaffClient;
import com.taim.dto.OrganizationDTO;
import com.taim.dto.StaffDTO;
import com.taim.model.Staff;
import com.taimsoft.desktopui.controllers.edit.CustomerEditDialogController;
import com.taimsoft.desktopui.controllers.edit.StaffEditDialogController;
import com.taimsoft.desktopui.uicomponents.LiveComboBoxTableCell;
import com.taimsoft.desktopui.util.RestClientFactory;
import com.taimsoft.desktopui.util.TransactionPanelLoader;
import com.taimsoft.desktopui.util.VistaNavigator;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import org.joda.time.DateTime;

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
    private TableColumn<StaffDTO, String> titleCol;
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
        titleCol.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getPosition().getValue()));
        orgCol.setCellValueFactory(param-> {
            if(param.getValue().getOrganization() != null){
                return param.getValue().getOrganization().orgNameProperty();
            }else{
                return null;
            }
        });
        checkedCol.setCellValueFactory(new PropertyValueFactory<>("isChecked"));
        checkedCol.setCellFactory(CheckBoxTableCell.forTableColumn(checkedCol));
        actionCol.setCellValueFactory(new PropertyValueFactory<>("action"));
        actionCol.setCellFactory(new Callback<TableColumn<StaffDTO, String>, TableCell<StaffDTO, String>>() {
            @Override
            public TableCell<StaffDTO, String> call(TableColumn<StaffDTO, String> param) {
                return new TableCell<StaffDTO, String>(){
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            ComboBox<String> comboBox = new ComboBox<>();
                            comboBox.setPromptText("SET ACTION");
                            comboBox.prefWidthProperty().bind(this.widthProperty());
                            StaffDTO staffDTO = getTableView().getItems().get(getIndex());
                            comboBox.setItems(FXCollections.observableArrayList("VIEW DETAILS", "EDIT", "DELETE"));
                            comboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
                                if(newValue.equals("VIEW DETAILS")){
                                    VistaNavigator.loadDetailVista(VistaNavigator.VISTA_STAFF_DETAIL, staffDTO);
                                }else if(newValue.equals("EDIT")){
                                    StaffEditDialogController controller = TransactionPanelLoader.showStaffEditor(staffDTO);
                                    if(controller != null && controller.isOkClicked()){
                                        getTableView().getItems().set(getIndex(), controller.getStaff());
                                    }
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
    public void initSearchField() {
        FilteredList<StaffDTO> filteredData = new FilteredList<>(FXCollections.observableArrayList(getOverviewDTOList()), p->true);
        VistaNavigator.getRootLayoutController().getSearchField().textProperty().addListener((observable,oldVal,newVal)->{
            filteredData.setPredicate(staffDTO -> {
                if (newVal == null || newVal.isEmpty()){
                    return true;
                }
                String lowerCase = newVal.toLowerCase();
                if(staffDTO.getFullname().toLowerCase().contains(lowerCase)){
                    return true;
                }else if(staffDTO.getPhone().toLowerCase().contains(lowerCase)){
                    return true;
                }else if(staffDTO.getEmail().toLowerCase().contains(lowerCase)){
                    return true;
                }else if(staffDTO.getPosition().name().toLowerCase().contains(lowerCase)){
                    return true;
                }else if(staffDTO.getOrganization() != null &&
                        staffDTO.getOrganization().getOrgName().toLowerCase().contains(lowerCase)){
                    return true;
                }

                return false;
            });
            getOverviewTable().setItems(filteredData);
        });
    }

    @Override
    public void initSummaryLabel() {}

    @Override
    public IClient<StaffDTO> getOverviewClient(){
        return this.staffClient;
    }

    @FXML
    public void handleAddStaff(){
        StaffDTO newStaff = new StaffDTO();
        newStaff.setDateCreated(DateTime.now());
        newStaff.setDateModified(DateTime.now());
        newStaff.setOrganization(new OrganizationDTO());
        newStaff.getOrganization().setDateModified(DateTime.now());
        newStaff.getOrganization().setDateCreated(DateTime.now());
        StaffEditDialogController controller = TransactionPanelLoader.showStaffEditor(newStaff);
        if(controller != null && controller.isOkClicked()){
            getOverviewTable().getItems().add(controller.getStaff());
        }
    }
}
