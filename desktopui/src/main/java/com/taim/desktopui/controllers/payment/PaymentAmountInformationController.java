package com.taim.desktopui.controllers.payment;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.taim.client.CustomerClient;
import com.taim.desktopui.util.AlertBuilder;
import com.taim.desktopui.util.AutoCompleteComboBoxListener;
import com.taim.desktopui.util.RestClientFactory;
import com.taim.dto.CustomerDTO;
import com.taim.dto.PaymentDTO;
import com.taim.model.Payment;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.fxmisc.easybind.EasyBind;

import java.net.URL;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PaymentAmountInformationController implements Initializable{

    enum SearchType{
        BY_NAME("By Name"),
        BY_PHONE("By Phone"),
        BY_EMAIL("By Email");

        private String value;

        SearchType(String vvalue){
            this.value = vvalue;
        }

        public String getValue() {
            return value;
        }

        public static SearchType getSearchType(String value){
            for (SearchType st : SearchType.values()){
                if (value.equalsIgnoreCase(st.getValue())){
                    return st;
                }
            }
            return null;
        }
    }

    private CustomerDTO customer;
    private ObjectProperty<PaymentDTO> payment;
    private CustomerClient customerClient;
    private List<CustomerDTO> customerList;
    private List<String> customerNameList;
    private List<String> customerEmailList;
    private List<String> customerPhoneList;
    private Stage dialogStage;
    private Executor executor;


    @FXML
    private JFXComboBox<String> customerComboBox;
    @FXML
    private JFXComboBox<String> customerSearchTypeComboBox;
    @FXML
    private JFXTextField paymentAmountTextField;
    @FXML
    private Label paymentTypeLabel;

    public PaymentAmountInformationController(){
        customerClient = RestClientFactory.getCustomerClient();
        executor = Executors.newCachedThreadPool(r -> {
            Thread t = new Thread(r);
            t.setDaemon(true);
            return t;
        });
        payment = new SimpleObjectProperty<>();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        paymentTypeLabel.textProperty().bind(EasyBind.monadic(payment).
                flatMap(PaymentDTO::paymentTypeProperty).selectProperty(t -> new SimpleStringProperty(t.getValue())).orElse(""));
        List<String> allSearchTypes = Stream.of(SearchType.values()).map(SearchType::getValue).collect(Collectors.toList());
        customerSearchTypeComboBox.setItems(FXCollections.observableList(allSearchTypes));
        customerSearchTypeComboBox.getSelectionModel().selectFirst();
        customerSearchTypeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null && customerList != null){
                switch(SearchType.getSearchType(newValue)){
                    case BY_NAME:
                        customerComboBox.setItems(FXCollections.observableArrayList(customerNameList));
                        break;
                    case BY_EMAIL:
                        customerComboBox.setItems(FXCollections.observableArrayList(customerEmailList));
                        break;
                    case BY_PHONE:
                        customerComboBox.setItems(FXCollections.observableArrayList(customerPhoneList));
                        break;
                    default:
                        break;
                }
            }
        });

        customerComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null){
                Optional<CustomerDTO> tmpCustomer;
                switch(SearchType.getSearchType(customerSearchTypeComboBox.getValue())){
                    case BY_NAME:
                        tmpCustomer = customerList.stream().filter(customerDTO -> customerDTO.getFullname().equals(newValue)).findAny();
                        if(tmpCustomer.isPresent()){
                            this.customer = tmpCustomer.get();
                        }else{
                            new AlertBuilder(dialogStage).alertType(Alert.AlertType.ERROR).alertContentText("Unable to find selected customer.").build().showAndWait();
                        }
                        break;
                    case BY_EMAIL:
                        tmpCustomer = customerList.stream().filter(customerDTO -> customerDTO.getEmail().equals(newValue)).findAny();
                        if(tmpCustomer.isPresent()){
                            this.customer = tmpCustomer.get();
                        }else{
                            new AlertBuilder(dialogStage).alertType(Alert.AlertType.ERROR).alertContentText("Unable to find selected customer.").build().showAndWait();
                        }
                        break;
                    case BY_PHONE:
                        tmpCustomer = customerList.stream().filter(customerDTO -> customerDTO.getPhone().equals(newValue)).findAny();
                        if(tmpCustomer.isPresent()){
                            this.customer = tmpCustomer.get();
                        }else{
                            new AlertBuilder(dialogStage).alertType(Alert.AlertType.ERROR).alertContentText("Unable to find selected customer.").build().showAndWait();
                        }
                        break;
                    default:
                        break;
                }
            }
        });

        //Fetch customer list
        fetchCustomers();
        new AutoCompleteComboBoxListener<>(customerComboBox);
    }

    private void fetchCustomers(){
        Task<List<CustomerDTO>>  customerDTOsTask = new Task<List<CustomerDTO>>() {
            @Override
            protected List<CustomerDTO> call() throws Exception {
                return customerClient.getList();
            }
        };

        customerDTOsTask.setOnSucceeded(event -> {
            customerList = customerDTOsTask.getValue();
            customerNameList = customerList.stream().map(CustomerDTO::getFullname).collect(Collectors.toList());
            customerEmailList = customerList.stream().map(CustomerDTO::getEmail).collect(Collectors.toList());
            customerPhoneList = customerList.stream().map(CustomerDTO::getPhone).collect(Collectors.toList());
        });

        customerDTOsTask.setOnFailed(event -> {
            new AlertBuilder(dialogStage)
                    .alertType(Alert.AlertType.ERROR)
                    .alertContentText("Failed to fetch all customers.")
                    .build()
                    .showAndWait();
        });

        executor.execute(customerDTOsTask);
    }


    public CustomerDTO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDTO customer) {
        this.customer = customer;
    }

    public PaymentDTO getPayment() {
        return payment.get();
    }

    public ObjectProperty<PaymentDTO> paymentProperty() {
        return payment;
    }

    public void setPayment(PaymentDTO payment) {
        this.payment.set(payment);
    }

    public Stage getDialogStage() {
        return dialogStage;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
}
