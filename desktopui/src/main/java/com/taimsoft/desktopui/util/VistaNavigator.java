package com.taimsoft.desktopui.util;

import com.taimsoft.desktopui.controllers.RootLayoutController;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

/**
 * Created by Tjin on 8/29/2017.
 */
public class VistaNavigator {
    public static final String VISTA_TRANSACTION = "TransactionOverview.fxml";
    private static RootLayoutController rootLayoutController;

    public static void setRootLayoutController(RootLayoutController rootLayoutController){
        VistaNavigator.rootLayoutController = rootLayoutController;
    }

    public static void loadVista(String fxml){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(VistaNavigator.class.getResource("/fxml/" + fxml));
        try {
            rootLayoutController.setVista(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
