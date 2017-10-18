package com.taimsoft.desktopui.controllers.details;

import com.taim.dto.PaymentDTO;
import com.taim.dto.TransactionDTO;
import com.taim.dto.TransactionDetailDTO;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableStringValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.math.BigDecimal;

public class TransactionDetailsController implements IDetailController<TransactionDTO> {
    private TransactionDTO transactionDTO;

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

    public TransactionDetailsController(){}

    @FXML
    public void initialize() throws InterruptedException {
        bindTransactionDetailTable();
        bindPaymentTable();
        actionComboBox.setItems(FXCollections.observableArrayList("EDIT", "PRINT"));
    }

    @Override
    public void initDetailData(TransactionDTO transactionDTO) {
        this.transactionDTO = transactionDTO;
        bindTransactionInfoLabels();
        transactionDetaiTableView.setItems(FXCollections.observableArrayList(transactionDTO.getTransactionDetails()));
        paymentRecordTableView.setItems(FXCollections.observableArrayList(transactionDTO.getPayments()));
    }

    private void bindTransactionInfoLabels(){
        if(transactionDTO != null){
            transactionIdLabel.textProperty().bind(initStringBinding(Bindings.isNull(transactionDTO.idProperty().asObject()),
                    "", transactionDTO.idProperty().asString()));
            transactionTypeLabel.textProperty().bind(initStringBinding(Bindings.isNull(transactionDTO.transactionTypeProperty()),
                    "", transactionDTO.transactionTypeProperty().asString()));
            dateCreatedLabel.textProperty().bind(initStringBinding(transactionDTO.dateCreatedProperty().isNull(),
                    "", transactionDTO.dateCreatedProperty().asString()));
            gstLabel.textProperty().bind(initStringBinding(Bindings.isNull(transactionDTO.gstProperty().asObject()),
                    "", transactionDTO.gstProperty().asString()));
            pstLabel.textProperty().bind(initStringBinding(Bindings.isNull(transactionDTO.pstProperty().asObject()),
                    "", transactionDTO.pstProperty().asString()));
            saleAmountLabel.textProperty().bind(initStringBinding(Bindings.isNull(transactionDTO.saleAmountProperty().asObject()),
                    "", transactionDTO.saleAmountProperty().asString()));
            staffLabel.textProperty().bind(initStringBinding(transactionDTO.getStaff().fullnameProperty().isEmpty(),
                    "", transactionDTO.getStaff().fullnameProperty()));
            paymentDueDateLabel.textProperty().bind((initStringBinding(transactionDTO.paymentDueDateProperty().isNull(),
                    "", transactionDTO.paymentDueDateProperty().asString())));
            paymentStatusLabel.textProperty().bind(initStringBinding(transactionDTO.paymentStatusProperty().isNull(),
                    "", transactionDTO.paymentStatusProperty().asString()));
            deliveryDueDateLabel.textProperty().bind(initStringBinding(transactionDTO.deliveryDueDateProperty().isNull(),
                    "", transactionDTO.deliveryDueDateProperty().asString()));
            deliveryStatusLabel.textProperty().bind(initStringBinding(transactionDTO.deliveryStatusProperty().isNull(),
                    "", transactionDTO.deliveryStatusProperty().asString()));
            finalizedLabel.textProperty().bind(initStringBinding(Bindings.isNull(transactionDTO.finalizedProperty().asObject()),
                    "", transactionDTO.finalizedProperty().asString()));
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
        boxNumCol.setCellValueFactory(param -> param.getValue().getPackageInfo().boxProperty().asObject());
        pieceNumCol.setCellValueFactory(param -> param.getValue().getPackageInfo().piecesProperty().asObject());
        unitPriceCol.setCellValueFactory(param -> param.getValue().getProduct().unitPriceProperty().asObject());
        remarkCol.setCellValueFactory(new PropertyValueFactory<>("note"));
    }

    private void bindPaymentTable(){
        paymentDateCol.setCellValueFactory(new PropertyValueFactory<>("dateCreated"));
        paymentTypeCol.setCellValueFactory(new PropertyValueFactory<>("paymentType"));
        paymentAmountCol.setCellValueFactory(new PropertyValueFactory<>("paymentAmount"));
        paymentIsDepositCol.setCellValueFactory(param -> param.getValue().isDeposit()? new SimpleStringProperty("Yes") : new SimpleStringProperty("No"));
    }

    private StringBinding initStringBinding(ObservableBooleanValue condition, String thenVal, ObservableStringValue otherwiseVal){
        return Bindings.when(condition).then(thenVal).otherwise(otherwiseVal);
    }


}