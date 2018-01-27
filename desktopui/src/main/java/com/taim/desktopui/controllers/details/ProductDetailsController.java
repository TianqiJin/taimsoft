package com.taim.desktopui.controllers.details;

import com.jfoenix.controls.JFXComboBox;
import com.taim.client.TransactionClient;
import com.taim.desktopui.util.RestClientFactory;
import com.taim.desktopui.util.TransactionPanelLoader;
import com.taim.dto.ProductDTO;
import com.taim.dto.TransactionDTO;
import com.taim.model.Transaction;
import com.taim.desktopui.controllers.edit.ProductEditDialogController;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableStringValue;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class ProductDetailsController implements IDetailController<ProductDTO> {
    private static final DateTimeFormatter dtf = DateTimeFormat.forPattern("MMM-dd-yyyy");
    private ProductDTO productDTO;
    private TransactionClient transactionClient;
    private Executor executor;
    private List<TransactionDTO> quotationList;
    private List<TransactionDTO> invoiceList;
    private List<TransactionDTO> stockList;
    private List<TransactionDTO> returnList;

    @FXML
    private Label skuLabel;
    @FXML
    private Label textureLabel;
    @FXML
    private Label widthLabel;
    @FXML
    private Label lengthLabel;
    @FXML
    private Label heightLabel;
    @FXML
    private Label quantityLabel;
    @FXML
    private Label virtualQuantityLabel;
    @FXML
    private Label unitPriceLabel;
    @FXML
    private Label stockUnitPriceLabel;
    @FXML
    private Label displayNameLabel;
    @FXML
    private JFXComboBox<String> actionComboBox;
    @FXML
    private Label dateCreatedLabel;
    @FXML
    private TabPane transactionTabPane;


    public ProductDetailsController(){
        this.transactionClient = RestClientFactory.getTransactionClient();
        quotationList = new ArrayList<>();
        invoiceList = new ArrayList<>();
        stockList = new ArrayList<>();
        returnList = new ArrayList<>();
        executor = Executors.newCachedThreadPool(r -> {
            Thread t = new Thread(r);
            t.setDaemon(true);
            return t;
        });
    }

    @FXML
    public void initialize(){
        actionComboBox.setItems(FXCollections.observableArrayList("EDIT"));
        actionComboBox.valueProperty().addListener(((observable, oldValue, newValue) -> {
            if(newValue != null){
                if(newValue.equals("EDIT")){
                    ProductEditDialogController controller = TransactionPanelLoader.showProductEditor(productDTO);
                    if(controller != null && controller.isOKClicked()){
                        initDetailData(controller.getProduct());
                    }
                }
            }
            Platform.runLater(() -> actionComboBox.getSelectionModel().clearSelection());
        }));
    }

    @Override
    public void initDetailData(ProductDTO obj) {
        this.productDTO = obj;
        Task<List<TransactionDTO>> listTask = new Task<List<TransactionDTO>>() {
            @Override
            protected List<TransactionDTO> call() throws Exception {
                return transactionClient.getListByProductID(productDTO.getId());
            }
        };

        listTask.setOnSucceeded(event -> {
            List<TransactionDTO> list = listTask.getValue();
            invoiceList = list.stream().filter(t -> t.getTransactionType().equals(Transaction.TransactionType.INVOICE)).collect(Collectors.toList());
            stockList = list.stream().filter(t -> t.getTransactionType().equals(Transaction.TransactionType.STOCK)).collect(Collectors.toList());
            quotationList = list.stream().filter(t -> t.getTransactionType().equals(Transaction.TransactionType.QUOTATION)).collect(Collectors.toList());
            returnList = list.stream().filter(t -> t.getTransactionType().equals(Transaction.TransactionType.RETURN)).collect(Collectors.toList());
            initTransactionTabPane();
            bindProductInfoLabels();
        });

        executor.execute(listTask);
    }

    private void initTransactionTabPane(){
        transactionTabPane.getSelectionModel().clearSelection();
        transactionTabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {
                FXMLLoader fXMLLoader = new FXMLLoader();
                AnchorPane root = fXMLLoader.load(this.getClass().getResource("/fxml/details/TransactionTableView.fxml").openStream());
                root.prefHeightProperty().bind(transactionTabPane.heightProperty());
                root.prefWidthProperty().bind(transactionTabPane.widthProperty());
                TransactionTableViewController controller = fXMLLoader.getController();
                if(newValue.getText().equals("Quotation Transactions")){
                    controller.initTableData(quotationList);
                }else if(newValue.getText().equals("Invoice Transactions")){
                    controller.initTableData(invoiceList);
                }else if(newValue.getText().equals("Stock Transactions")){
                    controller.initTableData(stockList);
                }else if(newValue.getText().equals("Return Transactions")){
                    controller.initTableData(returnList);
                }
                newValue.setContent(root);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        transactionTabPane.getSelectionModel().selectFirst();
    }

    private void bindProductInfoLabels(){
        if(productDTO != null) {
            skuLabel.textProperty().bind(initStringBinding(productDTO.skuProperty().isNull(),
                    "", productDTO.skuProperty()));
            textureLabel.textProperty().bind(initStringBinding(productDTO.textureProperty().isNull(),
                    "", productDTO.textureProperty()));
            dateCreatedLabel.textProperty().bind(initStringBinding(productDTO.dateCreatedProperty().isNull(),
                    "", new SimpleStringProperty(dtf.print(productDTO.getDateCreated()))));
            widthLabel.textProperty().bind(initStringBinding(Bindings.isNull(productDTO.widthProperty().asObject()),
                    "", productDTO.widthProperty().asString()));
            lengthLabel.textProperty().bind(initStringBinding(Bindings.isNull(productDTO.lengthProperty().asObject()),
                    "", productDTO.lengthProperty().asString()));
            heightLabel.textProperty().bind(initStringBinding(Bindings.isNull(productDTO.heightProperty().asObject()),
                    "", productDTO.heightProperty().asString()));
            quantityLabel.textProperty().bind(initStringBinding(Bindings.isNull(productDTO.totalNumProperty().asObject()),
                    "", productDTO.totalNumProperty().asString()));
            virtualQuantityLabel.textProperty().bind(initStringBinding(Bindings.isNull(productDTO.virtualTotalNumProperty().asObject()),
                    "", productDTO.virtualTotalNumProperty().asString()));
            unitPriceLabel.textProperty().bind(initStringBinding(Bindings.isNull(productDTO.unitPriceProperty().asObject()),
                    "", productDTO.unitPriceProperty().asString()));
            stockUnitPriceLabel.textProperty().bind(initStringBinding(Bindings.isNull(productDTO.stockUnitPriceProperty().asObject()),
                    "", productDTO.stockUnitPriceProperty().asString()));
            displayNameLabel.textProperty().bind(initStringBinding(productDTO.displayNameProperty().isNull(),
                    "", productDTO.displayNameProperty()));
        }
    }

    private StringBinding initStringBinding(ObservableBooleanValue condition, String thenVal, ObservableStringValue
            otherwiseVal){
        return Bindings.when(condition).then(thenVal).otherwise(otherwiseVal);
    }
}
