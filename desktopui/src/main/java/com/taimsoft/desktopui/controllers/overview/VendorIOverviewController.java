package com.taimsoft.desktopui.controllers.overview;

import com.taim.client.IClient;
import com.taim.client.VendorClient;
import com.taim.dto.TransactionDTO;
import com.taim.dto.VendorDTO;
import com.taim.model.Transaction;
import com.taimsoft.desktopui.uicomponents.LiveComboBoxTableCell;
import com.taimsoft.desktopui.util.RestClientFactory;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;

import static com.taimsoft.desktopui.controllers.overview.IOverviewController.SummaryLabelMode.*;

/**
 * Created by Tjin on 8/30/2017.
 */
public class VendorIOverviewController extends IOverviewController<VendorDTO> {
    private VendorClient vendorClient;

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
    private SplitPane summarySplitPane;
    @FXML
    private Label totalUnpaidLabel;
    @FXML
    private Label totalPaidLabel;

    public VendorIOverviewController(){
        this.vendorClient = RestClientFactory.getVendorClient();
    }

    @FXML
    public void initialize(){
        nameCol.setCellValueFactory(new PropertyValueFactory<>("fullname"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        checkedCol.setCellValueFactory(new PropertyValueFactory<>("isChecked"));
        checkedCol.setCellFactory(CheckBoxTableCell.forTableColumn(checkedCol));
        actionCol.setCellValueFactory(new PropertyValueFactory<>("action"));
        actionCol.setCellFactory(param -> {
            LiveComboBoxTableCell<VendorDTO, String> liveComboBoxTableCell = new LiveComboBoxTableCell<>(
                    FXCollections.observableArrayList("VIEW DETAILS", "EDIT", "DELETE"));
            return liveComboBoxTableCell;
        });
        bindSummaryLabel(totalUnpaidLabel, Unpaid);
        bindSummaryLabel(totalPaidLabel, Paid);
    }

    @Override
    public void initSearchField() {

    }

    @Override
    public IClient<VendorDTO> getOverviewClient(){
        return this.vendorClient;
    }

    private void bindSummaryLabel(Label label, SummaryLabelMode mode){
        DoubleBinding numberBinding = Bindings.createDoubleBinding(() -> {
                    double totalValue = 0 ;
                    switch(mode){
                        case Paid:
                            for (VendorDTO item : getOverviewTable().getItems()) {
                                for(TransactionDTO transactionDTO: item.getTransactionList()){
                                    if(transactionDTO.getTransactionType().equals(Transaction.TransactionType.STOCK) &&
                                            transactionDTO.getPaymentStatus().equals(Transaction.PaymentStatus.PAID)){
                                        totalValue += transactionDTO.getSaleAmount();
                                    }
                                }
                            }
                            break;
                        case Unpaid:
                            for (VendorDTO item : getOverviewTable().getItems()) {
                                for(TransactionDTO transactionDTO: item.getTransactionList()){
                                    if(transactionDTO.getTransactionType().equals(Transaction.TransactionType.STOCK) &&
                                            transactionDTO.getPaymentStatus().equals(Transaction.PaymentStatus.UNPAID)){
                                        totalValue += transactionDTO.getSaleAmount();
                                    }
                                }
                            }
                        default:
                            break;
                    }

                    return totalValue ;
                },
                getOverviewTable().getItems());
        label.textProperty().bind(Bindings.format("%.2f", numberBinding));
    }
}
