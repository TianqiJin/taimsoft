/*
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.taim.desktopui.controllers.invoices;

import com.jfoenix.controls.JFXComboBox;
import com.taim.dto.TransactionDTO;
import com.taim.dto.basedtos.UserBaseModelDTO;
import com.taim.model.Transaction;
import com.taim.desktopui.util.InvoiceGenerator;
import com.taim.desktopui.util.VistaNavigator;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static final Logger logger = LoggerFactory.getLogger(InvoiceGenerationController.class);
    private static final DateTimeFormatter dtf = DateTimeFormat.forPattern("MMM-dd-yyyy");
    private Stage dialogStage;
    private File newInvoice;
    private TransactionDTO transaction;
    private BillingInfoController billFromController;
    private BillingInfoController billToController;

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
    private AnchorPane previewPane;
    @FXML
    private JFXComboBox<String> invoiceCreationComboBox;
    @FXML
    private SplitPane invoiceSplitPane;

    @FXML
    private void initialize(){ }

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
        billToController = initBillingInfoPanel(this.transaction.getCustomer(), billingToPane);
        billFromController = initBillingInfoPanel(this.transaction.getStaff(), billingFromPane);
        initInvoiceTransactionPreviewPanel();
    }

    private BillingInfoController initBillingInfoPanel(UserBaseModelDTO user, TitledPane billingPane){
        try {
            FXMLLoader fXMLLoader = new FXMLLoader();
            AnchorPane root = fXMLLoader.load(this.getClass().getResource("/fxml/invoices/BillingInfo.fxml").openStream());
            root.prefHeightProperty().bind(billingPane.heightProperty());
            root.prefWidthProperty().bind(billingPane.widthProperty());
            BillingInfoController controller = fXMLLoader.getController();
            controller.initData(user);
            billingPane.setContent(root);
            return controller;

        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
        }

        return null;
    }

    private void initInvoiceTransactionPreviewPanel(){
        try {
            FXMLLoader fXMLLoader = new FXMLLoader();
            AnchorPane root = fXMLLoader.load(this.getClass().getResource("/fxml/invoices/InvoiceTransactionPreview.fxml").openStream());
            root.prefHeightProperty().bind(previewPane.heightProperty());
            root.prefWidthProperty().bind(previewPane.widthProperty());
            InvoiceTransactionPreviewController controller = fXMLLoader.getController();
            controller.initTransactionDetailsData(this.transaction.getTransactionDetails());
            previewPane.getChildren().addAll(root);
        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    private void initInvoiceInfo(){
        invoiceCreationDateLabel.textProperty().bind(new SimpleStringProperty(dtf.print(this.transaction.getDateCreated())));
        paymentDueDateLabel.textProperty().bind(new SimpleStringProperty(dtf.print(this.transaction.getPaymentDueDate())));
        deliveryDueDateLabel.textProperty().bind(new SimpleStringProperty(dtf.print(this.transaction.getDeliveryDueDate())));
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
        }
//        else if(this.transaction.getTransactionType().equals(Transaction.TransactionType.RETURN)){
//            this.invoiceCreationComboBox.setItems(FXCollections.observableArrayList(InvoiceType.RETURN.getValue()));
//        }

        invoiceCreationComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            billFromController.scanRequiredFields();
            billToController.scanRequiredFields();
            if(billFromController.isReadyForInvoiceGeneration() && billToController.isReadyForInvoiceGeneration() && newValue != null){
                FileChooser fileChooser = new FileChooser();
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf");
                fileChooser.getExtensionFilters().add(extFilter);
                fileChooser.setTitle("Create New Invoice");
                newInvoice = fileChooser.showSaveDialog(dialogStage);
                try{
                    InvoiceGenerator generator = new InvoiceGenerator(newInvoice.getAbsolutePath(), dialogStage);
                    if(newValue.equals(InvoiceType.QUOTATION.getValue())){
                        generator.buildQuotation(transaction, billToController.getUser(), billFromController.getUser(), VistaNavigator.getGlobalProperty());
                    }else if(newValue.equals(InvoiceType.INVOICE.getValue())){
                        generator.buildInvoice(transaction, billToController.getUser(), billFromController.getUser(), VistaNavigator.getGlobalProperty());
                    }else if(newValue.equals(InvoiceType.DELIVERY_NOTICE.getValue())){
                        generator.buildDelivery(transaction, billToController.getUser(), billFromController.getUser(), VistaNavigator.getGlobalProperty());
                    }else if(newValue.equals(InvoiceType.RETURN.getValue())){

                    }
                }catch (Exception e){
                    logger.error(e.getMessage(), e);
                }
                finally {
                    invoiceCreationComboBox.getSelectionModel().clearSelection();
                }
            }
            Platform.runLater(() -> invoiceCreationComboBox.getSelectionModel().clearSelection());
        });
    }

}
