package com.taim.desktopui.util;

import com.taim.desktopui.constants.Constant;
import com.taim.desktopui.controllers.payment.GeneratePaymentRootController;
import com.taim.dto.PaymentDTO;
import com.taim.model.Payment;
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

    public static PaymentDTO loadCustomerPayment(PaymentDTO paymentDTO, Payment.PaymentType paymentType){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(PaymentPaneLoader.class.getResource("/fxml/payment/GeneratePaymentRoot.fxml"));
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
            dialogStage.getIcons().add(new Image(PaymentPaneLoader.class.getResourceAsStream(Constant.Image.appIconPath)));

            GeneratePaymentRootController controller = loader.getController();
            controller.init(paymentType, paymentDTO);
            controller.setDialogStage(dialogStage);
            dialogStage.showAndWait();

            return controller.getPayment();

        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }

}
