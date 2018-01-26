package com.taim.desktopui.controllers.transactions;

import com.taim.desktopui.controllers.edit.CustomerEditDialogController;
import com.taim.desktopui.util.*;
import com.taim.dto.*;
import com.taim.model.Transaction;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.joda.time.DateTime;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;


/**
 * Created by jiawei.liu on 9/17/17.
 */

public class GenerateQuotationController {
    private static final Logger logger = LoggerFactory.getLogger(GenerateQuotationController.class);

    private Stage dialogStage;

    private CustomerDTO customer;
    private StaffDTO staff;
    private List<CustomerDTO> customerList;
    private List<ProductDTO> productList;
    private ObservableList<TransactionDetailDTO> transactionDetailDTOObservableList;
    private TransactionDTO transaction;
    private StringBuffer errorMsgBuilder;
    private boolean confirmedClicked;
    private BooleanBinding confimButtonBinding;
    private int discount;
    private Executor executor;
    private Mode mode;
    private Map<Integer, Double> oldProductVirtualNumMap;


    @FXML
    private TableView<TransactionDetailDTO> transactionTableView;
    @FXML
    private TableColumn<TransactionDetailDTO, String> productIdCol;
    @FXML
    private TableColumn<TransactionDetailDTO, Number> unitPriceCol;
    @FXML
    private TableColumn<TransactionDetailDTO, Number> virtualNumCol;
    @FXML
    private TableColumn<TransactionDetailDTO, Number> actualNumCol;
    @FXML
    private TableColumn<TransactionDetailDTO, Number> qtyCol;
    @FXML
    private TableColumn<TransactionDetailDTO, Number> subTotalCol;
    @FXML
    private TableColumn<TransactionDetailDTO, Number> discountCol;
    @FXML
    private TableColumn<TransactionDetailDTO, Number> totalCol;
    @FXML
    private TableColumn<TransactionDetailDTO, String> sizeCol;
    @FXML
    private TableColumn deleteCol;
    @FXML
    private TableColumn<TransactionDetailDTO, String> remarkCol;


    //Transaction Information Labels
    @FXML
    private Label typeLabel;
    @FXML
    private Label dateLabel;

    //Staff Information Labels
    @FXML
    private Label staffFullNameLabel;
    @FXML
    private Label staffPhoneLabel;
    @FXML
    private Label staffPositionLabel;
    @FXML
    private Label staffEmail;

    //Customer Details Labels
    @FXML
    private Label fullNameLabel;
    @FXML
    private Label storeCreditLabel;
    @FXML
    private Label userTypeLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label phoneLabel;

    //Items Information Labels
    @FXML
    private Label itemsCountLabel;
    @FXML
    private Label subTotalLabel;
    @FXML
    private Label paymentDiscountLabel;
    @FXML
    private TextField pstTaxField;
    @FXML
    private TextField gstTaxField;
    @FXML
    private Label totalLabel;

    @FXML
    private Button addItemButton;
    @FXML
    private Button confirmButton;
    @FXML
    private Button cancelButton;
    @FXML
    private ComboBox<String> customerComboBox;
    @FXML
    private ComboBox<String> customerPhoneComboBox;
    @FXML
    private ComboBox<String> productComboBox;
    @FXML
    private TextArea textArea;

    @FXML
    private SplitPane transactionGeneratePane;


    @FXML
    private void initialize(){
        confimButtonBinding = Bindings.size(transactionTableView.getItems()).greaterThan(0);
        confirmButton.disableProperty().bind(confimButtonBinding);
        productIdCol.setCellValueFactory(p->new SimpleStringProperty(p.getValue().getProduct().getSku()));
        unitPriceCol.setCellValueFactory(u->new SimpleFloatProperty(new BigDecimal(u.getValue().getProduct().getUnitPrice()).floatValue()));
        qtyCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        sizeCol.setCellValueFactory(s->new SimpleStringProperty(SizeHelper.getSizeString(s.getValue().getProduct())));
        discountCol.setCellValueFactory(new PropertyValueFactory<>("discount"));
        discountCol.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Number>() {
            @Override
            public String toString(Number object) {
                return String.valueOf(object);
            }

            @Override
            public Float fromString(String string) {
                return Float.valueOf(string);
            }
        }));

        actualNumCol.setCellValueFactory(param ->
                new SimpleFloatProperty(new BigDecimal(param.getValue().getProduct().getTotalNum()).floatValue()));

        virtualNumCol.setCellValueFactory(param ->
                new SimpleFloatProperty(new BigDecimal(param.getValue().getProduct().getVirtualTotalNum()).floatValue()));
        remarkCol.setCellValueFactory(new PropertyValueFactory<>("note"));
        remarkCol.setOnEditCommit(event ->
            (event.getTableView().getItems().get(event.getTablePosition().getRow())).setNote(event.getNewValue()));

        remarkCol.setCellFactory(TextFieldTableCell.forTableColumn());
        qtyCol.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Number>() {
            @Override
            public String toString(Number object) {
                return String.valueOf(object);
            }

            @Override
            public Float fromString(String string) {
                return Float.valueOf(string);
            }
        }));

        qtyCol.setOnEditCommit(event -> {
            int oldValue = event.getOldValue().intValue();
            TransactionDetailDTO p = event.getTableView().getItems().get(event.getTablePosition().getRow());
            p.setQuantity(event.getNewValue().floatValue());
            p.setSaleAmount(new BigDecimal(p.getQuantity() * p.getProduct().getUnitPrice()).setScale(2, BigDecimal.ROUND_HALF_EVEN).floatValue());
            showPaymentDetails();
            p.getProduct().setVirtualTotalNum(p.getProduct().getVirtualTotalNum()+oldValue-event.getNewValue().intValue());
            refreshTable();
        });

        subTotalCol.setCellValueFactory(param ->
                new SimpleFloatProperty(new BigDecimal(param.getValue().getSaleAmount()).floatValue()));

        discountCol.setOnEditCommit(event ->{
            int oldValue= event.getOldValue().intValue();
            TransactionDetailDTO p = event.getTableView().getItems().get(event.getTablePosition().getRow());
            int newDiscount=validateDiscountEntered(oldValue,event.getNewValue().intValue());
            p.setDiscount(newDiscount);
            showPaymentDetails();
            refreshTable();

            (event.getTableView().getItems().get(event.getTablePosition().getRow())).setDiscount(newDiscount);
        });

        totalCol.setCellValueFactory(param ->
                new SimpleFloatProperty(new BigDecimal(param.getValue().getSaleAmount()* (100 - param.getValue().getDiscount()) / 100)
                        .setScale(2, RoundingMode.HALF_EVEN).floatValue()));

        deleteCol.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<TransactionDetailDTO, Boolean>,
                                        ObservableValue<Boolean>>() {
                    @Override
                    public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<TransactionDetailDTO, Boolean> p) {
                        return new SimpleBooleanProperty(p.getValue() != null);
                    }
                });

        deleteCol.setCellFactory(
                new Callback<TableColumn<TransactionDetailDTO, Boolean>, TableCell<TransactionDetailDTO, Boolean>>() {
                    @Override
                    public TableCell<TransactionDetailDTO, Boolean> call(TableColumn<TransactionDetailDTO, Boolean> p) {
                        return new ButtonCell(transactionTableView,oldProductVirtualNumMap,mode==Mode.EDIT,true);
                    }

                });
        executor = Executors.newCachedThreadPool(r -> {
            Thread t = new Thread(r);
            t.setDaemon(true);
            return t;
        });
    }




    @FXML
    public void handleAddItem(){
        ProductDTO selectedProduct = productList
                .stream()
                .filter(product -> product.getSku().equals(productComboBox.getSelectionModel().getSelectedItem()))
                .findFirst()
                .get();
        List<String> productIdList = transactionDetailDTOObservableList.stream()
                .map(t -> t.getProduct().getSku())
                .collect(Collectors.toList());
        if(productIdList.contains(selectedProduct.getSku())){
            new AlertBuilder()
                    .alertType(Alert.AlertType.ERROR)
                    .alertContentText("Product Add Error")
                    .alertContentText(selectedProduct.getSku() + " has already been added!")
                    .build()
                    .showAndWait();
        }else{
            TransactionDetailDTO newProductTransaction = new TransactionDetailDTO();
            newProductTransaction.setProduct(selectedProduct);
            newProductTransaction.setDiscount(0);
            newProductTransaction.setQuantity(0);
            newProductTransaction.setSaleAmount(selectedProduct.getUnitPrice()*newProductTransaction.getQuantity());
            transactionDetailDTOObservableList.add(newProductTransaction);
            oldProductVirtualNumMap.put(selectedProduct.getId(),selectedProduct.getVirtualTotalNum());
        }
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
            this.customer = RestClientFactory.getCustomerClient().getByName(controller.getCustomer().getFullname());
            customerList.add(this.customer);
            showCustomerDetails();
        }
    }

    @FXML
    public void handleCancelButton(){
        this.dialogStage.close();
    }

//    @FXML
//    public TransactionDTO handleConfirmButton() throws IOException, SQLException {
//        generateTransaction();
//        if(isConfirmedClicked()) {
//            dialogStage.close();
//        }
//        return this.transaction;
//    }


    public GenerateQuotationController(){
        confirmedClicked = false;
        discount = 100;
    }


    public void setDialogStage(Stage dialogStage){
        this.dialogStage = dialogStage;
    }


    /**
     * Initialize the main class for this class
     */

    public void setMainClass(TransactionDTO transactionFromAbove){

        //either edit or generate new quotation
        if (transactionFromAbove==null) {
            this.mode=Mode.CREATE;
            this.staff = VistaNavigator.getGlobalStaff();
            this.transaction = new TransactionDTO();
            transaction.setTransactionType(Transaction.TransactionType.QUOTATION);
            transaction.setFinalized(false);
            transaction.setStaff(staff);
            transaction.setDateCreated(DateTime.now());
        }else{
            this.mode=Mode.EDIT;
            this.staff = transactionFromAbove.getStaff();
            this.transaction = transactionFromAbove;
            this.customer = transactionFromAbove.getCustomer();
            if(transaction.isFinalized()){
                new AlertBuilder()
                        .alertType(Alert.AlertType.ERROR)
                        .alertContentText("This transaction is already finalized! You cannot edit on it anymore")
                        .build()
                        .showAndWait();
                confirmButton.setDisable(true);
            }

        }
        updatePrevProductNum();
        this.transactionDetailDTOObservableList = FXCollections.observableArrayList(transaction.getTransactionDetails());
        transactionTableView.setItems(transactionDetailDTOObservableList);
        transactionDetailDTOObservableList.addListener(new ListChangeListener<TransactionDetailDTO>() {
            @Override
            public void onChanged(Change<? extends TransactionDetailDTO> c) {
                while(c.next()){
                    if( c.wasAdded() || c.wasRemoved()){
                        showPaymentDetails();
                    }
                }
            }
        });
    }

    /**
     * Load Data From DB (Customer and Product)
     */
    public void initDataFromDB(){
        //load list of products and customers
        Task<List<CustomerDTO>> customersTask = new Task<List<CustomerDTO>>() {
            @Override
            protected List<CustomerDTO> call() throws Exception {
                return RestClientFactory.getCustomerClient().getList();
            }
        };
        Task<List<ProductDTO>> productsTask = new Task<List<ProductDTO>>() {
            @Override
            protected List<ProductDTO> call() throws Exception {
                return RestClientFactory.getProductClient().getList();
            }
        };

        customersTask.setOnSucceeded(event ->{
            this.customerList = customersTask.getValue();

            if(this.transaction.getCustomer()!=null && this.transaction.getCustomer().getFullname() != null){
                Optional<CustomerDTO> customer =  customerList.stream().filter(p -> p.getFullname().equals(transaction.getCustomer().getFullname())).findFirst();
                if(customer.isPresent()){
                    this.customer = customer.get();
                    showCustomerDetails();
                }
            }
            List<String> tmpCustomerList = new ArrayList<>();
            for(CustomerDTO customer: this.customerList){
//                customer.constructCustomerInfo();
//                tmpCustomerList.add(customer.getCustomerInfo());
                tmpCustomerList.add(customer.getFullname());
            }
            customerComboBox.setItems(FXCollections.observableArrayList(tmpCustomerList));
            customerComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
                for(CustomerDTO tmpCustomer: this.customerList){
                    if(tmpCustomer.getFullname().equals(newValue)){
                        customer = tmpCustomer;
                        showCustomerDetails();
                        break;
                    }
                }
            });
            List<String> tmpCustomerPhoneList = new ArrayList<>();
            for(CustomerDTO customer: this.customerList){
//                customer.constructCustomerPhoneInfo();
//                tmpCustomerPhoneList.add(customer.getCustomerPhoneInfo());
                tmpCustomerPhoneList.add(customer.getPhone());
            }
            customerPhoneComboBox.setItems(FXCollections.observableArrayList(tmpCustomerPhoneList));
            customerPhoneComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
                for(CustomerDTO tmpCustomer: this.customerList){
                    if(tmpCustomer.getPhone() != null && tmpCustomer.getPhone().equals(newValue)){
                        customer = tmpCustomer;
                        showCustomerDetails();
                        break;
                    }
                }
            });
            new AutoCompleteComboBoxListener<>(customerComboBox);
            new AutoCompleteComboBoxListener<>(customerPhoneComboBox);
        });

        customersTask.setOnFailed(event -> {
            logger.error(customersTask.getException().getMessage());
            new AlertBuilder()
                    .alertType(Alert.AlertType.ERROR)
                    .alertHeaderText("Database Error!")
                    .alertContentText("Unable to fetch customer information from the database!")
                    .build()
                    .showAndWait();
            dialogStage.close();
        });
        //product
        productsTask.setOnSucceeded(event ->{
            this.productList = productsTask.getValue();
            List<String> tmpProductList = productList
                    .stream()
                    .map(product -> product.getSku())
                    .collect(Collectors.toList());
            productComboBox.setItems(FXCollections.observableArrayList(tmpProductList));
            new AutoCompleteComboBoxListener<>(productComboBox);
        });
        productsTask.setOnFailed(event -> {
            logger.error(productsTask.getException().getMessage());
            new AlertBuilder()
                    .alertType(Alert.AlertType.ERROR)
                    .alertHeaderText("Database Error!")
                    .alertContentText("Unable to fetch product information from the database!")
                    .build()
                    .showAndWait();
            dialogStage.close();
        });
        executor.execute(customersTask);
        executor.execute(productsTask);
    }

    /**
     * Initial Panel Details
     */
    public void initPanelDetails(){
        showTransactionDetails();
        showStaffDetails();
        showCustomerDetails();
        showPaymentDetails();
    }


    private void showTransactionDetails(){
        typeLabel.setText(transaction.getTransactionType().getValue());
        dateLabel.setText(new SimpleDateFormat("yyyy-MM-dd").format(transaction.getDateCreated().toDate()));
        textArea.setText(transaction.getNote());
    }

    private void showStaffDetails(){
        if(this.staff != null){
            staffFullNameLabel.setText(this.staff.getFullname());
            staffPhoneLabel.setText(this.staff.getPhone());
            staffPositionLabel.setText(this.staff.getPosition().toString());
            staffEmail.setText(this.staff.getEmail());
        }else{
            staffFullNameLabel.setText("");
            staffPhoneLabel.setText("");
            staffPositionLabel.setText("");
            staffEmail.setText("");
        }
    }


    /**
     * Show customer details grid pane
     */

    private void showCustomerDetails(){
        if(this.customer != null){
            addItemButton.setDisable(false);
            fullNameLabel.setText(this.customer.getFullname());
            storeCreditLabel.setText(String.valueOf(this.customer.getStoreCredit()));
            userTypeLabel.setText(this.customer.getUserType().getValue());
            emailLabel.setText(this.customer.getEmail());
            phoneLabel.setText(this.customer.getPhone());
        }
        else{
            addItemButton.setDisable(true);
            fullNameLabel.setText("");
            storeCreditLabel.setText("");
            userTypeLabel.setText("");
            emailLabel.setText("");
            phoneLabel.setText("");
        }
    }

/**
     * Show payment details grid pane
     */

    private void showPaymentDetails(){
        if(this.transactionDetailDTOObservableList != null ){
            int pstNum = VistaNavigator.getGlobalProperty().getPstRate();
            int gstNum = VistaNavigator.getGlobalProperty().getGstRate();
            Iterator<TransactionDetailDTO> iterator = this.transactionDetailDTOObservableList.iterator();
            BigDecimal subTotalAfterDiscount = new BigDecimal(0.00);
            BigDecimal subTotalBeforeDiscount = new BigDecimal(0.00);
            while(iterator.hasNext()){
                TransactionDetailDTO tmp = iterator.next();
                subTotalBeforeDiscount = subTotalBeforeDiscount.add(new BigDecimal(tmp.getSaleAmount()));
                subTotalAfterDiscount = subTotalAfterDiscount.add(new BigDecimal(tmp.getSaleAmount()* (100 - tmp.getDiscount()) / 100));
            }
            BigDecimal paymentDiscount = subTotalBeforeDiscount.subtract(subTotalAfterDiscount).setScale(2, BigDecimal.ROUND_HALF_EVEN);

            BigDecimal pstTax = new BigDecimal(pstNum).multiply(subTotalAfterDiscount).divide(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_EVEN);
            BigDecimal gstTax = new BigDecimal(gstNum).multiply(subTotalAfterDiscount).divide(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_EVEN);

            BigDecimal total = subTotalAfterDiscount.add(pstTax).add(gstTax).setScale(2, BigDecimal.ROUND_HALF_EVEN);
            subTotalBeforeDiscount.setScale(2, RoundingMode.HALF_EVEN);

            itemsCountLabel.setText(String.valueOf(this.transactionDetailDTOObservableList.size()));
            subTotalLabel.setText(String.valueOf(subTotalBeforeDiscount.floatValue()));
            paymentDiscountLabel.setText(String.valueOf(paymentDiscount.floatValue()));
            pstTaxField.setText(String.valueOf(pstTax.floatValue()));
            gstTaxField.setText(String.valueOf(gstTax.floatValue()));
            totalLabel.setText(String.valueOf(total.floatValue()));
        }
        else{
            itemsCountLabel.setText("");
            subTotalLabel.setText("");
            paymentDiscountLabel.setText("");
            pstTaxField.setText("");
            gstTaxField.setText("");
            totalLabel.setText("");
        }
    }


    @FXML
    public void handleConfirmButton() throws IOException, SQLException{
        transaction.getTransactionDetails().clear();
        transactionDetailDTOObservableList.forEach(t->{
            if (t.getDateCreated()==null){
                t.setDateCreated(DateTime.now());
            }
            //t.setDateModified(DateTime.now());
        });
        transaction.getTransactionDetails().addAll(transactionDetailDTOObservableList);
        transaction.setSaleAmount(Double.valueOf(totalLabel.getText()));
        transaction.setGst(Double.valueOf(gstTaxField.getText()));
        transaction.setPst(Double.valueOf(pstTaxField.getText()));
        transaction.setNote(textArea.getText());
        transaction.setCustomer(customer);
        //transaction.setDateModified(DateTime.now());



        Optional<ButtonType> result = new AlertBuilder()
                .alertType(Alert.AlertType.CONFIRMATION)
                .alertTitle("Transaction Confirmation")
                .alertContentText("Are you sure you want to submit this transaction?\n")
                .alertHeaderText(null)
                .build()
                .showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            transaction.setCustomer(RestClientFactory.getCustomerClient().getByName(customer.getFullname()));
            transaction.setStaff(RestClientFactory.getStaffClient().getByName(staff.getUserName()));
            transaction.getTransactionDetails().forEach(t->{
                ProductDTO tmpProduct = RestClientFactory.getProductClient().getById(t.getProduct().getId());
                double newVirtNum = t.getProduct().getVirtualTotalNum()-oldProductVirtualNumMap.get(tmpProduct.getId())+tmpProduct.getVirtualTotalNum();
                tmpProduct.setVirtualTotalNum(newVirtNum);
                t.setProduct(tmpProduct);
            });
            Task<TransactionDTO> saveUpdateTransactionTask = new Task<TransactionDTO>() {
                @Override
                protected TransactionDTO call() throws Exception {
                    return RestClientFactory.getTransactionClient().saveOrUpdateAll(transaction);
                }
            };
            saveUpdateTransactionTask.setOnSucceeded(event -> {
                this.transaction = saveUpdateTransactionTask.getValue();
                confirmedClicked = true;
                dialogStage.close();
            });
            saveUpdateTransactionTask.exceptionProperty().addListener((observable, oldValue, newValue) -> {
                if(newValue != null) {
                    Exception ex = (Exception) newValue;
                    logger.error(ExceptionUtils.getRootCause(ex).getMessage());
                    JSONObject errorMsg = new JSONObject(ExceptionUtils.getRootCause(ex).getMessage());
                    new AlertBuilder().alertType(Alert.AlertType.ERROR)
                            .alertContentText(errorMsg.getString("taimErrorMessage"))
                            .build()
                            .showAndWait();
                    if(errorMsg.getInt("taimErrorCode") == 1){
                        Task<TransactionDTO> getTransactionTask = new Task<TransactionDTO>() {
                            @Override
                            protected TransactionDTO call() throws Exception {
                                return RestClientFactory.getTransactionClient().getById(transaction.getId());
                            }
                        };
                        getTransactionTask.setOnSucceeded(event -> {
                            setMainClass(getTransactionTask.getValue());
                            initPanelDetails();
                        });
                        getTransactionTask.exceptionProperty().addListener((observable1, oldValue1, newValue1) -> {
                            if(newValue1 != null) {
                                Exception newEx = (Exception) newValue1;
                                String newExMsg = ExceptionUtils.getRootCause(newEx).getMessage();
                                logger.error(newExMsg);
                                JSONObject newErrorMessage = new JSONObject(newExMsg);
                                new AlertBuilder().alertType(Alert.AlertType.ERROR)
                                        .alertHeaderText(newErrorMessage.getString("taimErrorMessage"))
                                        .build()
                                        .showAndWait();
                            }});
                        executor.execute(getTransactionTask);
                    }
                }
            });
            executor.execute(saveUpdateTransactionTask);
        }
    }



    public boolean isConfirmedClicked(){
        return this.confirmedClicked;
    }

    private void refreshTable(){
        transactionTableView.getColumns().get(0).setVisible(false);
        transactionTableView.getColumns().get(0).setVisible(true);
    }


    public TransactionDTO getTransaction(){return this.transaction;}

    private enum Mode{
        CREATE,EDIT;
    }



    private int validateDiscountEntered(int oldValue, int newValue){
        if (this.customer!=null && this.customer.getCustomerClass()!=null) {
            if(newValue <= this.customer.getCustomerClass().getCustomerDiscount()){
                return newValue;
            }else{
                new AlertBuilder()
                        .alertType(Alert.AlertType.ERROR)
                        .alertHeaderText("Discount Error!")
                        .alertContentText("Exceed Max discount rate for this customer!")
                        .build()
                        .showAndWait();
            }
        }
        return oldValue;
    }

    private void updatePrevProductNum(){
        oldProductVirtualNumMap= new HashMap<>();
        this.transaction.getTransactionDetails().forEach(t->{
            oldProductVirtualNumMap.put(t.getProduct().getId(),t.getProduct().getVirtualTotalNum());
        });
    }
}

