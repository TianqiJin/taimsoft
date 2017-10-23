package com.taim.desktopui;

import com.taim.dto.TransactionDTO;
import com.taimsoft.desktopui.util.RestClientFactory;
import com.taimsoft.desktopui.util.TransactionPanelLoader;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.awt.*;

/**
 * Created by jiawei.liu on 10/22/17.
 */


public class panelTest{

    public static class AsNonApp extends Application {
        @Override
        public void start(Stage primaryStage) throws Exception {
            primaryStage.setScene(createScene(showInvoice()));
        }
    }


    @BeforeClass
    public static void initJFX() {
        Thread t = new Thread("JavaFX Init Thread") {
            public void run() {
                Application.launch(AsNonApp.class, new String[0]);
            }
        };
        t.setDaemon(true);
        t.start();
    }

    public static Pane showInvoice(){
            TransactionDTO transactionDTO = RestClientFactory.getTransactionClient().getById(8);
            TransactionDTO updatedTransaction = TransactionPanelLoader.loadInvoice(transactionDTO,null);
            return null;
    }

    private static Scene createScene(Pane rootPane){
        Scene scene = new Scene(rootPane);
        rootPane.prefHeightProperty().bind(scene.heightProperty());
        rootPane.prefWidthProperty().bind(scene.widthProperty());

        return scene;
    }
}


