package com.taimsoft.desktopui.util;

import com.taimsoft.desktopui.controllers.RootLayoutController;
import com.taimsoft.desktopui.controllers.overview.OverviewController;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

/**
 * Created by Tjin on 8/29/2017.
 */
public class VistaNavigator {
    public static final String VISTA_TRANSACTION = "TransactionOverview.fxml";
    public static final String VISTA_PRODUCT = "ProductOverview.fxml";
    public static final String VISTA_CUSTOMER = "CustomerOverview.fxml";
    public static final String VISTA_STAFF = "StaffOverview.fxml";
    public static final String VISTA_VENDOR = "VendorOverview.fxml";
    private static RootLayoutController rootLayoutController;

    public static void setRootLayoutController(RootLayoutController rootLayoutController){
        VistaNavigator.rootLayoutController = rootLayoutController;
    }

    public static void loadVista(String fxml){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(VistaNavigator.class.getResource("/fxml/" + fxml));
        try {
            rootLayoutController.setVista(loader.load());
            OverviewController controller = loader.getController();
            controller.loadData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
