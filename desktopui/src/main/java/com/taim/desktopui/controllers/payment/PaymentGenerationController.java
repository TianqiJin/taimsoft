package com.taim.desktopui.controllers.payment;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.taim.client.CustomerClient;
import com.taim.client.PaymentClient;
import com.taim.client.VendorClient;
import com.taim.desktopui.constants.Constant;
import com.taim.desktopui.util.*;
import com.taim.dto.*;
import com.taim.dto.basedtos.UserBaseModelDTO;
import com.taim.model.Payment;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableObjectValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.fxmisc.easybind.EasyBind;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PaymentGenerationController implements Initializable{
    private static final DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd");
    private static final Logger logger = LoggerFactory.getLogger(PaymentGenerationController.class);
    private static final String TYPE = "PAYMENT";

    private CustomerClient customerClient;
    private VendorClient vendorClient;
    private PaymentClient paymentClient;
    private ObjectProperty<UserBaseModelDTO> user;
    private List<UserBaseModelDTO> userList;
    private List<String> userNameList;
    private ObservableList<PaymentDetailDTO> paymentDetailList;
    private Executor executor;
    private ObjectProperty<PaymentDTO> payment;
    private Stage dialogStage;

    @FXML
    private SplitPane paymentGenerationSplitPane;
    @FXML
    private Label titleLabel;
    @FXML
    private JFXComboBox<String> userComboBox;
    @FXML
    private JFXTextField paymentAmountTextField;
    @FXML
    private JFXTextField paymentIDTextField;
    @FXML
    private JFXDatePicker paymentDatePicker;
    @FXML
    private JFXTextField memoTextField;
    @FXML
    private Label amountDueLabel;
    @FXML
    private Label appliedAmountLabel;
    @FXML
    private Label discountLabel;
    @FXML
    private TableView<PaymentDetailDTO> paymentDetailTableView;
    @FXML
    private TableColumn<PaymentDetailDTO, String> idCol;
    @FXML
    private TableColumn<PaymentDetailDTO, String> dateCol;
    @FXML
    private TableColumn<PaymentDetailDTO, String> typeCol;
    @FXML
    private TableColumn<PaymentDetailDTO, Number> amountCol;
    @FXML
    private TableColumn<PaymentDetailDTO, Number> balanceCol;
    @FXML
    private TableColumn<PaymentDetailDTO, Number> paymentCol;
    @FXML
    private TableColumn deleteCol;
    @FXML
    private JFXButton findTransactionButton;


    public PaymentGenerationController(){
        this.executor = Executors.newCachedThreadPool(r -> {
            Thread t = new Thread(r);
            t.setDaemon(true);
            return t;
        });
        customerClient = RestClientFactory.getCustomerClient();
        paymentClient = RestClientFactory.getPaymentClient();
        vendorClient = RestClientFactory.getVendorClient();
        this.payment = new SimpleObjectProperty<>();
        this.user = new SimpleObjectProperty<>();
        this.userList = new ArrayList<>();
        this.userNameList = new ArrayList<>();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initFindTransactionButton();
        initTitle();
        initPaymentTransactionPanel();
        initPaymentBasicInfoPanel();
    }

    @FXML
    private void handleCancelButton(){
        dialogStage.close();
    }

    @FXML
    private void handleConfirmButton(){
        dialogStage.close();
    }

    @FXML
    private void handleFindTransactionsButton(){
        try {
            FXMLLoader fXMLLoader = new FXMLLoader();
            AnchorPane root = fXMLLoader.load(this.getClass().getResource("/fxml/payment/FindTransaction.fxml").openStream());
            Stage findTransactionStage = new Stage();
            findTransactionStage.setTitle("Find Transactions");
            findTransactionStage.initModality(Modality.APPLICATION_MODAL);
            Scene scene = new Scene(root);
            findTransactionStage.setScene(scene);
            findTransactionStage.getIcons().add(new Image(PaymentGenerationController.class.getResourceAsStream(Constant.Image.appIconPath)));

            FindTransactionController findTransactionController = fXMLLoader.getController();
            findTransactionController.setStage(findTransactionStage);
            findTransactionController.fetchUnpaidInvoices(
                    this.payment.get().getPaymentType(),
                    this.payment.get().getUserID(),
                    paymentDetailTableView.getItems());

            findTransactionStage.showAndWait();
        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    public void init(Payment.PaymentType type, PaymentDTO payment){
        initPayment(payment, type);
        initUserList(this.payment.get());
    }

    private void initUserList(PaymentDTO paymentDTO){
        if(paymentDTO.getPaymentType().equals(Payment.PaymentType.CUSTOMER_PAYMENT)){
            Task<List<CustomerDTO>> getAllCustomerTask = new Task<List<CustomerDTO>>() {
                @Override
                protected List<CustomerDTO> call() throws Exception {
                    return customerClient.getList();
                }
            };
            getAllCustomerTask.setOnSucceeded(event -> {
                userList.addAll(getAllCustomerTask.getValue());
                userNameList.addAll(userList.stream().map(UserBaseModelDTO::getFullname).collect(Collectors.toList()));
            });
            getAllCustomerTask.setOnFailed(event -> {
                new AlertBuilder(dialogStage)
                        .alertType(Alert.AlertType.ERROR)
                        .alertContentText("Failed to fetch customers.")
                        .build()
                        .showAndWait();
            });
            executor.execute(getAllCustomerTask);
        }else{
            Task<List<VendorDTO>> getAllVendorTask = new Task<List<VendorDTO>>() {
                @Override
                protected List<VendorDTO> call() throws Exception {
                    return vendorClient.getList();
                }
            };
            getAllVendorTask.setOnSucceeded(event -> {
                userList.addAll(getAllVendorTask.getValue());
                userNameList = userList.stream().map(UserBaseModelDTO::getFullname).collect(Collectors.toList());
                userComboBox.setItems(FXCollections.observableArrayList(userNameList));
            });
            getAllVendorTask.setOnFailed(event -> {
                new AlertBuilder(dialogStage)
                        .alertType(Alert.AlertType.ERROR)
                        .alertContentText("Failed to fetch vendors.")
                        .build()
                        .showAndWait();
            });
            executor.execute(getAllVendorTask);
        }
    }

    private void initTitle(){
        titleLabel.textProperty().bind(EasyBind.monadic(payment).flatMap(PaymentDTO::paymentTypeProperty)
                .selectProperty(t -> new SimpleStringProperty(t.getValue())).orElse(""));;
    }

    private void initPayment(PaymentDTO newPayment, Payment.PaymentType paymentType){
        if(newPayment == null){
            newPayment = new PaymentDTO();
            newPayment.setPresentId(IDGenerator.createPaymentID());
            newPayment.setDateCreated(DateTime.now());
            newPayment.setDateModified(DateTime.now());
            newPayment.setStaffID(VistaNavigator.getGlobalStaff().getId());
            newPayment.setPaymentType(paymentType);
        }
        this.payment.set(newPayment);
    }

    private void initPaymentTransactionPanel(){
        idCol.setCellValueFactory(param -> param.getValue().getTransaction().presentIdProperty());
        dateCol.setCellValueFactory(param -> new SimpleStringProperty(dtf.print(param.getValue().getTransaction().getDateCreated())));
        typeCol.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getTransaction().getTransactionType().getValue()));
        amountCol.setCellValueFactory(param -> param.getValue().getTransaction().saleAmountProperty());
        balanceCol.setCellValueFactory(param -> param.getValue().getTransaction().balanceProperty());
        paymentCol.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Number>() {
            @Override
            public String toString(Number object) {
                return String.valueOf(object);
            }

            @Override
            public Double fromString(String string) {
                return Double.valueOf(string);
            }
        }));
        paymentCol.setOnEditCommit(event -> {
            PaymentDetailDTO paymentDetailDTO = event.getTableView().getItems().get(event.getTablePosition().getRow());
            paymentDetailDTO.setAmount((Double) event.getNewValue());
        });
    }

    private void initPaymentBasicInfoPanel(){
        memoTextField.textProperty().bind(EasyBind.monadic(payment).selectProperty(PaymentDTO::noteProperty));
        paymentIDTextField.textProperty().bind(EasyBind.monadic(payment).selectProperty(PaymentDTO::presentIdProperty));
        paymentDatePicker.setOnAction(event ->{
            this.payment.get().setDateCreated(DateUtils.toDateTime(paymentDatePicker.getValue()));
        });
        paymentDatePicker.setConverter(new StringConverter<LocalDate>() {
            java.time.format.DateTimeFormatter dateFormatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd");
            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }
            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        });

        userComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null){
                Optional<UserBaseModelDTO> tmpUser = userList.stream().filter(userBaseModelDTO -> userBaseModelDTO.getFullname().equals(newValue)).findAny();
                tmpUser.ifPresent(userBaseModelDTO -> this.user.set(userBaseModelDTO));
            }
        });
    }

    private void initPaymentAmountPanel(){}

    private void initFindTransactionButton(){
        findTransactionButton.disableProperty().bind(user.isNull());
    }

    public Stage getDialogStage() {
        return dialogStage;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public PaymentDTO getPayment() {
        return payment.get();
    }

    public ObservableObjectValue<PaymentDTO> paymentProperty() {
        return payment;
    }

    public UserBaseModelDTO getUser() {
        return user.get();
    }

    public ObjectProperty<UserBaseModelDTO> userProperty() {
        return user;
    }

    public void setUser(UserBaseModelDTO user) {
        this.user.set(user);
    }

}
