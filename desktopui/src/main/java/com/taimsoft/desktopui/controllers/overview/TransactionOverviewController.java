package com.taimsoft.desktopui.controllers.overview;

import com.taim.client.IClient;
import com.taim.client.TransactionClient;
import com.taim.dto.PaymentDTO;
import com.taim.dto.TransactionDTO;
import com.taim.dto.VendorDTO;
import com.taim.model.Transaction;
import com.taimsoft.desktopui.uicomponents.LiveComboBoxTableCell;
import com.taimsoft.desktopui.util.RestClientFactory;
import com.taimsoft.desktopui.util.TransactionPanelLoader;
import com.taimsoft.desktopui.util.VistaNavigator;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static com.taimsoft.desktopui.controllers.overview.OverviewController.SummaryLabelMode.*;

/**
 * Created by Tjin on 8/28/2017.
 */
public class TransactionOverviewController extends OverviewController<TransactionDTO> {
    private TransactionClient transactionClient;

    @FXML
    private TableColumn<TransactionDTO, String> dateCol;
    @FXML
    private TableColumn<TransactionDTO, String> typeCol;
    @FXML
    private TableColumn<TransactionDTO, String> idCol;
    @FXML
    private TableColumn<TransactionDTO, Double> totalCol;
    @FXML
    private TableColumn<TransactionDTO, Double> balanceCol;
    @FXML
    private TableColumn<TransactionDTO, String> statusCol;
    @FXML
    private TableColumn<TransactionDTO, String> actionCol;
    @FXML
    private TableColumn<TransactionDTO, Boolean> checkedCol;
    @FXML
    private SplitPane summarySplitPane;
    @FXML
    private Label totalQuotedLabel;
    @FXML
    private Label totalUnpaidLabel;
    @FXML
    private Label totalPaidLabel;
    @FXML
    private TextField searchField;
    @FXML
    private DatePicker fromDatePicker;
    @FXML
    private DatePicker toDatePicker;
    @FXML
    private ComboBox<Transaction.TransactionType> transactionTypeComboBox;
    @FXML
    private ComboBox<Transaction.TransactionType> createNewTransactionComboBox;

    public TransactionOverviewController(){
        super();
        this.transactionClient = RestClientFactory.getTransactionClient();
    }

    @FXML
    @SuppressWarnings("unchecked")
    public void initialize(){
        dateCol.setCellValueFactory(new PropertyValueFactory<>("dateCreated"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("transactionType"));
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        totalCol.setCellValueFactory(new PropertyValueFactory<>("saleAmount"));
        balanceCol.setCellValueFactory((TableColumn.CellDataFeatures<TransactionDTO, Double> param) -> {
            BigDecimal roundedBalance = new BigDecimal(param.getValue().getSaleAmount());
            for(PaymentDTO payment: param.getValue().getPayments()){
                roundedBalance = roundedBalance.subtract(new BigDecimal(payment.getPaymentAmount()));
            }
            return new SimpleDoubleProperty(roundedBalance.setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue()).asObject();
        });
        statusCol.setCellValueFactory(new PropertyValueFactory<>("paymentStatus"));
        checkedCol.setCellValueFactory(new PropertyValueFactory<>("isChecked"));
        checkedCol.setCellFactory(CheckBoxTableCell.forTableColumn(checkedCol));
        actionCol.setCellValueFactory(new PropertyValueFactory<>("action"));
        actionCol.setCellFactory(new Callback<TableColumn<TransactionDTO, String>, TableCell<TransactionDTO, String>>() {
            @Override
            public TableCell<TransactionDTO, String> call(TableColumn<TransactionDTO, String> param) {
                return new TableCell<TransactionDTO, String>(){
                    ComboBox<String> comboBox = new ComboBox<>();
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            comboBox.setPromptText("SET ACTION");
                            comboBox.prefWidthProperty().bind(this.widthProperty());
                            TransactionDTO transactionDTO = getTableView().getItems().get(getIndex());
                            switch (transactionDTO.getTransactionType()){
                                case STOCK:
                                    comboBox.setItems(FXCollections.observableArrayList("VIEW DETAILS", "EDIT", "PRINT", "DELETE"));
                                    break;
                                case RETURN:
                                    comboBox.setItems(FXCollections.observableArrayList("VIEW DETAILS", "EDIT", "PRINT", "DELETE"));
                                    break;
                                case INVOICE:
                                    comboBox.setItems(FXCollections.observableArrayList("VIEW DETAILS", "EDIT", "PRINT", "DELETE"));
                                    break;
                                case QUOTATION:
                                    comboBox.setItems(FXCollections.observableArrayList("VIEW DETAILS", "CONVERT TO INVOICE", "EDIT", "PRINT", "DELETE"));
                                    break;
                                default:
                                    break;
                            }
                            comboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
                                if(newValue.equals("VIEW DETAILS")){
                                    VistaNavigator.loadDetailVista(VistaNavigator.VISTA_TRANSACTION_DETAIL, transactionDTO);
                                }
                            });
                            comboBox.setValue(item);
                            setGraphic(comboBox);
                        }
                    }

                };
            }
        });

        bindSummaryLabel(totalQuotedLabel, Quoted);
        bindSummaryLabel(totalPaidLabel, Paid);
        bindSummaryLabel(totalUnpaidLabel, Unpaid);

        createNewTransactionComboBox.setItems(FXCollections.observableArrayList(Transaction.TransactionType.values()));
        createNewTransactionComboBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                switch (createNewTransactionComboBox.getSelectionModel().getSelectedItem()){
                    case QUOTATION:
                        TransactionDTO transactionDTO = TransactionPanelLoader.loadQuotation(null,null);
//                        if (transactionDTO!=null){
//                            transactionDTOS.add(transactionDTO);
//                        }
                        break;
                    case INVOICE:
                        break;
                    case STOCK:
                        break;
                    case RETURN:
                        break;
                }
            }
        });
        transactionTypeComboBox.setItems(FXCollections.observableArrayList(Transaction.TransactionType.values()));
        transactionTypeComboBox.setConverter(new StringConverter<Transaction.TransactionType>() {
            @Override
            public String toString(Transaction.TransactionType object) {
                if(object == null){
                    return null;
                }
                return object.toString();
            }

            @Override
            public Transaction.TransactionType fromString(String string) {
                if(string == null){
                    return null;
                }
                return Transaction.TransactionType.valueOf(string);
            }
        });
    }

    @FXML
    private void handleFilterTransaction(){
        LocalDate fromDate = fromDatePicker.getValue();
        LocalDate toDate = toDatePicker.getValue();
        Transaction.TransactionType type = transactionTypeComboBox.getValue();

        ObservableList<TransactionDTO> subList = FXCollections.observableArrayList(getOverviewDTOList());
        if(type != null){
            subList = subList.stream().filter(transaction -> transaction.getTransactionType().equals(type)).collect(Collectors.toCollection(FXCollections::observableArrayList));
        }
        if(fromDate != null){
            subList = subList.stream().filter(transaction -> transaction.getDateCreated().isAfter(new DateTime(fromDate))).collect(Collectors.toCollection(FXCollections::observableArrayList));
        }
        if(toDate != null){
            subList = subList.stream().filter(transaction -> transaction.getDateCreated().isBefore(new DateTime(toDate))).collect(Collectors.toCollection(FXCollections::observableArrayList));
        }

        getOverviewTable().setItems(subList);
    }

    @FXML
    private void handleClearFilter(){
        transactionTypeComboBox.getEditor().clear();
        fromDatePicker.getEditor().clear();
        toDatePicker.getEditor().clear();
        getOverviewTable().setItems(FXCollections.observableArrayList(getOverviewDTOList()));
    }

    @Override
    public void initSearchField() {
        FilteredList<TransactionDTO> filteredData = new FilteredList<TransactionDTO>(FXCollections.observableArrayList(getOverviewDTOList()), p->true);
        searchField.textProperty().addListener((observable,oldVal,newVal)->{
            filteredData.setPredicate(transactionDTO -> {
                if (newVal == null || newVal.isEmpty()){
                    return true;
                }
                String lowerCase = newVal.toLowerCase();
                if(transactionDTO.getDateCreated().toString().toLowerCase().contains(lowerCase)){
                    return true;
                }else if(transactionDTO.getTransactionType().name().toLowerCase().contains(lowerCase)){
                    return true;
                }else if(String.valueOf(transactionDTO.getId()).equals(lowerCase)){
                    return true;
                }else if(String.valueOf(transactionDTO.getSaleAmount()).contains(lowerCase)){
                    return true;
                }else if(transactionDTO.getPaymentStatus().getValue().toLowerCase().contains(lowerCase)){
                    return true;
                }else{
                    BigDecimal roundedBalance = new BigDecimal(transactionDTO.getSaleAmount());
                    for(PaymentDTO payment: transactionDTO.getPayments()){
                        roundedBalance = roundedBalance.subtract(new BigDecimal(payment.getPaymentAmount()));
                    }
                    if(roundedBalance.setScale(2, BigDecimal.ROUND_HALF_EVEN).toString().contains(lowerCase)){
                        return true;
                    }
                }

                return false;
            });
            getOverviewTable().setItems(filteredData);
        });
    }

    @Override
    public IClient<TransactionDTO> getOverviewClient(){
        return this.transactionClient;
    }

    private void bindSummaryLabel(Label label, SummaryLabelMode mode){
        DoubleBinding numberBinding = Bindings.createDoubleBinding(() -> {
                    double totalValue = 0 ;
                    switch(mode){
                        case Quoted:
                            for (TransactionDTO item : getOverviewTable().getItems()) {
                                if(item.getTransactionType().equals(Transaction.TransactionType.QUOTATION) && !item.isFinalized()){
                                    totalValue = totalValue + item.getSaleAmount();
                                }
                            }
                            break;
                        case Paid:
                            for (TransactionDTO item : getOverviewTable().getItems()) {
                                if(item.getTransactionType().equals(Transaction.TransactionType.INVOICE) &&
                                        item.getPaymentStatus().equals(Transaction.PaymentStatus.PAID)){
                                    totalValue = totalValue + item.getSaleAmount();
                                }
                            }
                            break;
                        case Unpaid:
                            for (TransactionDTO item : getOverviewTable().getItems()) {
                                if(item.getTransactionType().equals(Transaction.TransactionType.INVOICE) &&
                                        item.getPaymentStatus().equals(Transaction.PaymentStatus.UNPAID)){
                                    totalValue = totalValue + item.getSaleAmount();
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
