package com.taim.desktopui.controllers.details;

import com.jfoenix.controls.JFXComboBox;
import com.taim.desktopui.controllers.edit.VendorEditDialogController;
import com.taim.desktopui.util.AlertBuilder;
import com.taim.desktopui.util.RestClientFactory;
import com.taim.desktopui.util.TransactionPanelLoader;
import com.taim.dto.TransactionDTO;
import com.taim.dto.VendorDTO;
import com.taim.model.Transaction;
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
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class VendorDetailsController implements IDetailController<VendorDTO> {
    private static final Logger logger = LoggerFactory.getLogger(VendorDetailsController.class);
    private static final DateTimeFormatter dtf = DateTimeFormat.forPattern("MMM-dd-yyyy");
    private VendorDTO vendorDTO;
    private List<TransactionDTO> stockList;
    private Executor executor;
    private Stage stage;

    @FXML
    private Label fullNameLabel;
    @FXML
    private Label phoneLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label dateCreatedLabel;
    @FXML
    private Label vendorTypeLabel;
    @FXML
    private Label orgNameLabel;
    @FXML
    private Label streetNumLabel;
    @FXML
    private Label streetLabel;
    @FXML
    private Label cityLabel;
    @FXML
    private Label countryLabel;
    @FXML
    private Label postalCodeLabel;
    @FXML
    private JFXComboBox<String> actionComboBox;
    @FXML
    private TabPane transactionTabPane;


    public VendorDetailsController(){
        executor = Executors.newCachedThreadPool(r -> {
            Thread t = new Thread(r);
            t.setDaemon(true);
            return t;
        });
    }

    @FXML
    public void initialize(){
        actionComboBox.setItems(FXCollections.observableArrayList("EDIT"));
        actionComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null){
                if(newValue.equals("EDIT")){
                    VendorEditDialogController controller = TransactionPanelLoader.showVendorEditor(vendorDTO);
                    if(controller != null && controller.isOKClicked()){
                        initDetailData(controller.getVendor());
                    }
                }
            }
            Platform.runLater(() -> actionComboBox.getSelectionModel().clearSelection());
        });
    }

    @Override
    public void initDetailData(VendorDTO obj) {
        this.vendorDTO = obj;
        initDataFromDB(vendorDTO.getId());
        bindVendorInfoLabels();
    }

    @Override
    public void setStage(Stage stage){
        this.stage = stage;
    }

    public void initDataFromDB(int id){
        Task<List<TransactionDTO>> transactionTask = new Task<List<TransactionDTO>>() {
            @Override
            protected List<TransactionDTO> call() throws Exception {
                return RestClientFactory.getTransactionClient().getListByVendorID(id);
            }
        };

        transactionTask.setOnSucceeded(event ->{
            try {
                this.stockList = transactionTask.get().stream().filter(t -> t.getTransactionType().equals(Transaction.TransactionType.STOCK)).collect(Collectors.toList());
                initTransactionTabPane();
            } catch (InterruptedException | ExecutionException e) {
                logger.error(e.getMessage(), e);
            }
        });
        transactionTask.setOnFailed(event -> {
            new AlertBuilder(stage)
                    .alertType(Alert.AlertType.ERROR)
                    .alertHeaderText("Database Error!")
                    .alertContentText("Unable to fetch transaction information from the database!")
                    .build()
                    .showAndWait();
        });
        executor.execute(transactionTask);

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
                if(newValue.getText().equals("Stock Transactions")){
                    controller.initTableData(stockList);
                }
                newValue.setContent(root);
            } catch (IOException ex) {
                logger.error(ex.getMessage(), ex);
            }
        });
        transactionTabPane.getSelectionModel().selectFirst();
    }

    private void bindVendorInfoLabels(){
        if(vendorDTO != null) {
            dateCreatedLabel.textProperty().bind(initStringBinding(vendorDTO.dateCreatedProperty().isNull(),
                    "", new SimpleStringProperty(dtf.print(vendorDTO.getDateCreated()))));
            fullNameLabel.textProperty().bind(initStringBinding(vendorDTO.fullnameProperty().isNull(),
                    "", vendorDTO.fullnameProperty()));
            emailLabel.textProperty().bind(initStringBinding(vendorDTO.emailProperty().isNull(),
                    "", vendorDTO.emailProperty()));
            phoneLabel.textProperty().bind(initStringBinding(vendorDTO.phoneProperty().isNull(),
                    "", vendorDTO.phoneProperty()));
            vendorTypeLabel.textProperty().bind(initStringBinding(vendorDTO.userTypeProperty().isNull(),
                    "", new SimpleStringProperty(vendorDTO.getUserType().getValue())));
            if(vendorDTO.getOrganization() != null){
                orgNameLabel.textProperty().bind(initStringBinding(vendorDTO.getOrganization().orgNameProperty().isNull(),
                        "", vendorDTO.getOrganization().orgNameProperty()));
                streetNumLabel.textProperty().bind(initStringBinding(vendorDTO.getOrganization().streetNumProperty().isNull(),
                        "", vendorDTO.getOrganization().streetNumProperty()));
                streetLabel.textProperty().bind(initStringBinding(vendorDTO.getOrganization().streetProperty().isNull(),
                        "", vendorDTO.getOrganization().streetProperty()));
                cityLabel.textProperty().bind(initStringBinding(vendorDTO.getOrganization().cityProperty().isNull(),
                        "", vendorDTO.getOrganization().cityProperty()));
                countryLabel.textProperty().bind(initStringBinding(vendorDTO.getOrganization().countryProperty().isNull(),
                        "", vendorDTO.getOrganization().countryProperty()));
                postalCodeLabel.textProperty().bind(initStringBinding(vendorDTO.getOrganization().postalCodeProperty().isNull(),
                        "", vendorDTO.getOrganization().postalCodeProperty()));
            }
        }
    }

    private StringBinding initStringBinding(ObservableBooleanValue condition, String thenVal, ObservableStringValue
            otherwiseVal){
        return Bindings.when(condition).then(thenVal).otherwise(otherwiseVal);
    }
}
