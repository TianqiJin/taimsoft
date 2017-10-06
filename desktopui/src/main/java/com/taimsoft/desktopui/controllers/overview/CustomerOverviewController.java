package com.taimsoft.desktopui.controllers.overview;

import com.sun.org.apache.xpath.internal.operations.Quo;
import com.taim.client.CustomerClient;
import com.taim.client.IClient;
import com.taim.dto.CustomerDTO;
import com.taim.dto.ProductDTO;
import com.taim.dto.TransactionDTO;
import com.taim.model.Customer;
import com.taim.model.Transaction;
import com.taimsoft.desktopui.uicomponents.LiveComboBoxTableCell;
import com.taimsoft.desktopui.util.RestClientFactory;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;

import static com.taimsoft.desktopui.controllers.overview.OverviewController.SummaryLabelMode.Paid;
import static com.taimsoft.desktopui.controllers.overview.OverviewController.SummaryLabelMode.Quoted;
import static com.taimsoft.desktopui.controllers.overview.OverviewController.SummaryLabelMode.Unpaid;


/**
 * Created by Tjin on 8/30/2017.
 */
public class CustomerOverviewController extends OverviewController<CustomerDTO>{
    private CustomerClient customerClient;

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
        actionCol.setCellFactory(param -> {
            LiveComboBoxTableCell<CustomerDTO, String> liveComboBoxTableCell = new LiveComboBoxTableCell<>(
                    FXCollections.observableArrayList("VIEW DETAILS", "EDIT", "DELETE"));
            return liveComboBoxTableCell;
        });

        bindSummaryLabel(totalPaidLabel, Paid);
        bindSummaryLabel(totalUnpaidLabel, Unpaid);
        bindSummaryLabel(totalQuotedLabel, Quoted);
    }

    @Override
    public void initSearchField() {

    }

    @Override
    public IClient<CustomerDTO> getOverviewClient(){
        return this.customerClient;
    }

    private void bindSummaryLabel(Label label, SummaryLabelMode mode){
        DoubleBinding numberBinding = Bindings.createDoubleBinding(() -> {
                    double totalValue = 0 ;
                    switch(mode){
                        case Quoted:
                            for (CustomerDTO item : getOverviewTable().getItems()) {
                                for(TransactionDTO transactionDTO : item.getTransactionList()){
                                    if(transactionDTO.getTransactionType().equals(Transaction.TransactionType.QUOTATION)
                                            && !transactionDTO.isFinalized()){
                                        totalValue = totalValue + transactionDTO.getSaleAmount();
                                    }
                                }
                            }
                            break;
                        case Paid:
                            for (CustomerDTO item : getOverviewTable().getItems()) {
                                for(TransactionDTO transactionDTO : item.getTransactionList()){
                                    if(transactionDTO.getTransactionType().equals(Transaction.TransactionType.INVOICE) &&
                                            transactionDTO.getPaymentStatus().equals(Transaction.PaymentStatus.PAID)){
                                        totalValue = totalValue + transactionDTO.getSaleAmount();
                                    }
                                }
                            }
                            break;
                        case Unpaid:
                            for (CustomerDTO item : getOverviewTable().getItems()) {
                                for(TransactionDTO transactionDTO : item.getTransactionList()){
                                    if(transactionDTO.getTransactionType().equals(Transaction.TransactionType.INVOICE) &&
                                            transactionDTO.getPaymentStatus().equals(Transaction.PaymentStatus.UNPAID)){
                                        totalValue = totalValue + transactionDTO.getSaleAmount();
                                    }
                                }
                            }
                            break;
                        default:
                            break;
                    }

                    return totalValue ;
                },
                getOverviewTable().getItems());
        label.textProperty().bind(Bindings.format("%.2f", numberBinding));
    }
}
