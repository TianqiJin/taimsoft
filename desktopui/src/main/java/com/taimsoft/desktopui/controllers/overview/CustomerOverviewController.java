package com.taimsoft.desktopui.controllers.overview;

import com.taim.client.CustomerClient;
import com.taim.dto.CustomerDTO;
import com.taim.model.Customer;
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
public class CustomerOverviewController implements OverviewController<CustomerDTO>{
    private ObservableList<CustomerDTO> customerDTOS;
    private CustomerClient customerClient;

    @FXML
    private TableView<CustomerDTO> customerTable;
    @FXML
    private TableColumn<CustomerDTO, String> nameCol;
    @FXML
    private TableColumn<CustomerDTO, String> phoneCol;
    @FXML
    private TableColumn<CustomerDTO, String> emailCol;
    @FXML
    private TableColumn<CustomerDTO, Double> storeCreditCol;
    @FXML
    private TableColumn<CustomerDTO, String> actionCol;
    @FXML
    private TableColumn<CustomerDTO, Boolean> checkedCol;

    public CustomerOverviewController(){
        customerClient = RestClientFactory.getCustomerClient();
    }

    @FXML
    public void initialize(){
        nameCol.setCellValueFactory(new PropertyValueFactory<>("fullname"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        storeCreditCol.setCellValueFactory(new PropertyValueFactory<>("storeCredit"));
        checkedCol.setCellValueFactory(new PropertyValueFactory<>("isChecked"));
        actionCol.setCellValueFactory(new PropertyValueFactory<>("action"));
        actionCol.setCellFactory(param -> new LiveComboBoxTableCell<>(FXCollections.observableArrayList("Edit", "Delete")));
    }

    @Override
    public void loadData() {
        customerDTOS = FXCollections.observableArrayList(customerClient.getCustomerList());
        customerTable.setItems(customerDTOS);
    }

    @Override
    public void initSearchField() {

    }
}
