package com.taimsoft.desktopui;

import com.taimsoft.desktopui.util.VistaNavigator;
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
        primaryStage.setTitle(APPLICATION_TITLE);

        //Set the primaryStage bound to the maximum of the screen
        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        primaryStage.setX(bounds.getMinX());
        primaryStage.setY(bounds.getMinY());
        primaryStage.setWidth(bounds.getWidth());
        primaryStage.setHeight(bounds.getHeight());

        primaryStage.setScene(createScene());
        primaryStage.show();
    }

    public Pane loadMainVista(){
        //Load the fxml file
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/RootLayout.fxml"));
        try {
            BorderPane rootLayout = loader.load();
            VistaNavigator.setRootLayoutController(loader.getController());
            //TODO: Load the first vista pane
            return rootLayout;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Scene createScene(){
        Pane rootPane = loadMainVista();
        Scene scene = new Scene(rootPane);
        rootPane.prefHeightProperty().bind(scene.heightProperty());
        rootPane.prefWidthProperty().bind(scene.widthProperty());

        return scene;
    }

    public static void main(String[] args){
        launch(args);
    }
}
