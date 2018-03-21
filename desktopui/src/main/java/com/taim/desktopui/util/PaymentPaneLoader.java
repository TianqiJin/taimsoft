package com.taim.desktopui.util;

import com.taim.desktopui.constants.Constant;
import com.taim.desktopui.controllers.payment.GeneratePaymentRootController;
import com.taim.dto.PaymentDTO;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class PaymentPaneLoader {
    private static final Logger logger = LoggerFactory.getLogger(PaymentPaneLoader.class);

    public static PaymentDTO loadCustomerPayment(PaymentDTO paymentDTO){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(TransactionPanelLoader.class.getResource("/fxml/transactions/GeneratePaymentRoot.fxml"));
        try {
            AnchorPane page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Payment Generation");
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

            GeneratePaymentRootController controller = loader.getController();
            controller.init(GeneratePaymentRootController.PaymentMode.CUSTOMER, paymentDTO);
            dialogStage.showAndWait();

        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }


    }

    public static PaymentDTO loadVendorPayment(){

    }
}
