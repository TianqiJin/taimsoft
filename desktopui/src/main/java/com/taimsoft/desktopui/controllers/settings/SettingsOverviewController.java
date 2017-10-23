package com.taimsoft.desktopui.controllers.settings;

import com.taim.client.PropertyClient;
import com.taim.dto.PropertyDTO;
import com.taim.model.Property;
import com.taimsoft.desktopui.util.RestClientFactory;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SettingsOverviewController {
    private Executor executor;
    private PropertyClient propertyClient;
    private PropertyDTO property;

    @FXML
    private TableView<PropertyDTO.CustomerClassDTO> customerClassTable;
    @FXML
    private TableColumn<PropertyDTO.CustomerClassDTO, String> customerClassCol;
    @FXML
    private TableColumn<PropertyDTO.CustomerClassDTO, Integer> discountCol;
    @FXML
    private TableColumn<PropertyDTO.CustomerClassDTO, String> actionCol;
    @FXML
    private TextField companyNameField;
    @FXML
    private TextField productWarnLimitField;
    @FXML
    private TextField gstNumField;
    @FXML
    private TextField gstRateField;
    @FXML
    private TextField pstRateField;

    @FXML
    public void initialize(){
        customerClassCol.setCellValueFactory(new PropertyValueFactory<>("customerClassName"));
        discountCol.setCellValueFactory(new PropertyValueFactory<>("customerDiscount"));
        actionCol.setCellValueFactory(new PropertyValueFactory<>("action"));
        actionCol.setCellFactory(new Callback<TableColumn<PropertyDTO.CustomerClassDTO, String>, TableCell<PropertyDTO.CustomerClassDTO, String>>() {
            @Override
            public TableCell<PropertyDTO.CustomerClassDTO, String> call(TableColumn<PropertyDTO.CustomerClassDTO, String> param) {
                return new TableCell<PropertyDTO.CustomerClassDTO, String>(){
                    ComboBox<String> comboBox = new ComboBox<>();
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            comboBox.setPromptText("SET ACTION");
                            comboBox.prefWidthProperty().bind(this.widthProperty());
                            PropertyDTO.CustomerClassDTO customerClassDTO = getTableView().getItems().get(getIndex());
                            comboBox.setItems(FXCollections.observableArrayList("EDIT", "DELETE"));
                            comboBox.setValue(item);
                            setGraphic(comboBox);
                        }
                    }
                };
            }
        });
    }

    public SettingsOverviewController(){
        this.propertyClient = RestClientFactory.getPropertyClient();
        this.executor = Executors.newCachedThreadPool(r -> {
            Thread t = new Thread(r);
            t.setDaemon(true);
            return t;
        });
    }

    public void initPropertyData(){
        Task<List<PropertyDTO>> task = new Task<List<PropertyDTO>>() {
            @Override
            protected List<PropertyDTO> call() throws Exception {
                return propertyClient.getList();
            }
        };
        task.setOnSucceeded(event -> {
            if(task.getValue().size() != 0){
                this.property = task.getValue().get(0);
                customerClassTable.setItems(FXCollections.observableArrayList(this.property.getCustomerClasses()));
                initGeneralInfoTextFields();
            }
        });

        executor.execute(task);
    }

    private void initGeneralInfoTextFields(){
        companyNameField.setText(this.property.getCompanyName());
        productWarnLimitField.setText(String.valueOf(this.property.getProductWarnLimit()));
        gstNumField.setText(this.property.getGstNumber());
        gstRateField.setText(String.valueOf(this.property.getGstRate()));
        pstRateField.setText(String.valueOf(this.property.getPstRate()));
    }
}
