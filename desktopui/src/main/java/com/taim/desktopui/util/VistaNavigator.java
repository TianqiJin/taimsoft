package com.taim.desktopui.util;

import com.taim.desktopui.controllers.RootLayoutController;
import com.taim.desktopui.controllers.details.IDetailController;
import com.taim.desktopui.controllers.overview.HomeOverviewController;
import com.taim.desktopui.controllers.overview.IOverviewController;
import com.taim.desktopui.controllers.settings.SettingsOverviewController;
import com.taim.dto.PropertyDTO;
import com.taim.dto.StaffDTO;
import javafx.fxml.FXMLLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by Tjin on 8/29/2017.
 */
public class VistaNavigator {
    private static final Logger logger = LoggerFactory.getLogger(VistaNavigator.class);
    public static final String VISTA_HOME = "overview/HomeOverview.fxml";
    public static final String VISTA_TRANSACTION = "overview/TransactionOverview.fxml";
    public static final String VISTA_PAYMENT = "overview/PaymentOverview.fxml";
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

    public static RootLayoutController getRootLayoutController() {
        return rootLayoutController;
    }

    @SuppressWarnings("unchecked")
    public static void loadVista(String fxml, boolean fetchTransactionsForSummary){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(VistaNavigator.class.getResource("/fxml/" + fxml));
        try {
            rootLayoutController.setVista(loader.load());
            IOverviewController controller = loader.getController();
            controller.setStage(rootLayoutController.getStage());
            controller.setFetchTransactions(fetchTransactionsForSummary);
            controller.initOverviewData(controller.getOverviewClient());
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T>void loadDetailVista(String fxml, T obj){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(VistaNavigator.class.getResource("/fxml/" + fxml));
        try {
            rootLayoutController.setVista(loader.load());
            IDetailController controller = loader.getController();
            controller.setStage(rootLayoutController.getStage());
            controller.initDetailData(obj);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public static void loadSettingVista(String fxml){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(VistaNavigator.class.getResource("/fxml/" + fxml));
        try {
            rootLayoutController.setVista(loader.load());
            SettingsOverviewController controller = loader.getController();
            controller.setStage(rootLayoutController.getStage());
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public static void loadHomeVista(String fxml){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(VistaNavigator.class.getResource("/fxml/" + fxml));
        try {
            rootLayoutController.setVista(loader.load());
            HomeOverviewController controller = loader.getController();
            controller.initTransactionData();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
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
