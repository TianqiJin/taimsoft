package com.taimsoft.desktopui.controllers;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

/**
 * Created by Tjin on 8/25/2017.
 */
public class RootLayoutController {

    @FXML
    private Button menu;
    @FXML
    private AnchorPane menuList;

    @FXML
    public void initialize() {
        prepareSlideMenuAnimation();
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
