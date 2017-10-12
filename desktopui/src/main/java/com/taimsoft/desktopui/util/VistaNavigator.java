package com.taimsoft.desktopui.util;

import com.taimsoft.desktopui.controllers.RootLayoutController;
import com.taimsoft.desktopui.controllers.details.IDetailController;
import com.taimsoft.desktopui.controllers.overview.IOverviewController;
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
    public static final String VISTA_PRODUCT_DETAIL = "details/ProductDetails.fxml";
    public static final String VISTA_CUSTOMER_DETAIL = "details/CustomerDetails.fxml";

    private static RootLayoutController rootLayoutController;

    public static void setRootLayoutController(RootLayoutController rootLayoutController){
        VistaNavigator.rootLayoutController = rootLayoutController;
    }

    public static void loadVista(String fxml){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(VistaNavigator.class.getResource("/fxml/" + fxml));
        try {
            rootLayoutController.setVista(loader.load());
            IOverviewController controller = loader.getController();
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
            IDetailController controller = loader.getController();
            controller.initDetailData(obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
