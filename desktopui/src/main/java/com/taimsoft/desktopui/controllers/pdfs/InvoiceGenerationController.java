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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

/**
 * Created by tjin on 3/7/2016.
 */
public class InvoiceGenerationController {
    private Stage dialogStage;
    private File selectedDirectory;
    private TransactionDTO transaction;

    @FXML
    private Label invoiceDirectoryLabel;

    @FXML
    private Label invoiceCreationDateLabel;
    @FXML
    private Label paymentDueDateLabel;
    @FXML
    private Label deliveryDueDateLabel;
    @FXML
    private Label invoiceNumLabel;
    @FXML
    private AnchorPane billingInfoPane;

    @FXML
    private void initialize(){
    }

    public InvoiceGenerationController(){}

    @FXML
    public void handleInvoiceDirectory(){
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Invoice Directory");
        selectedDirectory = directoryChooser.showDialog(dialogStage);
        if(selectedDirectory != null){
            invoiceDirectoryLabel.setText(selectedDirectory.toString());
        }
    }

//    @FXML
//    public void handleConfirmButton(){
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
//        }
    @FXML
    public void handleCancelButton(){
        dialogStage.close();
    }

    public void setDialogStage(Stage stage){
        this.dialogStage = stage;
    }

    public void initData(TransactionDTO transaction){
        this.transaction = transaction;
        initBillingInfoPanel(this.transaction.getCustomer());
        initBillingInfoPanel(this.transaction.getStaff());
    }

    private void initBillingInfoPanel(UserBaseModelDTO user){
        try {
            FXMLLoader fXMLLoader = new FXMLLoader();
            AnchorPane root;
            root = fXMLLoader.load(this.getClass().getResource("/fxml/pdfs/BillingInfo.fxml").openStream());
            root.prefHeightProperty().bind(billingInfoPane.heightProperty());
            root.prefWidthProperty().bind(billingInfoPane.widthProperty());
            BillingInfoController controller = fXMLLoader.getController();
            controller.initData(user);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void initInvoiceInfo(){
        invoiceCreationDateLabel.textProperty().bindBidirectional(this.transaction.in);
        paymentDueDateLabel;
         deliveryDueDateLabel;
        invoiceNumLabel;
    }

}
