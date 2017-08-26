package com.taimsoft.desktopui;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

/**
 * Created by Tjin on 8/23/2017.
 */
public class TaimDesktop extends Application {
    private static final String APPLICATION_TITLE = "TAIM SOFTWARE DESKTOP";

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/RootLayout.fxml"));
            BorderPane rootLayout = loader.load();
            Scene scene = new Scene(rootLayout);
            rootLayout.prefHeightProperty().bind(scene.heightProperty());
            rootLayout.prefWidthProperty().bind(scene.widthProperty());
            primaryStage.setTitle(APPLICATION_TITLE);
            Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
            primaryStage.setX(bounds.getMinX());
            primaryStage.setY(bounds.getMinY());
            primaryStage.setWidth(bounds.getWidth());
            primaryStage.setHeight(bounds.getHeight());
            primaryStage.setScene(scene);
            primaryStage.show();
        }catch(IOException e){
            e.printStackTrace();
        }

    }

    public static void main(String[] args){
        launch(args);
    }
}
