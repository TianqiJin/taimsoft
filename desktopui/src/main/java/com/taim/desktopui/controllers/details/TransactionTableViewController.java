package com.taim.desktopui.controllers.details;

import com.taim.desktopui.util.TransactionPanelLoader;
import com.taim.desktopui.util.VistaNavigator;
import com.taim.dto.TransactionDTO;
import com.taim.model.Transaction;
import com.taim.model.TransactionDetail;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;

public class TransactionTableViewController {
    private static final DateTimeFormatter dtf = DateTimeFormat.forPattern("MMM-dd-yyyy");

    @FXML
    private TableView<TransactionDTO> transactionTableView;
    @FXML
    private TableColumn<TransactionDTO, String> transactionIDCol;
    @FXML
    private TableColumn<TransactionDTO, String> dateCreatedCol;
    @FXML
    private TableColumn<TransactionDTO, String> staffCol;
    @FXML
    private TableColumn<TransactionDTO, String> cuVeCol;
    @FXML
    private TableColumn<TransactionDTO, Number> saleAmountCol;
    @FXML
    private TableColumn<TransactionDTO, String> deliveryStatusCol;
    @FXML
    private TableColumn<TransactionDTO, String> paymentStatusCol;
    @FXML
    private TableColumn<TransactionDTO, String> finalizedCol;
    @FXML
    private TableColumn<TransactionDTO, String> actionCol;

    @FXML
    public void initialize(){
        transactionIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        dateCreatedCol.setCellValueFactory(param -> new SimpleStringProperty(dtf.print(param.getValue().getDateCreated())));
        staffCol.setCellValueFactory(param -> param.getValue().getStaff().fullnameProperty());
        cuVeCol.setCellValueFactory(param -> {
            if(param.getValue().getTransactionType().equals(Transaction.TransactionType.INVOICE)){
                if(param.getValue().getVendor() != null){
                    return param.getValue().getVendor().fullnameProperty();
                }

            }else{
                if(param.getValue().getCustomer() != null){
                    return param.getValue().getCustomer().fullnameProperty();
                }
            }
            return new SimpleStringProperty();
        });
        saleAmountCol.setCellValueFactory(new PropertyValueFactory<>("saleAmount"));
        deliveryStatusCol.setCellValueFactory(param -> {
            if (param.getValue().getDeliveryStatus() != null) {
                return new SimpleStringProperty(param.getValue().getDeliveryStatus().getValue());
            }
            return null;
        });
        paymentStatusCol.setCellValueFactory(param -> {
            if(param.getValue().getPaymentStatus() != null){
                return new SimpleStringProperty(param.getValue().getPaymentStatus().getValue());
            }

            return null;
        });
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
                return new TableCell<TransactionDTO, String>() {
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
                            comboBox.setItems(FXCollections.observableArrayList("VIEW DETAILS"));
                            comboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
                                if (newValue != null) {
                                    if (newValue.equals("VIEW DETAILS")) {
                                        VistaNavigator.loadDetailVista(VistaNavigator.VISTA_TRANSACTION_DETAIL, transactionDTO);
                                    }
                                    Platform.runLater(() -> comboBox.getSelectionModel().clearSelection());
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

    public void initTableData(List<TransactionDTO> transactionDTOList){
        transactionTableView.setItems(FXCollections.observableArrayList(transactionDTOList));
    }

}
