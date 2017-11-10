package com.taimsoft.desktopui.util;

import com.taim.dto.*;
import com.taim.model.Transaction;
import com.taimsoft.desktopui.controllers.*;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by jiawei.liu on 10/4/17.
 */
public class TransactionPanelLoader {

    public static TransactionDTO loadQuotation(TransactionDTO transaction){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(TransactionPanelLoader.class.getResource("/fxml/GenerateQuotation.fxml"));
        AnchorPane page = null;
        try {
            page = loader.load();
            page.getStylesheets().add(TransactionPanelLoader.class.getResource("/css/bootstrap3.css").toExternalForm());
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Transaction Panel");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            //Set the primaryStage bound to the maximum of the screen
            Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
            dialogStage.setX(bounds.getMinX());
            dialogStage.setY(bounds.getMinY());
            dialogStage.setWidth(bounds.getWidth());
            dialogStage.setHeight(bounds.getHeight());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            GenerateQuotationController controller = loader.getController();
            controller.setMainClass(transaction);
            controller.setDialogStage(dialogStage);
            controller.initDataFromDB();
            controller.initPanelDetails();
            dialogStage.showAndWait();

            if (controller.isConfirmedClicked()){
                transaction = controller.getTransaction();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return transaction;
    }

    public static TransactionDTO loadInvoice(TransactionDTO transaction){
        if(transaction==null){
            System.out.println("Please select a valid transaction!");
            return null;
        }

        if(transaction.getTransactionType()!= Transaction.TransactionType.INVOICE && transaction.getTransactionType()!= Transaction.TransactionType.QUOTATION){
            System.out.println("Please either select an invoice to edit or select quotation to generat invoice!!");
            return null;
        }

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(TransactionPanelLoader.class.getResource("/fxml/GenerateInvoice.fxml"));
        AnchorPane page = null;
        try {
            page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Transaction Panel");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            GenerateInvoiceController controller = loader.getController();
            controller.setMainClass(transaction);
            controller.setDialogStage(dialogStage);
            controller.initDataFromDB();
            controller.initPanelDetails();
            dialogStage.showAndWait();

            if (controller.isConfirmedClicked()){
                transaction = controller.getTransaction();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return transaction;
    }


    public static TransactionDTO loadReturn(TransactionDTO transaction){
        if(transaction==null){
            System.out.println("Please select a valid transaction!");
            return null;
        }

        if(transaction.getTransactionType()!= Transaction.TransactionType.RETURN && transaction.getTransactionType()!= Transaction.TransactionType.INVOICE){
            System.out.println("Please either select an invoice to return or select return to generate!!");
            return null;
        }
        if (transaction.getTransactionType()== Transaction.TransactionType.INVOICE && !transaction.isFinalized()){
            System.out.println("Please select a finalized invoice transaction to generate return transaction!!");
            return null;
        }

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(TransactionPanelLoader.class.getResource("/fxml/GenerateReturn.fxml"));
        AnchorPane page = null;
        try {
            page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Transaction Panel");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            GenerateReturnController controller = loader.getController();
            controller.setMainClass(transaction);
            controller.setDialogStage(dialogStage);
            controller.initPanelDetails();
            dialogStage.showAndWait();

            if (controller.isConfirmedClicked()){
                transaction = controller.getTransaction();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return transaction;
    }

    public static TransactionDTO loadStock(TransactionDTO transaction){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(TransactionPanelLoader.class.getResource("/fxml/GenerateStock.fxml"));
        AnchorPane page = null;
        try {
            page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Transaction Panel");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            GenerateStockController controller = loader.getController();
            controller.setMainClass(transaction);
            controller.setDialogStage(dialogStage);
            controller.initDataFromDB();
            controller.initPanelDetails();
            dialogStage.showAndWait();

            if (controller.isConfirmedClicked()){
                transaction = controller.getTransaction();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return transaction;
    }


    public static boolean showCustomerEditor(CustomerDTO customerDTO){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(TransactionPanelLoader.class.getResource("/fxml/CustomerEditDialog.fxml"));
        AnchorPane page = null;
        try {
            page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Customer Editor");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            CustomerEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setTextField(customerDTO);
            dialogStage.showAndWait();
            return controller.isOKClicked();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    public static boolean showVendorEditor(VendorDTO vendorDTO){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(TransactionPanelLoader.class.getResource("/fxml/VendorEditDialog.fxml"));
        AnchorPane page = null;
        try {
            page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Vendor Editor");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            VendorEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setTextField(vendorDTO);
            dialogStage.showAndWait();
            return controller.isOKClicked();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    public static boolean showOrganizationEditor(OrganizationDTO organizationDTO){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(TransactionPanelLoader.class.getResource("/fxml/OrganizationEditDialog.fxml"));
        AnchorPane page = null;
        try {
            page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Address Editor");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            OrganizationEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setTextField(organizationDTO);
            dialogStage.showAndWait();
            return controller.isOKClicked();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean showProductEditor(ProductDTO productDTO, boolean isEdit){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(TransactionPanelLoader.class.getResource("/fxml/ProdictEditDialog.fxml"));
        AnchorPane page = null;
        try {
            page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Product Editor");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            ProductEditDialogController controller = loader.getController();
            controller.setMainClass(isEdit);
            controller.setDialogStage(dialogStage);
            controller.setTextField(productDTO);
            dialogStage.showAndWait();
            return controller.isOKClicked();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
