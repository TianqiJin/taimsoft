package com.taimsoft.desktopui.controllers;

import com.taim.client.PropertyClient;
import com.taim.dto.PropertyDTO;
import com.taimsoft.desktopui.TaimDesktop;
import com.taimsoft.desktopui.util.RestClientFactory;
import com.taimsoft.desktopui.util.VistaNavigator;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by Tjin on 8/25/2017.
 */
public class RootLayoutController {
    private PropertyClient propertyClient;
    private Executor executor;
    private PropertyDTO property;

    @FXML
    private Button menu;
    @FXML
    private AnchorPane menuList;
    @FXML
    private AnchorPane vistaPane;

    @FXML
    public void initialize() {
        prepareSlideMenuAnimation();
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
       VistaNavigator.loadVista(VistaNavigator.VISTA_TRANSACTION);
    }

    @FXML
    public void handleProductButton(){ VistaNavigator.loadVista(VistaNavigator.VISTA_PRODUCT); }

    @FXML
    public void handleCustomerButton(){ VistaNavigator.loadVista(VistaNavigator.VISTA_CUSTOMER); }

    @FXML
    public void handleStaffButton(){ VistaNavigator.loadVista(VistaNavigator.VISTA_STAFF); }

    @FXML
    public void handleVendorButton(){ VistaNavigator.loadVista(VistaNavigator.VISTA_VENDOR); }

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

    private void prepareSlideMenuAnimation() {
        TranslateTransition openNav = new TranslateTransition(new Duration(350), menuList);
        openNav.setToX(0);
        TranslateTransition closeNav=new TranslateTransition(new Duration(350), menuList);
        menu.setOnAction((ActionEvent evt)->{
            if(menuList.getTranslateX()!=0){
                openNav.play();
            }else{
                closeNav.setToX(-(menuList.getWidth()));
                closeNav.play();
            }
        });
    }
}
