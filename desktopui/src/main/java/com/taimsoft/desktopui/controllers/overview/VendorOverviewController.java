package com.taimsoft.desktopui.controllers.overview;

import com.taim.client.IClient;
import com.taim.client.VendorClient;
import com.taim.dto.TransactionDTO;
import com.taim.dto.VendorDTO;
import com.taim.model.Transaction;
import com.taim.model.Vendor;
import com.taimsoft.desktopui.uicomponents.LiveComboBoxTableCell;
import com.taimsoft.desktopui.util.RestClientFactory;
import com.taimsoft.desktopui.util.VistaNavigator;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import static com.taimsoft.desktopui.controllers.overview.IOverviewController.SummaryLabelMode.*;

/**
 * Created by Tjin on 8/30/2017.
 */
public class VendorOverviewController extends IOverviewController<VendorDTO> {
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

    public VendorOverviewController(){
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
        actionCol.setCellFactory(new Callback<TableColumn<VendorDTO, String>, TableCell<VendorDTO, String>>() {
            @Override
            public TableCell<VendorDTO, String> call(TableColumn<VendorDTO, String> param) {
                return new TableCell<VendorDTO, String>(){
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            ComboBox<String> comboBox = new ComboBox<>();
                            comboBox.setPromptText("SET ACTION");
                            comboBox.prefWidthProperty().bind(this.widthProperty());
                            VendorDTO vendorDTO = getTableView().getItems().get(getIndex());
                            comboBox.setItems(FXCollections.observableArrayList("VIEW DETAILS", "EDIT", "DELETE"));
                            comboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
                                if(newValue.equals("VIEW DETAILS")){
                                    VistaNavigator.loadDetailVista(VistaNavigator.VISTA_VENDOR_DETAIL, vendorDTO);
                                }else if(newValue.equals("EDIT")){

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
    public void initSearchField() {}

    @Override
    public IClient<VendorDTO> getOverviewClient(){
        return this.vendorClient;
    }

    @Override
    public void initSummaryLabel() {
        bindSummaryLabel(totalUnpaidLabel, INVOICE_UNPAID);
        bindSummaryLabel(totalPaidLabel, INVOICE_PAID);
    }

    private void bindSummaryLabel(Label label, SummaryLabelMode mode){
        DoubleBinding numberBinding = Bindings.createDoubleBinding(() -> {
                    double totalValue = 0 ;
                    switch(mode){
                        case INVOICE_PAID:
                            for (VendorDTO item : getOverviewDTOList()) {
                                for(TransactionDTO transactionDTO: item.getTransactionList()){
                                    if(transactionDTO.getTransactionType().equals(Transaction.TransactionType.STOCK) &&
                                            transactionDTO.getPaymentStatus().equals(Transaction.PaymentStatus.PAID)){
                                        totalValue += transactionDTO.getSaleAmount();
                                    }
                                }
                            }
                            break;
                        case INVOICE_UNPAID:
                            for (VendorDTO item : getOverviewDTOList()) {
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
        label.textProperty().bind(Bindings.format("%s%.2f", "$", numberBinding));
    }
}
