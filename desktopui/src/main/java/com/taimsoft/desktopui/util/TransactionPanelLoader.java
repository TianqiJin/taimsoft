package com.taimsoft.desktopui.util;

import com.taim.dto.CustomerDTO;
import com.taim.dto.StaffDTO;
import com.taim.dto.TransactionDTO;
import com.taimsoft.desktopui.controllers.CustomerEditDialogController;
import com.taimsoft.desktopui.controllers.GenerateQuotationController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by jiawei.liu on 10/4/17.
 */
public class TransactionPanelLoader {

    public static TransactionDTO loadQuotation(TransactionDTO transaction, StaffDTO staff){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(TransactionPanelLoader.class.getResource("/fxml/GenerateQuotation.fxml"));
        AnchorPane page = null;
        try {
            page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Transaction Panel");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            GenerateQuotationController controller = loader.getController();
            controller.setMainClass(transaction,staff);
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

}
