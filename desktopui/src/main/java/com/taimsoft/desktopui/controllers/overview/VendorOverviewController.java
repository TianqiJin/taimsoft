package com.taimsoft.desktopui.controllers.overview;

import com.taim.client.VendorClient;
import com.taim.dto.TransactionDTO;
import com.taim.dto.VendorDTO;
import com.taim.model.Transaction;
import com.taimsoft.desktopui.uicomponents.LiveComboBoxTableCell;
import com.taimsoft.desktopui.util.RestClientFactory;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import static com.taimsoft.desktopui.controllers.overview.OverviewController.SummaryLabelMode.*;

/**
 * Created by Tjin on 8/30/2017.
 */
public class VendorOverviewController implements OverviewController<VendorDTO>{
    private List<VendorDTO> vendorDTOS;
    private VendorClient vendorClient;
    private Executor executor;

    @FXML
    private TableView<VendorDTO> vendorTable;
    @FXML
    private TableColumn<VendorDTO, String> nameCol;
    @FXML
    private TableColumn<VendorDTO, String> phoneCol;
    @FXML
    private TableColumn<VendorDTO, String> emailCol;
    @FXML
    private TableColumn<VendorDTO, String> actionCol;
    @FXML
    private TableColumn<VendorDTO, Boolean> checkedCol;
    @FXML
    private Label unpaidLabel;
    @FXML
    private Label paidLabel;

    public VendorOverviewController(){
        this.vendorClient = RestClientFactory.getVendorClient();
        this.executor = Executors.newCachedThreadPool(r -> {
            Thread t = new Thread(r);
            t.setDaemon(true);
            return t;
        });
    }

    @FXML
    public void initialize(){
        nameCol.setCellValueFactory(new PropertyValueFactory<>("fullname"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        checkedCol.setCellValueFactory(new PropertyValueFactory<>("isChecked"));
        checkedCol.setCellFactory(CheckBoxTableCell.forTableColumn(checkedCol));
        actionCol.setCellValueFactory(new PropertyValueFactory<>("action"));
        actionCol.setCellFactory(param -> new LiveComboBoxTableCell<>(FXCollections.observableArrayList("Edit", "Delete")));
        bindSummaryLabel(unpaidLabel, Unpaid);
        bindSummaryLabel(paidLabel, Paid);
    }

    @Override
    public void loadData() {
        Task<List<VendorDTO>> vendorTask = new Task<List<VendorDTO>>() {
            @Override
            protected List<VendorDTO> call() throws Exception {
                return vendorClient.getVendorList();
            }
        };

        vendorTask.setOnSucceeded(event -> {
            vendorDTOS = vendorTask.getValue();
            vendorTable.setItems(FXCollections.observableArrayList(vendorDTOS));
            initSearchField();
        });

        executor.execute(vendorTask);
    }

    @Override
    public void initSearchField() {

    }

    private void bindSummaryLabel(Label label, SummaryLabelMode mode){
        DoubleBinding numberBinding = Bindings.createDoubleBinding(() -> {
                    double totalValue = 0 ;
                    switch(mode){
                        case Paid:
                            for (VendorDTO item : vendorTable.getItems()) {
                                for(TransactionDTO transactionDTO: item.getTransactionList()){
                                    if(transactionDTO.getPaymentStatus().equals(Transaction.PaymentStatus.PAID)){
                                        totalValue += transactionDTO.getSaleAmount();
                                    }
                                }
                            }
                            break;
                        case Unpaid:
                            for (VendorDTO item : vendorTable.getItems()) {
                                for(TransactionDTO transactionDTO: item.getTransactionList()){
                                    if(transactionDTO.getPaymentStatus().equals(Transaction.PaymentStatus.UNPAID)){
                                        totalValue += transactionDTO.getSaleAmount();
                                    }
                                }
                            }
                        default:
                            break;
                    }

                    return totalValue ;
                },
                vendorTable.getItems());
        label.textProperty().bind(Bindings.format("%.2f", numberBinding));
    }
}
