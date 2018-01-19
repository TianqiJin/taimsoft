package com.taim.desktopui.controllers.details;

import com.taim.desktopui.util.AlertBuilder;
import com.taim.desktopui.util.RestClientFactory;
import com.taim.desktopui.util.TransactionPanelLoader;
import com.taim.dto.PaymentDTO;
import com.taim.dto.TransactionDTO;
import com.taim.dto.TransactionDetailDTO;
import com.taim.desktopui.util.VistaNavigator;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableStringValue;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class TransactionDetailsController implements IDetailController<TransactionDTO> {
    private TransactionDTO transactionDTO;
    private static final DateTimeFormatter dtf = DateTimeFormat.forPattern("MMM-dd-yyyy");
    private static final Logger logger = LoggerFactory.getLogger(TransactionDetailsController.class);
    private Executor executor;

    @FXML
    private Label transactionIdLabel;
    @FXML
    private Label transactionTypeLabel;
    @FXML
    private Label dateCreatedLabel;
    @FXML
    private Label gstLabel;
    @FXML
    private Label pstLabel;
    @FXML
    private Label saleAmountLabel;
    @FXML
    private Label paymentLabel;
    @FXML
    private Label staffLabel;
    @FXML
    private Label cuVeLabel;
    @FXML
    private Label paymentDueDateLabel;
    @FXML
    private Label paymentStatusLabel;
    @FXML
    private Label deliveryDueDateLabel;
    @FXML
    private Label deliveryStatusLabel;
    @FXML
    private Label finalizedLabel;
    @FXML
    private Label noteLabel;
    @FXML
    private Hyperlink refrenceTransactionLink;

    @FXML
    private TableView<TransactionDetailDTO> transactionDetaiTableView;
    @FXML
    private TableColumn<TransactionDetailDTO, String> productIdCol;
    @FXML
    private TableColumn<TransactionDetailDTO, String> displayNameCol;
    @FXML
    private TableColumn<TransactionDetailDTO, Double> qtyCol;
    @FXML
    private TableColumn<TransactionDetailDTO, Double> subTotalCol;
    @FXML
    private TableColumn<TransactionDetailDTO, Double> discountCol;
    @FXML
    private TableColumn<TransactionDetailDTO, Integer> boxNumCol;
    @FXML
    private TableColumn<TransactionDetailDTO, Integer> pieceNumCol;
    @FXML
    private TableColumn<TransactionDetailDTO, Double> unitPriceCol;
    @FXML
    private TableColumn<TransactionDetailDTO, String> remarkCol;

    @FXML
    private TableView<PaymentDTO> paymentRecordTableView;
    @FXML
    private TableColumn<PaymentDTO, String> paymentDateCol;
    @FXML
    private TableColumn<PaymentDTO, String> paymentTypeCol;
    @FXML
    private TableColumn<PaymentDTO, Double> paymentAmountCol;
    @FXML
    private TableColumn<PaymentDTO, String> paymentIsDepositCol;
    @FXML
    private ComboBox<String> actionComboBox;
    @FXML
    private ImageView finalizedImage;

    public TransactionDetailsController(){
        executor = Executors.newCachedThreadPool(r -> {
            Thread t = new Thread(r);
            t.setDaemon(true);
            return t;
        });
    }

    @FXML
    public void initialize() throws InterruptedException {
        bindTransactionDetailTable();
        bindPaymentTable();
        actionComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null){
                if(newValue.equals("EDIT")){
                    switch (transactionDTO.getTransactionType()){
                        case QUOTATION:
                            initDetailData(TransactionPanelLoader.loadQuotation(transactionDTO));
                            break;
                        case INVOICE:
                            initDetailData(TransactionPanelLoader.loadInvoice(transactionDTO));
                            break;
                        case RETURN:
                            initDetailData(TransactionPanelLoader.loadReturn(transactionDTO));
                            break;
                        case STOCK:
                            initDetailData(TransactionPanelLoader.loadStock(transactionDTO));
                            break;
                    }
                }else if(newValue.equals("PRINT")){
                    TransactionPanelLoader.showPrintTransactionDialog(transactionDTO);
                }
            }
            Platform.runLater(() -> actionComboBox.getSelectionModel().clearSelection());
        });
    }

    @Override
    public void initDetailData(TransactionDTO transactionDTO) {
        this.transactionDTO = transactionDTO;
        bindTransactionInfoLabels();
        transactionDetaiTableView.setItems(FXCollections.observableArrayList(transactionDTO.getTransactionDetails()));
        paymentRecordTableView.setItems(FXCollections.observableArrayList(transactionDTO.getPayments()));
        if(transactionDTO.isFinalized()){
            actionComboBox.setItems(FXCollections.observableArrayList("PRINT"));
            finalizedImage.setVisible(true);
        }else{
            actionComboBox.setItems(FXCollections.observableArrayList("EDIT", "PRINT"));
        }
        refrenceTransactionLink.setText(String.valueOf(transactionDTO.getRefId()));
        refrenceTransactionLink.setOnAction(event -> {
            if(transactionDTO.getRefId() != 0){
                Task<TransactionDTO> getRefTransactionTask = new Task<TransactionDTO>() {
                    @Override
                    protected TransactionDTO call() throws Exception {
                        return RestClientFactory.getTransactionClient().getById(transactionDTO.getRefId());
                    }
                };

                getRefTransactionTask.setOnSucceeded(event1 -> {
                    VistaNavigator.loadDetailVista(VistaNavigator.VISTA_TRANSACTION_DETAIL, getRefTransactionTask.getValue());
                });

                getRefTransactionTask.setOnFailed(event1 -> {
                    logger.error(getRefTransactionTask.getException().getMessage());
                    new AlertBuilder()
                            .alertType(Alert.AlertType.ERROR)
                            .alertContentText("Unable to fetch reference transaction from the database")
                            .build()
                            .showAndWait();
                });
                executor.execute(getRefTransactionTask);
            }
        });

    }

    private void bindTransactionInfoLabels(){
        if(transactionDTO != null){
            transactionIdLabel.textProperty().bind(initStringBinding(Bindings.isNull(transactionDTO.idProperty().asObject()),
                    "", transactionDTO.idProperty().asString()));
            transactionTypeLabel.textProperty().bind(initStringBinding(Bindings.isNull(transactionDTO.transactionTypeProperty()),
                    "", new SimpleStringProperty(transactionDTO.getTransactionType().getValue())));
            dateCreatedLabel.textProperty().bind(initStringBinding(transactionDTO.dateCreatedProperty().isNull(),
                    "", new SimpleStringProperty(dtf.print(transactionDTO.getDateCreated()))));
            gstLabel.textProperty().bind(initStringBinding(Bindings.isNull(transactionDTO.gstProperty().asObject()),
                    "", transactionDTO.gstProperty().asString()));
            pstLabel.textProperty().bind(initStringBinding(Bindings.isNull(transactionDTO.pstProperty().asObject()),
                    "", transactionDTO.pstProperty().asString()));
            saleAmountLabel.textProperty().bind(initStringBinding(Bindings.isNull(transactionDTO.saleAmountProperty().asObject()),
                    "", transactionDTO.saleAmountProperty().asString()));
            staffLabel.textProperty().bind(initStringBinding(transactionDTO.getStaff().fullnameProperty().isEmpty(),
                    "", transactionDTO.getStaff().fullnameProperty()));
            paymentDueDateLabel.textProperty().bind((initStringBinding(transactionDTO.paymentDueDateProperty().isNull(),
                    "", new SimpleStringProperty(dtf.print(transactionDTO.getPaymentDueDate())))));
            paymentStatusLabel.textProperty().bind(initStringBinding(transactionDTO.paymentStatusProperty().isNull(),
                    "", transactionDTO.getPaymentStatus() != null?
                            new SimpleStringProperty(transactionDTO.getPaymentStatus().getValue()):
                            new SimpleStringProperty("")));
            deliveryDueDateLabel.textProperty().bind(initStringBinding(transactionDTO.deliveryDueDateProperty().isNull(),
                    "", new SimpleStringProperty(dtf.print(transactionDTO.getDeliveryDueDate()))));
            deliveryStatusLabel.textProperty().bind(initStringBinding(transactionDTO.deliveryStatusProperty().isNull(),
                    "", transactionDTO.getDeliveryStatus() != null?
                            new SimpleStringProperty(transactionDTO.getDeliveryStatus().getValue()):
                            new SimpleStringProperty("")));
            finalizedLabel.textProperty().bind(initStringBinding(Bindings.isNull(transactionDTO.finalizedProperty().asObject()),
                    "", transactionDTO.isFinalized()? new SimpleStringProperty("YES"): new SimpleStringProperty("NO")));
            noteLabel.textProperty().bind(initStringBinding(transactionDTO.noteProperty().isEmpty(),
                    "", transactionDTO.noteProperty()));
            if(transactionDTO.getCustomer() != null){
                cuVeLabel.textProperty().bind(initStringBinding(transactionDTO.getCustomer().fullnameProperty().isEmpty(),
                        "", transactionDTO.getCustomer().fullnameProperty()));
            }else{
                cuVeLabel.textProperty().bind(initStringBinding(transactionDTO.getVendor().fullnameProperty().isEmpty(),
                        "", transactionDTO.getVendor().fullnameProperty()));
            }
            BigDecimal paid = new BigDecimal(0);
            for(PaymentDTO paymentDTO: transactionDTO.getPayments()){
                paid = paid.add(new BigDecimal(paymentDTO.getPaymentAmount()));
            }
            paymentLabel.textProperty().bind(new SimpleStringProperty(paid.setScale(2, BigDecimal.ROUND_HALF_EVEN).toEngineeringString()));
        }

    }

    private void bindTransactionDetailTable(){
        productIdCol.setCellValueFactory(param -> param.getValue().getProduct().skuProperty());
        displayNameCol.setCellValueFactory(param -> param.getValue().getProduct().displayNameProperty());
        qtyCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        subTotalCol.setCellValueFactory(new PropertyValueFactory<>("saleAmount"));
        discountCol.setCellValueFactory(new PropertyValueFactory<>("discount"));
        boxNumCol.setCellValueFactory(param -> {
            if(param.getValue().getPackageInfo() != null){
                return  param.getValue().getPackageInfo().boxProperty().asObject();
            }
            return null;
        });
        pieceNumCol.setCellValueFactory(param -> {
            if(param.getValue().getPackageInfo() != null){
                return param.getValue().getPackageInfo().piecesProperty().asObject();
            }
            return null;
        });
        unitPriceCol.setCellValueFactory(param -> param.getValue().getProduct().unitPriceProperty().asObject());
        remarkCol.setCellValueFactory(new PropertyValueFactory<>("note"));
    }

    private void bindPaymentTable(){
        paymentDateCol.setCellValueFactory(param -> new SimpleStringProperty(dtf.print(param.getValue().getDateCreated())));
        paymentTypeCol.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getPaymentType().getValue()));
        paymentAmountCol.setCellValueFactory(new PropertyValueFactory<>("paymentAmount"));
        paymentIsDepositCol.setCellValueFactory(param -> param.getValue().isDeposit()? new SimpleStringProperty("Yes") : new SimpleStringProperty("No"));
    }

    private StringBinding initStringBinding(ObservableBooleanValue condition, String thenVal, ObservableStringValue otherwiseVal){
        return Bindings.when(condition).then(thenVal).otherwise(otherwiseVal);
    }


}