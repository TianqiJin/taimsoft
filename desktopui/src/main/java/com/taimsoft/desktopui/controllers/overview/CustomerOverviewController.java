package com.taimsoft.desktopui.controllers.overview;

import com.taim.client.CustomerClient;
import com.taim.dto.CustomerDTO;
import com.taim.model.Customer;
import com.taimsoft.desktopui.util.RestClientFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;


/**
 * Created by Tjin on 8/30/2017.
 */
public class CustomerOverviewController extends OverviewController<CustomerDTO>{
    private ObservableList<CustomerDTO> customerDTOS;
    private CustomerClient customerClient;

    @FXML
    private TableColumn<CustomerDTO, String> nameCol;
    @FXML
    private TableColumn<CustomerDTO, String> phoneCol;
    @FXML
    private TableColumn<CustomerDTO, String> emailCol;
    @FXML
    private TableColumn<CustomerDTO, Double> storeCreditCol;

    public CustomerOverviewController(){
        customerClient = RestClientFactory.getCustomerClient();
    }

    @FXML
    public void initialize(){
        nameCol.setCellValueFactory(new PropertyValueFactory<CustomerDTO, String>("fullname"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<CustomerDTO, String>("phone"));
        emailCol.setCellValueFactory(new PropertyValueFactory<CustomerDTO, String>("email"));
        storeCreditCol.setCellValueFactory(new PropertyValueFactory<CustomerDTO, Double>("storeCredit"));
        initFunctionalCols();
    }

    @Override
    public void loadData() {
        customerDTOS = FXCollections.observableArrayList(customerClient.getCustomerList());
        getGlobalTable().setItems(customerDTOS);
    }
}
