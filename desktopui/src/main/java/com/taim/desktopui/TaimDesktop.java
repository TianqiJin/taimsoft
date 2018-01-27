package com.taim.desktopui;

import com.taim.desktopui.constants.Constant;
import com.taim.desktopui.controllers.login.LoginDialogController;
import com.taim.desktopui.util.VistaNavigator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by Tjin on 8/23/2017.
 */
public class TaimDesktop extends Application {
    private static final String APPLICATION_TITLE = "TAIM SOFTWARE DESKTOP";
    private static final Logger logger = LoggerFactory.getLogger(TaimDesktop.class);
    private static Stage primaryStage;

    @Override
    public void start(Stage stage) {
        if(initLoginDialog()){
            primaryStage = stage;
            primaryStage.setTitle(APPLICATION_TITLE);

            //Set the primaryStage bound to the maximum of the screen
            Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
            primaryStage.setX(bounds.getMinX());
            primaryStage.setY(bounds.getMinY());
            primaryStage.setWidth(bounds.getWidth());
            primaryStage.setHeight(bounds.getHeight());

            primaryStage.setScene(createScene(loadMainVista()));
            primaryStage.getIcons().add(new Image(TaimDesktop.class.getResourceAsStream(Constant.Image.appIconPath)));
            primaryStage.show();
            VistaNavigator.loadHomeVista(VistaNavigator.VISTA_HOME);
        }
    }

    private Pane loadMainVista(){
        //Load the fxml file
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/RootLayout.fxml"));
        try {
            BorderPane rootLayout = loader.load();
            VistaNavigator.setRootLayoutController(loader.getController());
            return rootLayout;
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }

    private Scene createScene(Pane rootPane){
        Scene scene = new Scene(rootPane);
        rootPane.prefHeightProperty().bind(scene.heightProperty());
        rootPane.prefWidthProperty().bind(scene.widthProperty());

        return scene;
    }

    private boolean initLoginDialog(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(TaimDesktop.class.getResource("/fxml/login/LoginDialog.fxml"));
            AnchorPane page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Login Dialog");
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            dialogStage.initModality(Modality.WINDOW_MODAL);

            LoginDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            dialogStage.setResizable(false);
            dialogStage.getIcons().add(new Image(TaimDesktop.class.getResourceAsStream(Constant.Image.appIconPath)));
            dialogStage.showAndWait();

            return controller.isSuccessful();

        }catch(IOException e){
            logger.error(e.getMessage(), e);
            return false;
        }
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args){
        launch(args);
    }
}
