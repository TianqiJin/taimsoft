package com.taim.desktopui.util;

import com.taim.desktopui.constants.Constant;
import com.taim.desktopui.controllers.edit.*;
import com.taim.desktopui.controllers.transactions.*;
import com.taim.dto.*;
import com.taim.model.Transaction;
import com.taim.desktopui.controllers.pdfs.InvoiceGenerationController;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by jiawei.liu on 10/4/17.
 */
public class TransactionPanelLoader {
    private static final Logger logger = LoggerFactory.getLogger(TransactionPanelLoader.class);

    public static TransactionDTO loadQuotation(TransactionDTO transaction){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(TransactionPanelLoader.class.getResource("/fxml/transactions/GenerateQuotation.fxml"));
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
            dialogStage.getIcons().add(new Image(TransactionPanelLoader.class.getResourceAsStream(Constant.Image.appIconPath)));

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
            logger.error(e.getMessage(), e);
        }
        return transaction;
    }

    public static TransactionDTO loadInvoice(TransactionDTO transaction){
        if(transaction==null){
            new AlertBuilder()
                    .alertType(Alert.AlertType.ERROR)
                    .alertHeaderText("Transaction Error!")
                    .alertContentText("Please select a valid transaction!")
                    .build()
                    .showAndWait();
            return transaction;
        }
        if(transaction.getTransactionType()!= Transaction.TransactionType.INVOICE && transaction.getTransactionType()!= Transaction.TransactionType.QUOTATION){
            new AlertBuilder()
                    .alertType(Alert.AlertType.ERROR)
                    .alertHeaderText("Transaction Error!")
                    .alertContentText("Please either select an invoice to edit or select quotation to generate invoice!!")
                    .build()
                    .showAndWait();
            return transaction;
        }
        if (transaction.getTransactionType()== Transaction.TransactionType.QUOTATION && transaction.isFinalized()){
            new AlertBuilder()
                    .alertType(Alert.AlertType.ERROR)
                    .alertHeaderText("Transaction Error!")
                    .alertContentText("Quotation already finalized!")
                    .build()
                    .showAndWait();
            return transaction;
        }

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(TransactionPanelLoader.class.getResource("/fxml/transactions/GenerateInvoice.fxml"));
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
            dialogStage.getIcons().add(new Image(TransactionPanelLoader.class.getResourceAsStream(Constant.Image.appIconPath)));

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
            logger.error(e.getMessage(), e);
        }
        return transaction;
    }


    public static TransactionDTO loadReturn(TransactionDTO transaction){
        if(transaction==null){
            new AlertBuilder()
                    .alertType(Alert.AlertType.ERROR)
                    .alertHeaderText("Transaction Error!")
                    .alertContentText("Please select a valid transaction!")
                    .build()
                    .showAndWait();
            return transaction;
        }

        if(transaction.getTransactionType()!= Transaction.TransactionType.RETURN && transaction.getTransactionType()!= Transaction.TransactionType.INVOICE){
            new AlertBuilder()
                    .alertType(Alert.AlertType.ERROR)
                    .alertHeaderText("Transaction Error!")
                    .alertContentText("Please either select an Invoice to return or select Return to Edit!!")
                    .build()
                    .showAndWait();
            return transaction;
        }
        if (transaction.getTransactionType()== Transaction.TransactionType.INVOICE && !transaction.isFinalized()){
            new AlertBuilder()
                    .alertType(Alert.AlertType.ERROR)
                    .alertHeaderText("Transaction Error!")
                    .alertContentText("Please select a finalized invoice transaction to generate return transaction!!")
                    .build()
                    .showAndWait();
            return transaction;
        }

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(TransactionPanelLoader.class.getResource("/fxml/transactions/GenerateReturn.fxml"));
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
            dialogStage.getIcons().add(new Image(TransactionPanelLoader.class.getResourceAsStream(Constant.Image.appIconPath)));

            GenerateReturnController controller = loader.getController();
            controller.setMainClass(transaction);
            controller.setDialogStage(dialogStage);
            controller.initPanelDetails();
            dialogStage.showAndWait();

            if (controller.isConfirmedClicked()){
                transaction = controller.getTransaction();
            }

        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return transaction;
    }

    public static TransactionDTO loadStock(TransactionDTO transaction){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(TransactionPanelLoader.class.getResource("/fxml/transactions/GenerateStock.fxml"));
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
            dialogStage.getIcons().add(new Image(TransactionPanelLoader.class.getResourceAsStream(Constant.Image.appIconPath)));

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
            logger.error(e.getMessage(), e);
        }
        return transaction;
    }


    public static CustomerEditDialogController showCustomerEditor(CustomerDTO customerDTO){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(TransactionPanelLoader.class.getResource("/fxml/edit/CustomerEditDialog.fxml"));
            AnchorPane page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Customer");
            page.getStylesheets().add(TransactionPanelLoader.class.getResource("/css/bootstrap3.css").toExternalForm());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            dialogStage.getIcons().add(new Image(TransactionPanelLoader.class.getResourceAsStream(Constant.Image.appIconPath)));
            dialogStage.initModality(Modality.WINDOW_MODAL);
            //Set the stage bound to the maximum of the screen
            Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
            dialogStage.setX(bounds.getMinX());
            dialogStage.setY(bounds.getMinY());
            dialogStage.setWidth(bounds.getWidth());
            dialogStage.setHeight(bounds.getHeight());
            CustomerEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.initData(customerDTO);

            dialogStage.showAndWait();
            return controller;
        }catch (IOException e){
            logger.error(e.getMessage(), e);
        }

        return null;
    }

    public static StaffEditDialogController showStaffEditor(StaffDTO staffDTO){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(TransactionPanelLoader.class.getResource("/fxml/edit/StaffEditDialog.fxml"));
            AnchorPane page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Staff");
            page.getStylesheets().add(TransactionPanelLoader.class.getResource("/css/bootstrap3.css").toExternalForm());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            dialogStage.getIcons().add(new Image(TransactionPanelLoader.class.getResourceAsStream(Constant.Image.appIconPath)));
            dialogStage.initModality(Modality.WINDOW_MODAL);
            //Set the stage bound to the maximum of the screen
            Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
            dialogStage.setX(bounds.getMinX());
            dialogStage.setY(bounds.getMinY());
            dialogStage.setWidth(bounds.getWidth());
            dialogStage.setHeight(bounds.getHeight());
            StaffEditDialogController controller = loader.getController();
            controller.setStage(dialogStage);
            controller.initData(staffDTO);

            dialogStage.showAndWait();
            return controller;
        }catch (IOException e){
            logger.error(e.getMessage(), e);
        }

        return null;
    }


    public static VendorEditDialogController showVendorEditor(VendorDTO vendorDTO){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(TransactionPanelLoader.class.getResource("/fxml/edit/VendorEditDialog.fxml"));
        AnchorPane page = null;
        try {
            page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Vendor Editor");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            dialogStage.getIcons().add(new Image(TransactionPanelLoader.class.getResourceAsStream(Constant.Image.appIconPath)));
            //Set the stage bound to the maximum of the screen
            Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
            dialogStage.setX(bounds.getMinX());
            dialogStage.setY(bounds.getMinY());
            dialogStage.setWidth(bounds.getWidth());
            dialogStage.setHeight(bounds.getHeight());

            VendorEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.initData(vendorDTO);
            dialogStage.showAndWait();
            return controller;

        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }


    public static boolean showOrganizationEditor(OrganizationDTO organizationDTO){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(TransactionPanelLoader.class.getResource("/fxml/edit/OrganizationEditDialog.fxml"));
        AnchorPane page = null;
        try {
            page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Address Editor");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            dialogStage.getIcons().add(new Image(TransactionPanelLoader.class.getResourceAsStream(Constant.Image.appIconPath)));

            OrganizationEditDialogController controller = loader.getController();
//            controller.setDialogStage(dialogStage);
//            controller.setTextField(organizationDTO);
            dialogStage.showAndWait();
//            return controller.isOKClicked();

        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return false;
    }

    public static ProductEditDialogController showProductEditor(ProductDTO productDTO){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(TransactionPanelLoader.class.getResource("/fxml/edit/ProductEditDialog.fxml"));
        AnchorPane page = null;
        try {
            page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Product Editor");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            dialogStage.getIcons().add(new Image(TransactionPanelLoader.class.getResourceAsStream(Constant.Image.appIconPath)));

            ProductEditDialogController controller = loader.getController();
            //Set the stage bound to the maximum of the screen
            Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
            dialogStage.setX(bounds.getMinX());
            dialogStage.setY(bounds.getMinY());
            dialogStage.setWidth(bounds.getWidth());
            dialogStage.setHeight(bounds.getHeight());

            controller.setDialogStage(dialogStage);
            controller.initData(productDTO);
            dialogStage.showAndWait();
            return controller;

        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    public static TransactionDTO showNewTransactionDialog(Transaction.TransactionType type){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(TransactionPanelLoader.class.getResource("/fxml/InvoiceReturnTransaction.fxml"));
        TitledPane page = null;
        try {
            page = loader.load();
            page.getStylesheets().add(TransactionPanelLoader.class.getResource("/css/bootstrap3.css").toExternalForm());
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Create Transaction");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            dialogStage.getIcons().add(new Image(TransactionPanelLoader.class.getResourceAsStream(Constant.Image.appIconPath)));

            InvRetController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.assignType(type);
            dialogStage.setResizable(false);
            dialogStage.showAndWait();
            if (controller.isConfirmedClicked()){
                return controller.getTransactionDTO();
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    public static void showPrintTransactionDialog(TransactionDTO transactionDTO){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(TransactionPanelLoader.class.getResource("/fxml/pdfs/InvoiceGeneration.fxml"));
            AnchorPane page = loader.load();
            Stage dialogStage = new Stage();
            //Set the dialog stage bound to the maximum of the screen
            Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
            dialogStage.setX(bounds.getMinX());
            dialogStage.setY(bounds.getMinY());
            dialogStage.setWidth(bounds.getWidth());
            dialogStage.setHeight(bounds.getHeight());
            dialogStage.setTitle("PRINT INVOICES");
            page.getStylesheets().add(TransactionPanelLoader.class.getResource("/css/bootstrap3.css").toExternalForm());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            dialogStage.getIcons().add(new Image(TransactionPanelLoader.class.getResourceAsStream(Constant.Image.appIconPath)));
            dialogStage.initModality(Modality.WINDOW_MODAL);
            InvoiceGenerationController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.initData(transactionDTO);
            dialogStage.showAndWait();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }
}
