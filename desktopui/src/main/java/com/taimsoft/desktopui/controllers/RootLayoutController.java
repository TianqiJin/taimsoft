package com.taimsoft.desktopui.controllers;

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

/**
 * Created by Tjin on 8/25/2017.
 */
public class RootLayoutController {

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
