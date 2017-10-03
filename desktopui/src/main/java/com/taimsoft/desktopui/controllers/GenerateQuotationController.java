package com.taimsoft.desktopui.controllers;

import com.taim.dto.*;
import com.taim.model.Customer;
import com.taim.model.Transaction;
import com.taimsoft.desktopui.TaimDesktop;
import com.taimsoft.desktopui.util.AlertBuilder;
import com.taimsoft.desktopui.util.AutoCompleteComboBoxListener;
import com.taimsoft.desktopui.util.ButtonCell;
import com.taimsoft.desktopui.util.RestClientFactory;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleFloatProperty;
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
import org.joda.time.DateTime;

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

    private Stage dialogStage;
    private TaimDesktop taimDesktop;

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

    @FXML
    private TableView<TransactionDetailDTO> transactionTableView;
    @FXML
    private TableColumn<TransactionDetailDTO, Number> productIdCol;
    @FXML
    private TableColumn<TransactionDetailDTO, Number> unitPriceCol;
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
    private Label discountLabel;
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
    private Label pstTaxLabel;
    @FXML
    private Label gstTaxLabel;
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
    private void initialize(){
        confimButtonBinding = Bindings.size(transactionTableView.getItems()).greaterThan(0);
        confirmButton.disableProperty().bind(confimButtonBinding);
        productIdCol.setCellValueFactory(new PropertyValueFactory<>("productId"));
        unitPriceCol.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        qtyCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        sizeCol.setCellValueFactory(new PropertyValueFactory<>("size"));
        discountCol.setCellValueFactory(new PropertyValueFactory<>("discount"));
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
            TransactionDetailDTO p = event.getTableView().getItems().get(event.getTablePosition().getRow());
            p.setQuantity(event.getNewValue().floatValue());
            p.setSaleAmount(new BigDecimal(p.getQuantity() * p.getProduct().getUnitPrice()).setScale(2, BigDecimal.ROUND_HALF_EVEN).floatValue());
            showPaymentDetails();
            refreshTable();
        });

        subTotalCol.setCellValueFactory(param ->
                new SimpleFloatProperty(new BigDecimal(param.getValue().getSaleAmount()).floatValue()));

        //Add constrain around discount field ??
        discountCol.setOnEditCommit(event ->
                (event.getTableView().getItems().get(event.getTablePosition().getRow())).setDiscount(event.getNewValue().intValue()));

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
                        return new ButtonCell(transactionTableView);
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

            transactionDetailDTOObservableList.add(newProductTransaction);
        }
    }

    @FXML
    public void handleAddCustomer(){
        CustomerDTO newCustomer = new CustomerDTO();
        boolean okClicked = taimDesktop.showCustomerEditDialog(newCustomer);
        if(okClicked){
            //newCustomer.setUserName();
            boolean flag = true;
            try{
                RestClientFactory.getCustomerClient().addCustomer(newCustomer);

            }catch(Exception e){
                e.printStackTrace();
                flag = false;
                new AlertBuilder()
                        .alertType(Alert.AlertType.ERROR)
                        .alertTitle("Error")
                        .alertHeaderText("Add New Customer Error")
                        .alertContentText("Unable To Add New Customer" + newCustomer.getFullname() )
                        .build()
                        .showAndWait();
            }finally{
                if(flag){
                    this.customer = newCustomer;
                    customerList.add(this.customer);
                    showCustomerDetails();
                }
            }
        }
    }

    @FXML
    public void handleCancelButton(){
        this.dialogStage.close();
    }

    @FXML
    public TransactionDTO handleConfirmButton() throws IOException, SQLException {
        generateTransaction();
        return this.transaction;
    }


    public GenerateQuotationController(){
        confirmedClicked = false;
        discount = 100;
    }


    public void setDialogStage(Stage dialogStage){
        this.dialogStage = dialogStage;
    }



    private void generateTransaction() throws IOException, SQLException{
        transaction.getTransactionDetails().clear();
        transaction.getTransactionDetails().addAll(transactionDetailDTOObservableList);
        transaction.setSaleAmount(Double.valueOf(totalLabel.getText()));
        transaction.setGst(Double.valueOf(gstTaxLabel.getText()));
        transaction.setPst(Double.valueOf(pstTaxLabel.getText()));
        transaction.setNote(textArea.getText());
        transaction.setCustomer(customer);
        transaction.setStaff(staff);
        transaction.setTransactionType(Transaction.TransactionType.QUOTATION);
//        transaction.getTransactionDetails().forEach( p -> {
//            transaction.getTransactionUpdateRecords().get(transaction.getTransactionUpdateRecords().size() - 1).getProductUpdate().put(p.getProductId(), p.getQuantity());
//        });

        Optional<ButtonType> result = new AlertBuilder()
                .alertType(Alert.AlertType.CONFIRMATION)
                .alertTitle("Transaction Confirmation")
                .alertContentText("Are you sure you want to submit this transaction?\n")
                .alertHeaderText(null)
                .build()
                .showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            RestClientFactory.getTransactionClient().addTransaction(transaction);
            confirmedClicked = true;
        }else{
            //transaction = generateNewTransaction(Transaction.TransactionType.QUOTATION, this.staff.getStaffId(), dateLabel.getText());
            transaction = new TransactionDTO();
        }
    }

    private void showTransactionDetails(){
        typeLabel.setText(transaction.getTransactionType().getValue());
        dateLabel.setText(new SimpleDateFormat("yyyy-MM-dd").format(transaction.getDateCreated()));
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
            discountLabel.setText(this.customer.getCustomerClass().getValue());
            emailLabel.setText(this.customer.getEmail());
            phoneLabel.setText(this.customer.getPhone());
        }
        else{
            addItemButton.setDisable(true);
            fullNameLabel.setText("");
            storeCreditLabel.setText("");
            discountLabel.setText("");
            emailLabel.setText("");
            phoneLabel.setText("");
        }
    }
    /**
     * Show payment details grid pane
     */
    private void showPaymentDetails(){
        if(this.transactionDetailDTOObservableList != null ){
            Iterator<TransactionDetailDTO> iterator = this.transactionDetailDTOObservableList.iterator();
            BigDecimal subTotalAfterDiscount = new BigDecimal(0.00);
            BigDecimal subTotalBeforeDiscount = new BigDecimal(0.00);
            while(iterator.hasNext()){
                TransactionDetailDTO tmp = iterator.next();
                subTotalBeforeDiscount = subTotalBeforeDiscount.add(new BigDecimal(tmp.getSaleAmount()));
                subTotalAfterDiscount = subTotalAfterDiscount.add(new BigDecimal(tmp.getSaleAmount()* (100 - tmp.getDiscount()) / 100));
            }
            BigDecimal paymentDiscount = subTotalBeforeDiscount.subtract(subTotalAfterDiscount).setScale(2, BigDecimal.ROUND_HALF_EVEN);
            BigDecimal pstTax;

            //PST+GST HANDLE??
            if(customer != null && customer.getPstNumber() != null){
                pstTax = new BigDecimal("0.00");
            }else{
                pstTax = new BigDecimal(taimDesktop.getProperty().getPstRate()).multiply(subTotalAfterDiscount).divide(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_EVEN);
            }
            BigDecimal gstTax = new BigDecimal(taimDesktop.getProperty().getGstRate()).multiply(subTotalAfterDiscount).divide(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_EVEN);


            BigDecimal total = subTotalAfterDiscount.add(pstTax).add(gstTax).setScale(2, BigDecimal.ROUND_HALF_EVEN);
            subTotalBeforeDiscount.setScale(2, RoundingMode.HALF_EVEN);

            itemsCountLabel.setText(String.valueOf(this.transactionDetailDTOObservableList.size()));
            subTotalLabel.setText(String.valueOf(subTotalBeforeDiscount.floatValue()));
            paymentDiscountLabel.setText(String.valueOf(paymentDiscount.floatValue()));
            pstTaxLabel.setText(String.valueOf(pstTax.floatValue()));
            gstTaxLabel.setText(String.valueOf(gstTax.floatValue()));
            totalLabel.setText(String.valueOf(total.floatValue()));
        }
        else{
            itemsCountLabel.setText("");
            subTotalLabel.setText("");
            paymentDiscountLabel.setText("");
            pstTaxLabel.setText("");
            gstTaxLabel.setText("");
            totalLabel.setText("");
        }
    }

    /**
     * Initialize the main class for this class
     * @param taimDesktop
     */
    public void setMainClass(TaimDesktop taimDesktop){
        this.taimDesktop = taimDesktop;
        this.staff = this.taimDesktop.getStaff();
        //either edit or generate new quotation
        if (taimDesktop.getTransaction()==null) {
            this.transaction = new TransactionDTO();
            transaction.setTransactionType(Transaction.TransactionType.QUOTATION);
            transaction.setStaff(staff);
            transaction.setDateCreated(DateTime.now());
        }else{
            this.transaction = taimDesktop.getTransaction();
        }
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
        showStaffDetails();
    }


    public boolean isConfirmedClicked(){
        return this.confirmedClicked;
    }

    private void refreshTable(){
        transactionTableView.getColumns().get(0).setVisible(false);
        transactionTableView.getColumns().get(0).setVisible(true);
    }

    //@Override
    public void initPanelDetails(){
        showTransactionDetails();
        showStaffDetails();
        showCustomerDetails();
        showPaymentDetails();
    }

    //@Override
    public void initDataFromDB(){
        Task<List<CustomerDTO>> customersTask = new Task<List<CustomerDTO>>() {
            @Override
            protected List<CustomerDTO> call() throws Exception {
                return RestClientFactory.getCustomerClient().getCustomerList();
            }
        };
        Task<List<ProductDTO>> productsTask = new Task<List<ProductDTO>>() {
            @Override
            protected List<ProductDTO> call() throws Exception {
                return RestClientFactory.getProductClient().getProductList();
            }
        };
        customersTask.setOnSucceeded(event ->{
            this.customerList = customersTask.getValue();
            if(this.transaction.getCustomer().getFullname() != null){
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
            System.out.println((event.getSource().getMessage());
            new AlertBuilder()
                    .alertType(Alert.AlertType.ERROR)
                    .alertHeaderText("Database Error!")
                    .alertContentText("Unable to fetch customer information from the database!")
                    .build()
                    .showAndWait();
            dialogStage.close();
        });
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
            System.out.println(event.getSource().getMessage());
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


    private Integer returnDiscount(){
        if(this.customer != null){
            if(customer.getCustomerClass()== Customer.CustomerClass.CLASSA){
                return this.taimDesktop.getProperty().getUserClass().getClassA();
            }else if(customer.getCustomerClass()== Customer.CustomerClass.CLASSB){
                return this.saleSystem.getProperty().getUserClass().getClassB();
            }else if(customer.getCustomerClass()== Customer.CustomerClass.CLASSC){
                return this.saleSystem.getProperty().getUserClass().getClassC();
            }
        }
        return null;
    }


}
