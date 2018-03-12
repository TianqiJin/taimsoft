package com.taim.desktopui.controllers.details;

import com.jfoenix.controls.JFXComboBox;
import com.taim.dto.StaffDTO;
import com.taim.dto.TransactionDTO;
import com.taim.model.Transaction;
import com.taim.desktopui.controllers.edit.StaffEditDialogController;
import com.taim.desktopui.util.AlertBuilder;
import com.taim.desktopui.util.RestClientFactory;
import com.taim.desktopui.util.TransactionPanelLoader;
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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class StaffDetailsController implements IDetailController<StaffDTO> {
    private static final DateTimeFormatter dtf = DateTimeFormat.forPattern("MMM-dd-yyyy");
    private static final Logger logger = LoggerFactory.getLogger(StaffEditDialogController.class);
    private StaffDTO staffDTO;
    private List<TransactionDTO> quotationList;
    private List<TransactionDTO> invoiceList;
    private List<TransactionDTO> returnList;
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
    private Label positionLabel;
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
    private Label dateCreatedLabel;
    @FXML
    private JFXComboBox<String> actionComboBox;
    @FXML
    private TabPane transactionTabPane;


    public StaffDetailsController(){
        quotationList = new ArrayList<>();
        invoiceList = new ArrayList<>();
        returnList = new ArrayList<>();
        stockList = new ArrayList<>();
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
                    StaffEditDialogController controller = TransactionPanelLoader.showStaffEditor(staffDTO);
                    if(controller != null && controller.isOkClicked()){
                        initDetailData(controller.getStaff());
                    }
                }
            }
            Platform.runLater(() -> actionComboBox.getSelectionModel().clearSelection());
        });
    }

    @Override
    public void initDetailData(StaffDTO obj) {
        this.staffDTO = obj;
        initDataFromDB(staffDTO.getId());
        bindStaffInfoLabels();
    }

    @Override
    public void setStage(Stage stage){
        this.stage = stage;
    }

    public void initDataFromDB(int id){
        Task<List<TransactionDTO>> transactionTask = new Task<List<TransactionDTO>>() {
            @Override
            protected List<TransactionDTO> call() throws Exception {
                return RestClientFactory.getTransactionClient().getListByStaffID(id);
            }
        };

        transactionTask.setOnSucceeded(event ->{
            try {
                this.invoiceList = transactionTask.get().stream().filter(t -> t.getTransactionType().equals(Transaction.TransactionType.INVOICE)).collect(Collectors.toList());
                this.quotationList = transactionTask.get().stream().filter(t -> t.getTransactionType().equals(Transaction.TransactionType.QUOTATION)).collect(Collectors.toList());
//                this.returnList = transactionTask.get().stream().filter(t -> t.getTransactionType().equals(Transaction.TransactionType.RETURN)).collect(Collectors.toList());
//                this.stockList = transactionTask.get().stream().filter(t -> t.getTransactionType().equals(Transaction.TransactionType.STOCK)).collect(Collectors.toList());
                initTransactionTabPane();
            } catch (InterruptedException | ExecutionException e) {
                logger.error(e.getMessage(), e);
            }
        });
        transactionTask.setOnFailed(event -> {
            logger.error(transactionTask.getException().getMessage());
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
                if(newValue.getText().equals("Quotation Transactions")){
                    controller.initTableData(quotationList);
                }else if(newValue.getText().equals("Invoice Transactions")){
                    controller.initTableData(invoiceList);
                }else if(newValue.getText().equals("Return Transactions")){
                    controller.initTableData(returnList);
                }else if(newValue.getText().equals("Stock Transactions")){
                    controller.initTableData(stockList);
                }
                newValue.setContent(root);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        transactionTabPane.getSelectionModel().selectFirst();
    }

    private void bindStaffInfoLabels(){
        if(staffDTO != null) {
            dateCreatedLabel.textProperty().bind(initStringBinding(staffDTO.dateCreatedProperty().isNull(),
                    "", new SimpleStringProperty(dtf.print(staffDTO.getDateCreated()))));
            fullNameLabel.textProperty().bind(initStringBinding(staffDTO.fullnameProperty().isNull(),
                    "", staffDTO.fullnameProperty()));
            emailLabel.textProperty().bind(initStringBinding(staffDTO.emailProperty().isNull(),
                    "", staffDTO.emailProperty()));
            phoneLabel.textProperty().bind(initStringBinding(staffDTO.phoneProperty().isNull(),
                    "", staffDTO.phoneProperty()));
            positionLabel.textProperty().bind(initStringBinding(staffDTO.positionProperty().isNull(),
                    "", new SimpleStringProperty(staffDTO.getPosition().getValue())));
            if(staffDTO.getOrganization() != null){
                orgNameLabel.textProperty().bind(initStringBinding(staffDTO.getOrganization().orgNameProperty().isNull(),
                        "", staffDTO.getOrganization().orgNameProperty()));
                streetNumLabel.textProperty().bind(initStringBinding(staffDTO.getOrganization().streetNumProperty().isNull(),
                        "", staffDTO.getOrganization().streetNumProperty()));
                streetLabel.textProperty().bind(initStringBinding(staffDTO.getOrganization().streetProperty().isNull(),
                        "", staffDTO.getOrganization().streetProperty()));
                cityLabel.textProperty().bind(initStringBinding(staffDTO.getOrganization().cityProperty().isNull(),
                        "", staffDTO.getOrganization().cityProperty()));
                countryLabel.textProperty().bind(initStringBinding(staffDTO.getOrganization().countryProperty().isNull(),
                        "", staffDTO.getOrganization().countryProperty()));
                postalCodeLabel.textProperty().bind(initStringBinding(staffDTO.getOrganization().postalCodeProperty().isNull(),
                        "", staffDTO.getOrganization().postalCodeProperty()));
            }
        }
    }

    private StringBinding initStringBinding(ObservableBooleanValue condition, String thenVal, ObservableStringValue
            otherwiseVal){
        return Bindings.when(condition).then(thenVal).otherwise(otherwiseVal);
    }
}
