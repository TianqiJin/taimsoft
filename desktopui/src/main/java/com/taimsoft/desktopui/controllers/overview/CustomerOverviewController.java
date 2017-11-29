package com.taimsoft.desktopui.controllers.overview;

import com.taim.client.CustomerClient;
import com.taim.client.IClient;
import com.taim.dto.CustomerDTO;
import com.taim.dto.OrganizationDTO;
import com.taim.dto.PropertyDTO;
import com.taim.dto.TransactionDTO;
import com.taim.model.Customer;
import com.taim.model.DeliveryStatus;
import com.taim.model.Property;
import com.taim.model.Transaction;
import com.taim.model.basemodels.UserBaseModel;
import com.taimsoft.desktopui.controllers.edit.CustomerEditDialogController;
import com.taimsoft.desktopui.uicomponents.LiveComboBoxTableCell;
import com.taimsoft.desktopui.util.*;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.joda.time.DateTime;

import java.util.List;
import java.util.stream.Collectors;


/**
 * Created by Tjin on 8/30/2017.
 */
public class CustomerOverviewController extends IOverviewController<CustomerDTO> {
    private CustomerClient customerClient;
    private ObservableList<String> customerClassList;

    @FXML
    private TableColumn<CustomerDTO, String> nameCol;
    @FXML
    private TableColumn<CustomerDTO, String> typeCol;
    @FXML
    private TableColumn<CustomerDTO, String> classCol;
    @FXML
    private TableColumn<CustomerDTO, String> phoneCol;
    @FXML
    private TableColumn<CustomerDTO, String> emailCol;
    @FXML
    private TableColumn<CustomerDTO, Double> storeCreditCol;
    @FXML
    private TableColumn<CustomerDTO, String> actionCol;
//    @FXML
//    private TableColumn<CustomerDTO, Boolean> checkedCol;
    @FXML
    private SplitPane summarySplitPane;
    @FXML
    private Label totalQuotedLabel;
    @FXML
    private Label totalUnpaidLabel;
    @FXML
    private Label totalPaidLabel;
    @FXML
    private ComboBox<String> customerClassComboBox;
    @FXML
    private ComboBox<UserBaseModel.UserType> customerTypeComboBox;
    @FXML
    private CheckBox paymentOverdueCheckBox;
    @FXML
    private CheckBox deliveryOverdueCheckBox;

    public CustomerOverviewController(){
        customerClient = RestClientFactory.getCustomerClient();
        customerClassList = FXCollections.observableArrayList(VistaNavigator.getGlobalProperty().getCustomerClasses().stream()
                .map(PropertyDTO.CustomerClassDTO::getCustomerClassName)
                .collect(Collectors.toList()));
    }

    @FXML
    public void initialize(){
        nameCol.setCellValueFactory(new PropertyValueFactory<>("fullname"));
        typeCol.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getUserType().getValue()));
        classCol.setCellValueFactory(param -> {
            if(param.getValue().getCustomerClass() != null){
                return param.getValue().getCustomerClass().customerClassNameProperty();
            }

            return null;
        });
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        storeCreditCol.setCellValueFactory(new PropertyValueFactory<>("storeCredit"));
//        checkedCol.setCellValueFactory(new PropertyValueFactory<>("isChecked"));
//        checkedCol.setCellFactory(CheckBoxTableCell.forTableColumn(checkedCol));
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
                                    CustomerEditDialogController controller = TransactionPanelLoader.showCustomerEditor(customerDTO);
                                    if(controller != null && controller.isOKClicked()){
                                        getTableView().getItems().set(getIndex(), controller.getCustomer());
                                    }
                                }else if(newValue.equals("DELETE")){
                                    DeleteEntityUtil<CustomerDTO> deleteEntityUtil = new DeleteEntityUtil<>(customerDTO, customerClient);
                                    deleteEntityUtil.deleteEntity(getOverviewTable(),
                                            getIndex(),
                                            "SUCCESSFULLY DELETED CUSTOMER",
                                            getRootPane());
                                }
                            });
                            comboBox.setValue(item);
                            setGraphic(comboBox);
                        }
                    }
                };
            }
        });
        customerClassComboBox.setItems(customerClassList);
        customerTypeComboBox.setItems(FXCollections.observableArrayList(UserBaseModel.UserType.values()));
        customerTypeComboBox.setConverter(new StringConverter<UserBaseModel.UserType>() {
            @Override
            public String toString(UserBaseModel.UserType object) {
                if(object == null){
                    return null;
                }
                return object.toString();
            }

            @Override
            public UserBaseModel.UserType fromString(String string) {
                if(string == null){
                    return null;
                }
                return UserBaseModel.UserType.valueOf(string);
            }
        });
    }

    @Override
    public void initSearchField() {
        FilteredList<CustomerDTO> filteredData = new FilteredList<>(FXCollections.observableArrayList(getOverviewDTOList()), p->true);
        VistaNavigator.getRootLayoutController().getSearchField().textProperty().addListener((observable,oldVal,newVal)->{
            filteredData.setPredicate(customerDTO -> {
                if (newVal == null || newVal.isEmpty()){
                    return true;
                }
                String lowerCase = newVal.toLowerCase();
                if(customerDTO.getFullname().toLowerCase().contains(lowerCase)){
                    return true;
                }else if(customerDTO.getPhone().toLowerCase().contains(lowerCase)){
                    return true;
                }else if(customerDTO.getEmail().toLowerCase().contains(lowerCase)){
                    return true;
                }else if(String.valueOf(customerDTO.getStoreCredit()).contains(lowerCase)){
                    return true;
                }

                return false;
            });
            getOverviewTable().setItems(filteredData);
        });
    }

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

    @FXML
    public void handleAddCustomer(){
        CustomerDTO newCustomer = new CustomerDTO();
        newCustomer.setDateCreated(DateTime.now());
        newCustomer.setDateModified(DateTime.now());
        newCustomer.setOrganization(new OrganizationDTO());
        newCustomer.getOrganization().setDateModified(DateTime.now());
        newCustomer.getOrganization().setDateCreated(DateTime.now());
        CustomerEditDialogController controller = TransactionPanelLoader.showCustomerEditor(newCustomer);
        if(controller != null && controller.isOKClicked()){
            getOverviewTable().getItems().add(controller.getCustomer());
        }
    }

    @FXML
    private void handleFilterCustomer(){
        String customerClass = customerClassComboBox.getValue();
        UserBaseModel.UserType customerType = customerTypeComboBox.getValue();

        ObservableList<CustomerDTO> subList = FXCollections.observableArrayList(getOverviewDTOList());
        if(customerClass != null){
            subList = subList.stream().filter(customerDTO -> customerDTO.getCustomerClass().getCustomerClassName().equals(customerClass)).collect(Collectors.toCollection(FXCollections::observableArrayList));
        }
        if(customerType != null){
            subList = subList.stream().filter(customerDTO -> customerDTO.getUserType().equals(customerType)).collect(Collectors.toCollection(FXCollections::observableArrayList));
        }
        if(deliveryOverdueCheckBox.isSelected()){
            List<TransactionDTO> transactions = getTransactionList().stream()
                    .filter(transactionDTO -> transactionDTO.getDeliveryDueDate() != null
                            && transactionDTO.getDeliveryDueDate().isBefore(DateTime.now())
                            && transactionDTO.getDeliveryStatus() != null
                            && !transactionDTO.getDeliveryStatus().getStatus().equals(DeliveryStatus.Status.DELIVERED))
                    .collect(Collectors.toList());
            subList = subList.stream().
                    filter(customerDTO -> transactions.stream().anyMatch(transactionDTO -> transactionDTO.getCustomer().getId() == customerDTO.getId()))
                    .collect(Collectors.toCollection(FXCollections::observableArrayList));
        }
        if(paymentOverdueCheckBox.isSelected()){
            List<TransactionDTO> transactions = getTransactionList().stream()
                    .filter(transactionDTO -> transactionDTO.getPaymentDueDate() != null
                            && transactionDTO.getPaymentDueDate().isBefore(DateTime.now())
                            && transactionDTO.getPaymentStatus() != null
                            && !transactionDTO.getPaymentStatus().equals(Transaction.PaymentStatus.PAID))
                    .collect(Collectors.toList());
            subList = subList.stream().
                    filter(customerDTO -> transactions.stream().anyMatch(transactionDTO -> transactionDTO.getCustomer().getId() == customerDTO.getId()))
                    .collect(Collectors.toCollection(FXCollections::observableArrayList));
        }

        getOverviewTable().setItems(subList);
    }

    @FXML
    private void handleClearFilter(){
        customerClassComboBox.getEditor().clear();
        customerTypeComboBox.getEditor().clear();
        deliveryOverdueCheckBox.setSelected(false);
        paymentOverdueCheckBox.setSelected(false);
        getOverviewTable().setItems(FXCollections.observableArrayList(getOverviewDTOList()));
    }


    private void bindSummaryLabel(Label label, SummaryLabelMode mode){
        DoubleBinding numberBinding = Bindings.createDoubleBinding(() -> {
                    double totalValue = 0 ;
                    switch(mode){
                        case QUOTED:
                            for (TransactionDTO item : getTransactionList()) {
                                if(item.getTransactionType().equals(Transaction.TransactionType.QUOTATION) && !item.isFinalized()){
                                    totalValue = totalValue + item.getSaleAmount();
                                }
                            }
                            break;
                        case INVOICE_PAID:
                            for (TransactionDTO item : getTransactionList()) {
                                if(item.getTransactionType().equals(Transaction.TransactionType.INVOICE) &&
                                        item.getPaymentStatus().equals(Transaction.PaymentStatus.PAID)){
                                    totalValue = totalValue + item.getSaleAmount();
                                }
                            }
                            break;
                        case INVOICE_UNPAID:
                            for (TransactionDTO item : getTransactionList()) {
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
        label.textProperty().bind(Bindings.format("%s%.2f", "$", numberBinding));
    }
}
