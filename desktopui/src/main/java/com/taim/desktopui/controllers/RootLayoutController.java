package com.taim.desktopui.controllers;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import com.taim.client.PropertyClient;
import com.taim.desktopui.TaimDesktop;
import com.taim.desktopui.util.RestClientFactory;
import com.taim.dto.PropertyDTO;
import com.taim.desktopui.util.VistaNavigator;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by Tjin on 8/25/2017.
 */
public class RootLayoutController {
    private PropertyClient propertyClient;
    private Executor executor;
    private PropertyDTO property;
    private Stage stage;

    @FXML
    private JFXHamburger menu;
    @FXML
    private JFXDrawer menuList;
    @FXML
    private JFXTextField searchField;
    @FXML
    private AnchorPane vistaPane;
    @FXML
    private AnchorPane titlePane;
    @FXML
    private VBox menulListVbox;
    @FXML
    private BorderPane borderPane;

    @FXML
    public void initialize() {
        menuList.setSidePane(menulListVbox);
        borderPane.setLeft(null);
        HamburgerBackArrowBasicTransition transition = new HamburgerBackArrowBasicTransition(menu);
        transition.setRate(-1);
        menu.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
            transition.setRate(transition.getRate() * -1);
            transition.play();
            if(menuList.isHidden() || menuList.isHiding()){
                borderPane.setLeft(menuList);
                Platform.runLater(() -> menuList.open());
            }else if(menuList.isShowing() || menuList.isShown()){
                menuList.close();
                Platform.runLater(() -> borderPane.setLeft(null));
            }
        });

    }

    public RootLayoutController(){
        this.propertyClient = RestClientFactory.getPropertyClient();
        this.executor = Executors.newCachedThreadPool(r -> {
            Thread t = new Thread(r);
            t.setDaemon(true);
            return t;
        });
    }

    @FXML
    public void handleHomeButton(){
        VistaNavigator.loadHomeVista(VistaNavigator.VISTA_HOME);
    }
    @FXML
    public void handleTransactionButton(){
       VistaNavigator.loadVista(VistaNavigator.VISTA_TRANSACTION, false);
    }

    @FXML
    public void handlePaymentButton(){VistaNavigator.loadVista(VistaNavigator.VISTA_PAYMENT, false);}

    @FXML
    public void handleProductButton(){ VistaNavigator.loadVista(VistaNavigator.VISTA_PRODUCT, false); }

    @FXML
    public void handleCustomerButton(){ VistaNavigator.loadVista(VistaNavigator.VISTA_CUSTOMER, true); }

    @FXML
    public void handleStaffButton(){ VistaNavigator.loadVista(VistaNavigator.VISTA_STAFF, false); }

    @FXML
    public void handleVendorButton(){ VistaNavigator.loadVista(VistaNavigator.VISTA_VENDOR, true); }

    @FXML
    public void handleSettingButton(){
        VistaNavigator.loadSettingVista(VistaNavigator.VISTA_SETTINGS);
    }

    @FXML
    public void handleLogoutButton(){
        TaimDesktop taimDesktop = new TaimDesktop();
        TaimDesktop.getPrimaryStage().close();
        taimDesktop.start(TaimDesktop.getPrimaryStage());
    }

    public void setVista(Node node){
        Pane tmpPane = (Pane) node;
        tmpPane.prefHeightProperty().bind(vistaPane.heightProperty());
        tmpPane.prefWidthProperty().bind(vistaPane.widthProperty());
        vistaPane.getChildren().setAll(tmpPane);
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public TextField getSearchField() {
        return searchField;
    }

}
