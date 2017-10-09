package com.taimsoft.desktopui.util;

import com.taimsoft.desktopui.controllers.RootLayoutController;
import com.taimsoft.desktopui.controllers.details.DetailController;
import com.taimsoft.desktopui.controllers.overview.OverviewController;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

/**
 * Created by Tjin on 8/29/2017.
 */
public class VistaNavigator {
    public static final String VISTA_TRANSACTION = "overview/TransactionOverview.fxml";
    public static final String VISTA_PRODUCT = "overview/ProductOverview.fxml";
    public static final String VISTA_CUSTOMER = "overview/CustomerOverview.fxml";
    public static final String VISTA_STAFF = "overview/StaffOverview.fxml";
    public static final String VISTA_VENDOR = "overview/VendorOverview.fxml";
    public static final String VISTA_TRANSACTION_DETAIL = "details/TransactionDetails.fxml";
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
            controller.initOverviewData(controller.getOverviewClient());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static <T>void loadDetailVista(String fxml, T obj){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(VistaNavigator.class.getResource("/fxml/" + fxml));
        try {
            rootLayoutController.setVista(loader.load());
            DetailController controller = loader.getController();
            controller.initDetailData(obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
