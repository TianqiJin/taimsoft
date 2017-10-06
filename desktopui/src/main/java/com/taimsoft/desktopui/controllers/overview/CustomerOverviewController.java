package com.taimsoft.desktopui.controllers.overview;

import com.taim.client.CustomerClient;
import com.taim.dto.CustomerDTO;
import com.taim.dto.TransactionDTO;
import com.taim.model.Customer;
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

import static com.taimsoft.desktopui.controllers.overview.OverviewController.SummaryLabelMode.Quoted;


/**
 * Created by Tjin on 8/30/2017.
 */
public class CustomerOverviewController implements OverviewController<CustomerDTO>{
    private List<CustomerDTO> customerDTOS;
    private CustomerClient customerClient;
    private Executor executor;

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
    @FXML
    private Label totalQuotedLabel;
    @FXML
    private Label totalUnpaidLabel;
    @FXML
    private Label totalPaidLabel;

    public CustomerOverviewController(){
        customerClient = RestClientFactory.getCustomerClient();
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
        storeCreditCol.setCellValueFactory(new PropertyValueFactory<>("storeCredit"));
        checkedCol.setCellValueFactory(new PropertyValueFactory<>("isChecked"));
        checkedCol.setCellFactory(CheckBoxTableCell.forTableColumn(checkedCol));
        actionCol.setCellValueFactory(new PropertyValueFactory<>("action"));
        actionCol.setCellFactory(param -> new LiveComboBoxTableCell<>(FXCollections.observableArrayList("Edit", "Delete")));
    }

    @Override
    public void loadData() {
        Task<List<CustomerDTO>> customerTask = new Task<List<CustomerDTO>>() {
            @Override
            protected List<CustomerDTO> call() throws Exception {
                return customerClient.getCustomerList();
            }
        };

        customerTask.setOnSucceeded(event -> {
            customerDTOS = customerTask.getValue();
            customerTable.setItems(FXCollections.observableArrayList(customerDTOS));
            initSearchField();
        });

        executor.execute(customerTask);
    }

    @Override
    public void initSearchField() {

    }

    private void bindSummaryLabel(Label label, SummaryLabelMode mode){
        DoubleBinding numberBinding = Bindings.createDoubleBinding(() -> {
                    double totalValue = 0 ;
                    switch(mode){
                        case Quoted:
                            for (CustomerDTO item : customerTable.getItems()) {
                                for(TransactionDTO transactionDTO : item.getTransactionList()){
                                    if(transactionDTO.getTransactionType().equals(Transaction.TransactionType.QUOTATION)){
                                        totalValue += transactionDTO.getSaleAmount();
                                    }
                                }
                            }
                            break;
                        case Paid:
//                            for (CustomerDTO item : customerTable.getItems()) {
//                                for(TransactionDTO transactionDTO : item.getTransactionList()){
//                                    if(transactionDTO.getTransactionType().equals(Transaction.TransactionType.PURCHASE_ORDER)){
//                                        totalValue += transactionDTO.getSaleAmount();
//                                    }
//                                }
//                            }
//                            break;
                        case Unpaid:
//                            for (CustomerDTO item : customerTable.getItems()) {
//                                if(item.getPaymentStatus().equals(Transaction.PaymentStatus.UNPAID)){
//                                    totalValue = totalValue + item.getSaleAmount();
//                                }
//                            }
//                            break;
                        default:
                            break;
                    }

                    return totalValue ;
                },
                customerTable.getItems());
        label.textProperty().bind(Bindings.format("%.2f", numberBinding));
    }
}
