package com.taim.desktopui.controllers.overview;

import com.taim.client.IClient;
import com.taim.client.VendorClient;
import com.taim.desktopui.controllers.edit.VendorEditDialogController;
import com.taim.desktopui.util.DeleteEntityUtil;
import com.taim.desktopui.util.RestClientFactory;
import com.taim.desktopui.util.TransactionPanelLoader;
import com.taim.desktopui.util.VistaNavigator;
import com.taim.dto.OrganizationDTO;
import com.taim.dto.TransactionDTO;
import com.taim.dto.VendorDTO;
import com.taim.model.Transaction;
import com.taim.model.basemodels.UserBaseModel;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import java.util.List;
import java.util.stream.Collectors;

import static com.taim.desktopui.controllers.overview.IOverviewController.SummaryLabelMode.*;

/**
 * Created by Tjin on 8/30/2017.
 */
public class VendorOverviewController extends IOverviewController<VendorDTO> {
    private VendorClient vendorClient;

    @FXML
    private TableColumn<VendorDTO, String> nameCol;
    @FXML
    private TableColumn<VendorDTO, String> typeCol;
    @FXML
    private TableColumn<VendorDTO, String> phoneCol;
    @FXML
    private TableColumn<VendorDTO, String> emailCol;
    @FXML
    private TableColumn<VendorDTO, String> actionCol;
//    @FXML
//    private TableColumn<VendorDTO, Boolean> checkedCol;
    @FXML
    private SplitPane summarySplitPane;
    @FXML
    private Label totalUnpaidLabel;
    @FXML
    private Label totalPaidLabel;
    @FXML
    private ComboBox<UserBaseModel.UserType> vendorTypeComboBox;
    @FXML
    private CheckBox paymentOverdueCheckBox;
    @FXML
    private CheckBox deliveryOverdueCheckBox;
    @FXML
    private TextField addressFilterField;

    public VendorOverviewController(){
        this.vendorClient = RestClientFactory.getVendorClient();
    }

    @FXML
    public void initialize(){
        nameCol.setCellValueFactory(new PropertyValueFactory<>("fullname"));
        typeCol.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getUserType().getValue()));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
//        checkedCol.setCellValueFactory(new PropertyValueFactory<>("isChecked"));
//        checkedCol.setCellFactory(CheckBoxTableCell.forTableColumn(checkedCol));
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
                                if(newValue != null){
                                    if(newValue.equals("VIEW DETAILS")){
                                        VistaNavigator.loadDetailVista(VistaNavigator.VISTA_VENDOR_DETAIL, vendorDTO);
                                    }else if(newValue.equals("EDIT")){
                                        VendorEditDialogController controller = TransactionPanelLoader.showVendorEditor(vendorDTO);
                                        if(controller != null && controller.isOKClicked()){
                                            getTableView().getItems().set(getIndex(), controller.getVendor());
                                        }
                                    }else if(newValue.equals("DELETE")){
                                        DeleteEntityUtil<VendorDTO> deleteEntityUtil = new DeleteEntityUtil<>(vendorDTO, vendorClient);
                                        deleteEntityUtil.deleteEntity(getOverviewTable(),
                                                getIndex(),
                                                "SUCCESSFULLY DELETED VENDOR",
                                                getRootPane());
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
        vendorTypeComboBox.setItems(FXCollections.observableArrayList(UserBaseModel.UserType.values()));
        vendorTypeComboBox.setConverter(new StringConverter<UserBaseModel.UserType>() {
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

    @FXML
    public void handleAddVendor(){
        VendorDTO newVendor = new VendorDTO();
        newVendor.setDateCreated(DateTime.now());
        newVendor.setDateModified(DateTime.now());
        newVendor.setOrganization(new OrganizationDTO());
        newVendor.getOrganization().setDateCreated(DateTime.now());
        newVendor.getOrganization().setDateModified(DateTime.now());
        VendorEditDialogController controller = TransactionPanelLoader.showVendorEditor(newVendor);
        if(controller != null && controller.isOKClicked()){
            getOverviewTable().getItems().add(controller.getVendor());
        }
    }

    @FXML
    private void handleFilterVendor(){
        UserBaseModel.UserType vendorType = vendorTypeComboBox.getValue();

        ObservableList<VendorDTO> subList = FXCollections.observableArrayList(getOverviewDTOList());
        if(vendorType != null){
            subList = subList.stream().filter(customerDTO -> customerDTO.getUserType().equals(vendorType)).collect(Collectors.toCollection(FXCollections::observableArrayList));
        }
        if(deliveryOverdueCheckBox.isSelected()){
            List<TransactionDTO> transactions = getTransactionList().stream()
                    .filter(transactionDTO -> transactionDTO.getDeliveryDueDate() != null
                            && transactionDTO.getDeliveryDueDate().isBefore(DateTime.now())
                            && transactionDTO.getDeliveryStatus() != null
                            && !transactionDTO.getDeliveryStatus().getValue().equals(Transaction.DeliveryStatus.DELIVERED))
                    .collect(Collectors.toList());
            subList = subList.stream().
                    filter(vendorDTO -> transactions.stream().anyMatch(transactionDTO -> transactionDTO.getVendor().getId() == vendorDTO.getId()))
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
                    filter(vendorDTO -> transactions.stream().anyMatch(transactionDTO -> transactionDTO.getVendor().getId() == vendorDTO.getId()))
                    .collect(Collectors.toCollection(FXCollections::observableArrayList));
        }
        if(StringUtils.isNotEmpty(addressFilterField.getText())){
            subList = subList.stream().filter(customerDTO -> {
                OrganizationDTO customerOrg = customerDTO.getOrganization();
                String address = customerOrg.getStreetNum() + customerOrg.getStreet() + customerOrg.getCity() + customerOrg.getCountry() + customerOrg.getPostalCode();
                return address.contains(addressFilterField.getText());})
                    .collect(Collectors.toCollection(FXCollections::observableArrayList));
        }

        getOverviewTable().setItems(subList);
    }

    @FXML
    private void handleClearFilter(){
        vendorTypeComboBox.getEditor().clear();
        deliveryOverdueCheckBox.setSelected(false);
        paymentOverdueCheckBox.setSelected(false);
        getOverviewTable().setItems(FXCollections.observableArrayList(getOverviewDTOList()));
    }

    @Override
    public void initSearchField() {
        FilteredList<VendorDTO> filteredData = new FilteredList<>(FXCollections.observableArrayList(getOverviewDTOList()), p->true);
        VistaNavigator.getRootLayoutController().getSearchField().textProperty().addListener((observable,oldVal,newVal)->{
            filteredData.setPredicate(vendorDTO -> {
                if (newVal == null || newVal.isEmpty()){
                    return true;
                }
                String lowerCase = newVal.toLowerCase();
                if(vendorDTO.getFullname().toLowerCase().contains(lowerCase)){
                    return true;
                }else if(vendorDTO.getPhone().toLowerCase().contains(lowerCase)){
                    return true;
                }else if(vendorDTO.getEmail().toLowerCase().contains(lowerCase)){
                    return true;
                }

                return false;
            });
            getOverviewTable().setItems(filteredData);
        });
    }

    @Override
    public IClient<VendorDTO> getOverviewClient(){
        return this.vendorClient;
    }

    @Override
    public void initSummaryLabel() {
        bindSummaryLabel(totalUnpaidLabel, STOCK_UNPAID);
        bindSummaryLabel(totalPaidLabel, STOCK_PAID);
    }

    private void bindSummaryLabel(Label label, SummaryLabelMode mode){
        DoubleBinding numberBinding = Bindings.createDoubleBinding(() -> {
                    double totalValue = 0 ;
                    switch(mode){
                        case STOCK_PAID:
                            for (TransactionDTO item : getTransactionList()) {
                                if(item.getTransactionType().equals(Transaction.TransactionType.STOCK) &&
                                        item.getPaymentStatus().equals(Transaction.PaymentStatus.PAID)){
                                    totalValue = totalValue + item.getSaleAmount();
                                }
                            }
                            break;
                        case STOCK_UNPAID:
                            for (TransactionDTO item : getTransactionList()) {
                                if(item.getTransactionType().equals(Transaction.TransactionType.STOCK) &&
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
