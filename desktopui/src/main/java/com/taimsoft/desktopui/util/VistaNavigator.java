package com.taimsoft.desktopui.util;

import com.taim.dto.PropertyDTO;
import com.taim.dto.StaffDTO;
import com.taimsoft.desktopui.controllers.RootLayoutController;
import com.taimsoft.desktopui.controllers.details.IDetailController;
import com.taimsoft.desktopui.controllers.overview.IOverviewController;
import com.taimsoft.desktopui.controllers.settings.SettingsOverviewController;
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
    public static final String VISTA_VENDOR_DETAIL = "details/VendorDetails.fxml";
    public static final String VISTA_STAFF_DETAIL = "details/StaffDetails.fxml";
    public static final String VISTA_SETTINGS = "settings/SettingsOverview.fxml";

    private static RootLayoutController rootLayoutController;
    private static PropertyDTO globalProperty;
    private static StaffDTO globalStaff;

    public static void setRootLayoutController(RootLayoutController rootLayoutController){
        VistaNavigator.rootLayoutController = rootLayoutController;
    }

    @SuppressWarnings("unchecked")
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

    @SuppressWarnings("unchecked")
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

    public static void loadSettingVista(String fxml){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(VistaNavigator.class.getResource("/fxml/" + fxml));
        try {
            rootLayoutController.setVista(loader.load());
            SettingsOverviewController controller = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static PropertyDTO getGlobalProperty() {
        return globalProperty;
    }

    public static synchronized void setGlobalProperty(PropertyDTO globalProperty) {
        VistaNavigator.globalProperty = globalProperty;
    }

    public static StaffDTO getGlobalStaff() {
        return globalStaff;
    }

    public static synchronized void setGlobalStaff(StaffDTO globalStaff) {
        VistaNavigator.globalStaff = globalStaff;
    }
}
