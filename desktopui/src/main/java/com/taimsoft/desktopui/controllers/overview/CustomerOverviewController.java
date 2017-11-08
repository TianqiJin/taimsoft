package com.taimsoft.desktopui.controllers.overview;

import com.taim.client.CustomerClient;
import com.taim.client.IClient;
import com.taim.dto.CustomerDTO;
import com.taim.dto.TransactionDTO;
import com.taim.model.Transaction;
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


/**
 * Created by Tjin on 8/30/2017.
 */
public class CustomerOverviewController extends IOverviewController<CustomerDTO> {
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
    private SplitPane summarySplitPane;
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
        actionCol.setCellFactory(new Callback<TableColumn<CustomerDTO, String>, TableCell<CustomerDTO, String>>() {
            @Override
            public TableCell<CustomerDTO, String> call(TableColumn<CustomerDTO, String> param) {
                return new TableCell<CustomerDTO, String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            ComboBox<String> comboBox = new ComboBox<>();
                            comboBox.setPromptText("SET ACTION");
                            comboBox.prefWidthProperty().bind(this.widthProperty());
                            CustomerDTO customerDTO = getTableView().getItems().get(getIndex());
                            comboBox.setItems(FXCollections.observableArrayList("VIEW DETAILS", "EDIT", "DELETE"));
                            comboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
                                if (newValue.equals("VIEW DETAILS")) {
                                    VistaNavigator.loadDetailVista(VistaNavigator.VISTA_CUSTOMER_DETAIL, customerDTO);
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
    public IClient<CustomerDTO> getOverviewClient(){
        return this.customerClient;
    }

    @Override
    public void initSummaryLabel() {
        bindSummaryLabel(totalPaidLabel, SummaryLabelMode.INVOICE_PAID);
        bindSummaryLabel(totalUnpaidLabel, SummaryLabelMode.INVOICE_UNPAID);
        bindSummaryLabel(totalQuotedLabel, SummaryLabelMode.QUOTED);
    }

    private void bindSummaryLabel(Label label, SummaryLabelMode mode){
        DoubleBinding numberBinding = Bindings.createDoubleBinding(() -> {
                    double totalValue = 0 ;
                    switch(mode){
                        case QUOTED:
                            for (CustomerDTO item : getOverviewDTOList()) {
                                for(TransactionDTO transactionDTO : item.getTransactionList()){
                                    if(transactionDTO.getTransactionType().equals(Transaction.TransactionType.QUOTATION)
                                            && !transactionDTO.isFinalized()){
                                        totalValue = totalValue + transactionDTO.getSaleAmount();
                                    }
                                }
                            }
                            break;
                        case INVOICE_PAID:
                            for (CustomerDTO item : getOverviewDTOList()) {
                                for(TransactionDTO transactionDTO : item.getTransactionList()){
                                    if(transactionDTO.getTransactionType().equals(Transaction.TransactionType.INVOICE) &&
                                            transactionDTO.getPaymentStatus().equals(Transaction.PaymentStatus.PAID)){
                                        totalValue = totalValue + transactionDTO.getSaleAmount();
                                    }
                                }
                            }
                            break;
                        case INVOICE_UNPAID:
                            for (CustomerDTO item : getOverviewDTOList()) {
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
        label.textProperty().bind(Bindings.format("%s%.2f", "$", numberBinding));
    }
}
