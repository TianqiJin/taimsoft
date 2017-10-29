/*
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.taimsoft.desktopui.controllers.pdfs;

import com.taim.dto.TransactionDTO;
import com.taim.dto.basedtos.UserBaseModelDTO;
import com.taim.model.Transaction;
import com.taimsoft.desktopui.util.AlertBuilder;
import com.taimsoft.desktopui.util.InvoiceGenerator;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

/**
 * Created by tjin on 3/7/2016.
 */
public class InvoiceGenerationController {
    public enum InvoiceType{
        QUOTATION("Quotation"),
        INVOICE("Purchase Invoice"),
        DELIVERY_NOTICE("Delivery Notice"),
        RETURN("Return Invoice");

        private String value;

        InvoiceType(String vvalue){
            this.value = vvalue;
        }

        public String getValue(){
            return this.value;
        }
    }

    private Stage dialogStage;
    private File newInvoice;
    private TransactionDTO transaction;
//
//    @FXML
//    private Label invoiceDirectoryLabel;
    @FXML
    private Label invoiceCreationDateLabel;
    @FXML
    private Label paymentDueDateLabel;
    @FXML
    private Label deliveryDueDateLabel;
    @FXML
    private Label invoiceNumLabel;
    @FXML
    private TitledPane billingFromPane;
    @FXML
    private TitledPane billingToPane;
    @FXML
    private ComboBox<String> invoiceCreationComboBox;
    @FXML
    private void initialize(){ }

    @FXML
    public void handleConfirmButton(){
//        errorMsgBuilder = new StringBuilder();
//        if(isInvoicedirectoryValid()){
//            try{
//                InvoiceGenerator generator = new InvoiceGenerator(selectedDirectory.toString(), this.saleSystem);
//                if(isFieldValid()){
//                    Address address = new Address(streetField.getText().trim(), cityField.getText().trim(), postalCodeField.getText().trim());
//                    if(invoiceCheckBox.isSelected()){
//                        generator.buildInvoice(transaction,customer,staff, address);
//                    }
//                    if(quotationInvoiceCheckBox.isSelected()){
//                        generator.buildQuotation(transaction, customer, staff, address);
//                    }
//                    if(deliveryInvoiceCheckbox.isSelected()){
//                        generator.buildDelivery(transaction, customer, staff, address);
//                    }
//                    if(poCheckBox.isSelected()){
//                        generator.buildPo(transaction, customer, staff, address);
//                    }
//                }else{
//                    new AlertBuilder()
//                            .alertType(Alert.AlertType.ERROR)
//                            .alertHeaderText("Please fix the following error")
//                            .alertContentText(errorMsgBuilder.toString())
//                            .alertTitle("Invoice Error")
//                            .build()
//                            .showAndWait();
//                }
//            }catch(Exception e){
//                e.printStackTrace();
//            }
//            if(errorMsgBuilder.length() == 0){
//                dialogStage.close();
//            }
//        }else{
//            new AlertBuilder()
//                    .alertType(Alert.AlertType.ERROR)
//                    .alertHeaderText("Please fix the following error")
//                    .alertContentText(errorMsgBuilder.toString())
//                    .alertTitle("Invoice Error")
//                    .build()
//                    .showAndWait();
//            }
        }

    @FXML
    public void handleCancelButton(){
        dialogStage.close();
    }

    public void setDialogStage(Stage stage){
        this.dialogStage = stage;
    }

    public void initData(TransactionDTO transaction){
        this.transaction = transaction;
        initInvoiceInfo();
        initComboBox();
        initBillingInfoPanel(this.transaction.getCustomer(), billingToPane);
        initBillingInfoPanel(this.transaction.getStaff(), billingFromPane);
    }

    private void initBillingInfoPanel(UserBaseModelDTO user, TitledPane billingPane){
        try {
            FXMLLoader fXMLLoader = new FXMLLoader();
            AnchorPane root = fXMLLoader.load(this.getClass().getResource("/fxml/pdfs/BillingInfo.fxml").openStream());
            root.prefHeightProperty().bind(billingPane.heightProperty());
            root.prefWidthProperty().bind(billingPane.widthProperty());
            BillingInfoController controller = fXMLLoader.getController();
            controller.initData(user);
            billingPane.setContent(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void initInvoiceInfo(){
        invoiceCreationDateLabel.textProperty().bind(this.transaction.dateCreatedProperty().asString());
        paymentDueDateLabel.textProperty().bind(this.transaction.paymentDueDateProperty().asString());
        deliveryDueDateLabel.textProperty().bind(this.transaction.deliveryDueDateProperty().asString());
        invoiceNumLabel.textProperty().bind(this.transaction.idProperty().asString());
    }

    private void initComboBox(){
        if(this.transaction.getTransactionType().equals(Transaction.TransactionType.QUOTATION)){
            this.invoiceCreationComboBox.setItems(FXCollections.observableArrayList(InvoiceType.QUOTATION.getValue()));
        }else if(this.transaction.getTransactionType().equals(Transaction.TransactionType.INVOICE)){
            this.invoiceCreationComboBox.setItems(FXCollections.observableArrayList(
                    InvoiceType.QUOTATION.getValue(),
                    InvoiceType.INVOICE.getValue(),
                    InvoiceType.DELIVERY_NOTICE.getValue()));
        }else if(this.transaction.getTransactionType().equals(Transaction.TransactionType.RETURN)){
            this.invoiceCreationComboBox.setItems(FXCollections.observableArrayList(InvoiceType.RETURN.getValue()));
        }

        invoiceCreationComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Create New Invoice");
            newInvoice = fileChooser.showSaveDialog(dialogStage);
            InvoiceGenerator generator = new InvoiceGenerator(newInvoice.getAbsolutePath());
            if(newValue.equals(InvoiceType.QUOTATION.getValue())){
                generator.buildQuotation(transaction, );
            }else if(newValue.equals(InvoiceType.INVOICE.getValue())){

            }else if(newValue.equals(InvoiceType.DELIVERY_NOTICE.getValue())){

            }else if(newValue.equals(InvoiceType.RETURN.getValue())){

            }
        });
    }

    private void generateInvoice(){
        try{
            InvoiceGenerator generator = new InvoiceGenerator(newInvoice.getAbsolutePath());
            if(isFieldValid()){
                Address address = new Address(streetField.getText().trim(), cityField.getText().trim(), postalCodeField.getText().trim());
                if(invoiceCheckBox.isSelected()){
                    generator.buildInvoice(transaction,customer,staff, address);
                }
                if(quotationInvoiceCheckBox.isSelected()){
                    generator.buildQuotation(transaction, customer, staff, address);
                }
                if(deliveryInvoiceCheckbox.isSelected()){
                    generator.buildDelivery(transaction, customer, staff, address);
                }
                if(poCheckBox.isSelected()){
                    generator.buildPo(transaction, customer, staff, address);
                }
            }else{
                new AlertBuilder()
                        .alertType(Alert.AlertType.ERROR)
                        .alertHeaderText("Please fix the following error")
                        .alertContentText(errorMsgBuilder.toString())
                        .alertTitle("Invoice Error")
                        .build()
                        .showAndWait();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
