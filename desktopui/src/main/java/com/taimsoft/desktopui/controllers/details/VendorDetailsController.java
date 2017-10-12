package com.taimsoft.desktopui.controllers.details;

import com.taim.dto.CustomerDTO;
import com.taim.dto.TransactionDTO;
import com.taim.dto.VendorDTO;
import com.taim.model.Transaction;
import com.taim.model.Vendor;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableStringValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class VendorDetailsController implements IDetailController<VendorDTO> {
    private VendorDTO vendorDTO;
    private List<TransactionDTO> stockList;

    @FXML
    private Label fullNameLabel;
    @FXML
    private Label phoneLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label dateCreatedLabel;
    @FXML
    private ComboBox<String> actionComboBox;
    @FXML
    private TabPane transactionTabPane;


    public VendorDetailsController(){}

    @FXML
    public void initialize(){
        actionComboBox.setItems(FXCollections.observableArrayList("EDIT", "DELETE"));
    }

    @Override
    public void initDetailData(VendorDTO obj) {
        this.vendorDTO = obj;
        stockList = this.vendorDTO.getTransactionList().stream().filter(t -> t.getTransactionType().equals(Transaction.TransactionType.STOCK)).collect(Collectors.toList());
        initTransactionTabPane();
        bindVendorInfoLabels();
    }

    private void initTransactionTabPane(){
        transactionTabPane.getSelectionModel().clearSelection();
        transactionTabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {
                FXMLLoader fXMLLoader = new FXMLLoader();
                AnchorPane root = fXMLLoader.load(this.getClass().getResource("/fxml/details/TransactionTableView.fxml").openStream());
                root.prefHeightProperty().bind(transactionTabPane.heightProperty());
                root.prefWidthProperty().bind(transactionTabPane.widthProperty());
                TransactionTableViewController controller = fXMLLoader.getController();
                if(newValue.getText().equals("Stock Transactions")){
                    controller.initTableData(stockList);
                }
                newValue.setContent(root);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        transactionTabPane.getSelectionModel().selectFirst();
    }

    private void bindVendorInfoLabels(){
        if(vendorDTO != null) {
            dateCreatedLabel.textProperty().bind(initStringBinding(vendorDTO.dateCreatedProperty().isNull(),
                    "", vendorDTO.dateCreatedProperty().asString()));
            fullNameLabel.textProperty().bind(initStringBinding(vendorDTO.fullnameProperty().isNull(),
                    "", vendorDTO.fullnameProperty()));
            emailLabel.textProperty().bind(initStringBinding(vendorDTO.emailProperty().isNull(),
                    "", vendorDTO.emailProperty()));
            phoneLabel.textProperty().bind(initStringBinding(vendorDTO.phoneProperty().isNull(),
                    "", vendorDTO.phoneProperty()));
        }
    }

    private StringBinding initStringBinding(ObservableBooleanValue condition, String thenVal, ObservableStringValue
            otherwiseVal){
        return Bindings.when(condition).then(thenVal).otherwise(otherwiseVal);
    }
}
