package com.taim.desktopui.controllers.overview;

import com.taim.client.IClient;
import com.taim.client.TransactionClient;
import com.taim.desktopui.util.TransactionPanelLoader;
import com.taim.dto.PaymentDTO;
import com.taim.dto.TransactionDTO;
import com.taim.model.DeliveryStatus;
import com.taim.model.Transaction;
import com.taim.desktopui.util.DateUtils;
import com.taim.desktopui.util.RestClientFactory;
import com.taim.desktopui.util.VistaNavigator;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.stream.Collectors;

import static com.taim.desktopui.controllers.overview.IOverviewController.SummaryLabelMode.*;

/**
 * Created by Tjin on 8/28/2017.
 */
public class TransactionOverviewController extends IOverviewController<TransactionDTO> {
    private TransactionClient transactionClient;
    private static final DateTimeFormatter dtf = DateTimeFormat.forPattern("MMM-dd-yyyy");

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
    private TableColumn<TransactionDTO, String> paymentStatusCol;
    @FXML
    private TableColumn<TransactionDTO, String> deliveryStatusCol;
    @FXML
    private TableColumn<TransactionDTO, String> actionCol;
//    @FXML
//    private TableColumn<TransactionDTO, Boolean> checkedCol;
    @FXML
    private TableColumn<TransactionDTO, String> finalizedCol;
    @FXML
    private SplitPane summarySplitPane;
    @FXML
    private Label totalQuotedLabel;
    @FXML
    private Label totalInvoiceUnpaidLabel;
    @FXML
    private Label totalInvoicePaidLabel;
    @FXML
    private Label totalStockUnpaidLabel;
    @FXML
    private Label totalStockPaidLabel;
    @FXML
    private DatePicker fromDatePicker;
    @FXML
    private DatePicker toDatePicker;
    @FXML
    private ComboBox<Transaction.TransactionType> transactionTypeComboBox;
    @FXML
    private CheckBox paymentOverDueCheckBox;
    @FXML
    private CheckBox deliveryOverDueCheckBox;
    @FXML
    private CheckBox finalizedCheckBox;
    @FXML
    private ComboBox<Transaction.TransactionType> createNewTransactionComboBox;

    public TransactionOverviewController(){
        super();
        this.transactionClient = RestClientFactory.getTransactionClient();
    }

    @FXML
    @SuppressWarnings("unchecked")
    public void initialize(){
        dateCol.setCellValueFactory(param -> new SimpleStringProperty(dtf.print(param.getValue().getDateCreated())));
        typeCol.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getTransactionType().getValue()));
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        totalCol.setCellValueFactory(new PropertyValueFactory<>("saleAmount"));
        balanceCol.setCellValueFactory((TableColumn.CellDataFeatures<TransactionDTO, Double> param) -> {
            BigDecimal roundedBalance = new BigDecimal(param.getValue().getSaleAmount());
            for(PaymentDTO payment: param.getValue().getPayments()){
                roundedBalance = roundedBalance.subtract(new BigDecimal(payment.getPaymentAmount()));
            }
            return new SimpleDoubleProperty(roundedBalance.setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue()).asObject();
        });
        paymentStatusCol.setCellValueFactory(param -> {
            if(param.getValue().getPaymentStatus() != null){
                return new SimpleStringProperty(param.getValue().getPaymentStatus().getValue());
            }

            return null;
        });
        deliveryStatusCol.setCellValueFactory(param -> {
            if(param.getValue().getDeliveryStatus() != null){
                return new SimpleStringProperty(param.getValue().getDeliveryStatus().getStatus().getValue());
            }
            return null;
        });
//        checkedCol.setCellValueFactory(new PropertyValueFactory<>("isChecked"));
//        checkedCol.setCellFactory(CheckBoxTableCell.forTableColumn(checkedCol));
        finalizedCol.setCellValueFactory(param -> {
            if(param.getValue().isFinalized()){
                return new SimpleStringProperty("YES");
            }else{
                return new SimpleStringProperty("NO");
            }
        });
        actionCol.setCellValueFactory(new PropertyValueFactory<>("action"));
        actionCol.setCellFactory(new Callback<TableColumn<TransactionDTO, String>, TableCell<TransactionDTO, String>>() {
            @Override
            public TableCell<TransactionDTO, String> call(TableColumn<TransactionDTO, String> param) {
                return new TableCell<TransactionDTO, String>(){
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            ComboBox<String> comboBox = new ComboBox<>();
                            comboBox.setPromptText("SET ACTION");
                            comboBox.prefWidthProperty().bind(this.widthProperty());
                            TransactionDTO transactionDTO = getTableView().getItems().get(getIndex());
                            switch (transactionDTO.getTransactionType()){
                                case STOCK:
                                    if(transactionDTO.isFinalized()){
                                        comboBox.setItems(FXCollections.observableArrayList("VIEW DETAILS", "PRINT"));
                                    }else{
                                        comboBox.setItems(FXCollections.observableArrayList("VIEW DETAILS", "EDIT", "PRINT"));
                                    }
                                    break;
                                case RETURN:
                                    if(transactionDTO.isFinalized()){
                                        comboBox.setItems(FXCollections.observableArrayList("VIEW DETAILS", "PRINT"));
                                    }else{
                                        comboBox.setItems(FXCollections.observableArrayList("VIEW DETAILS", "EDIT", "PRINT"));
                                    }
                                    break;
                                case INVOICE:
                                    if(transactionDTO.isFinalized()){
                                        comboBox.setItems(FXCollections.observableArrayList("VIEW DETAILS", "FILE RETURN","PRINT"));
                                    }else{
                                        comboBox.setItems(FXCollections.observableArrayList("VIEW DETAILS", "EDIT", "FILE RETURN","PRINT"));
                                    }
                                    break;
                                case QUOTATION:
                                    if(transactionDTO.isFinalized()){
                                        comboBox.setItems(FXCollections.observableArrayList("VIEW DETAILS", "CONVERT TO INVOICE", "PRINT"));
                                    }else{
                                        comboBox.setItems(FXCollections.observableArrayList("VIEW DETAILS", "CONVERT TO INVOICE", "EDIT", "PRINT"));
                                    }

                                    break;
                                default:
                                    break;
                            }
                            comboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
                                if(newValue != null){
                                    if(newValue.equals("VIEW DETAILS")){
                                        VistaNavigator.loadDetailVista(VistaNavigator.VISTA_TRANSACTION_DETAIL, transactionDTO);
                                    }else if(newValue.equals("PRINT")){
                                        TransactionPanelLoader.showPrintTransactionDialog(transactionDTO);
                                    }else if(newValue.equalsIgnoreCase("EDIT")){
                                        getOverviewTable().getItems().remove(getIndex());
                                        switch (transactionDTO.getTransactionType()){
                                            case QUOTATION:
                                                getOverviewTable().getItems().add(TransactionPanelLoader.loadQuotation(transactionDTO));
                                                break;
                                            case INVOICE:
                                                getOverviewTable().getItems().add(TransactionPanelLoader.loadInvoice(transactionDTO));
                                                break;
                                            case RETURN:
                                                getOverviewTable().getItems().add(TransactionPanelLoader.loadReturn(transactionDTO));
                                                break;
                                            case STOCK:
                                                getOverviewTable().getItems().add(TransactionPanelLoader.loadStock(transactionDTO));
                                                break;
                                        }
                                    }else if(newValue.equalsIgnoreCase("CONVERT TO INVOICE") && transactionDTO.getTransactionType()== Transaction.TransactionType.QUOTATION){
                                        getOverviewTable().getItems().remove(getIndex());
                                        getOverviewTable().getItems().add(TransactionPanelLoader.loadInvoice(transactionDTO));
                                    } else if(newValue.equalsIgnoreCase("FILE RETURN") && transactionDTO.getTransactionType()== Transaction.TransactionType.INVOICE){
                                        getOverviewTable().getItems().remove(getIndex());
                                        getOverviewTable().getItems().add(TransactionPanelLoader.loadReturn(transactionDTO));
                                    }
                                }
                                Platform.runLater(() -> comboBox.getSelectionModel().clearSelection());
                            });
                            comboBox.setValue(item);
                            setGraphic(comboBox);
                        }
                    }

                };
            }
        });

        createNewTransactionComboBox.setItems(FXCollections.observableArrayList(Transaction.TransactionType.values()));
        createNewTransactionComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null){
                switch (newValue){
                    case QUOTATION:
                        TransactionDTO newQuot = TransactionPanelLoader.loadQuotation(null);
                        if (newQuot!=null){
                            getOverviewTable().getItems().add(newQuot);
                        }
                        break;
                    case INVOICE:
                        TransactionDTO newInvo = TransactionPanelLoader.showNewTransactionDialog(Transaction.TransactionType.INVOICE);
                        if (newInvo !=null){
                            getOverviewTable().getItems().add(newInvo);
                        }
                        break;
                    case STOCK:
                        TransactionDTO newStok = TransactionPanelLoader.loadStock(null);
                        if (newStok!=null){
                            getOverviewTable().getItems().add(newStok);
                        }
                        break;
                    case RETURN:
                        TransactionDTO newRet = TransactionPanelLoader.showNewTransactionDialog(Transaction.TransactionType.RETURN);
                        if (newRet!=null){
                            getOverviewTable().getItems().add(newRet);
                        }
                        break;
                }
            }
            Platform.runLater(() -> createNewTransactionComboBox.getSelectionModel().clearSelection());
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
            subList = subList.stream().filter(transaction -> transaction.getDateCreated().isAfter(DateUtils.toDateTime(fromDate))).collect(Collectors.toCollection(FXCollections::observableArrayList));
        }
        if(toDate != null){
            subList = subList.stream().filter(transaction -> transaction.getDateCreated().isBefore(DateUtils.toDateTime(toDate))).collect(Collectors.toCollection(FXCollections::observableArrayList));
        }
        if(paymentOverDueCheckBox.isSelected()){
            subList = subList.stream().filter(transaction -> transaction.getPaymentDueDate() != null
                    && transaction.getPaymentDueDate().isBefore(DateTime.now())
                    && transaction.getPaymentStatus() != null
                    && !transaction.getPaymentStatus().equals(Transaction.PaymentStatus.PAID))
                    .collect(Collectors.toCollection(FXCollections::observableArrayList));
        }
        if(deliveryOverDueCheckBox.isSelected()){
            subList = subList.stream().filter(transaction -> transaction.getDeliveryDueDate() != null
                    && transaction.getDeliveryDueDate().isBefore(DateTime.now())
                    && transaction.getDeliveryStatus() != null
                    && !transaction.getDeliveryStatus().getStatus().equals(DeliveryStatus.Status.DELIVERED))
                    .collect(Collectors.toCollection(FXCollections::observableArrayList));
        }
        if(finalizedCheckBox.isSelected()){
            subList = subList.stream().filter(TransactionDTO::isFinalized)
                    .collect(Collectors.toCollection(FXCollections::observableArrayList));
        }

        getOverviewTable().setItems(subList);
    }

    @FXML
    private void handleClearFilter(){
        transactionTypeComboBox.getEditor().clear();
        fromDatePicker.getEditor().clear();
        toDatePicker.getEditor().clear();
        paymentOverDueCheckBox.setSelected(false);
        deliveryOverDueCheckBox.setSelected(false);
        getOverviewTable().setItems(FXCollections.observableArrayList(getOverviewDTOList()));
    }

    @Override
    public void initSearchField() {
        FilteredList<TransactionDTO> filteredData = new FilteredList<TransactionDTO>(FXCollections.observableArrayList(getOverviewDTOList()), p->true);
        VistaNavigator.getRootLayoutController().getSearchField().textProperty().addListener((observable,oldVal,newVal)->{
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
                }else if(transactionDTO.getPaymentStatus() != null &&
                        transactionDTO.getPaymentStatus().getValue().toLowerCase().contains(lowerCase)){
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

    @Override
    public void initSummaryLabel() {
        bindSummaryLabel(totalQuotedLabel, QUOTED);
        bindSummaryLabel(totalInvoicePaidLabel, INVOICE_PAID);
        bindSummaryLabel(totalInvoiceUnpaidLabel, INVOICE_UNPAID);
        bindSummaryLabel(totalStockPaidLabel, STOCK_PAID);
        bindSummaryLabel(totalStockUnpaidLabel, STOCK_UNPAID);
    }

    private void bindSummaryLabel(Label label, SummaryLabelMode mode){
        DoubleBinding numberBinding = Bindings.createDoubleBinding(() -> {
                    double totalValue = 0 ;
                    switch(mode){
                        case QUOTED:
                            for (TransactionDTO item : getOverviewDTOList()) {
                                if(item.getTransactionType().equals(Transaction.TransactionType.QUOTATION) && !item.isFinalized()){
                                    totalValue = totalValue + item.getSaleAmount();
                                }
                            }
                            break;
                        case INVOICE_PAID:
                            for (TransactionDTO item : getOverviewDTOList()) {
                                if(item.getTransactionType().equals(Transaction.TransactionType.INVOICE) &&
                                        item.getPaymentStatus().equals(Transaction.PaymentStatus.PAID)){
                                    totalValue = totalValue + item.getSaleAmount();
                                }else if(item.getTransactionType().equals(Transaction.TransactionType.INVOICE) &&
                                        item.getPaymentStatus().equals(Transaction.PaymentStatus.PARTIALLY_PAID)){
                                    for(PaymentDTO payment: item.getPayments()){
                                        totalValue += payment.getPaymentAmount();
                                    }
                                }
                            }
                            break;
                        case INVOICE_UNPAID:
                            for (TransactionDTO item : getOverviewDTOList()) {
                                if(item.getTransactionType().equals(Transaction.TransactionType.INVOICE) &&
                                        item.getPaymentStatus().equals(Transaction.PaymentStatus.UNPAID)){
                                    totalValue = totalValue + item.getSaleAmount();
                                }else if(item.getTransactionType().equals(Transaction.TransactionType.INVOICE) &&
                                        item.getPaymentStatus().equals(Transaction.PaymentStatus.PARTIALLY_PAID)){
                                    double unpaid = 0;
                                    for(PaymentDTO payment: item.getPayments()){
                                        unpaid += payment.getPaymentAmount();
                                    }
                                    totalValue += unpaid;
                                }
                            }
                            break;
                        case STOCK_PAID:
                            for (TransactionDTO item : getOverviewDTOList()) {
                                if(item.getTransactionType().equals(Transaction.TransactionType.STOCK) &&
                                        item.getPaymentStatus().equals(Transaction.PaymentStatus.PAID)){
                                    totalValue = totalValue + item.getSaleAmount();
                                }else if(item.getTransactionType().equals(Transaction.TransactionType.STOCK) &&
                                        item.getPaymentStatus().equals(Transaction.PaymentStatus.PARTIALLY_PAID)){
                                    for(PaymentDTO payment: item.getPayments()){
                                        totalValue += payment.getPaymentAmount();
                                    }
                                }
                            }
                            break;
                        case STOCK_UNPAID:
                            for (TransactionDTO item : getOverviewDTOList()) {
                                if(item.getTransactionType().equals(Transaction.TransactionType.STOCK) &&
                                        item.getPaymentStatus().equals(Transaction.PaymentStatus.UNPAID)){
                                    totalValue = totalValue + item.getSaleAmount();
                                }else if(item.getTransactionType().equals(Transaction.TransactionType.STOCK) &&
                                        item.getPaymentStatus().equals(Transaction.PaymentStatus.PARTIALLY_PAID)){
                                    double unpaid = 0;
                                    for(PaymentDTO payment: item.getPayments()){
                                        unpaid += payment.getPaymentAmount();
                                    }
                                    totalValue += unpaid;
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
