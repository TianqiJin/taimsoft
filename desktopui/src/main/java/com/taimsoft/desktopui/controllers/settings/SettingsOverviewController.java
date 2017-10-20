package com.taimsoft.desktopui.controllers.settings;

import com.taim.client.PropertyClient;
import com.taim.client.StaffClient;
import com.taim.dto.PropertyDTO;
import com.taim.dto.StaffDTO;
import com.taim.model.Property;
import com.taimsoft.desktopui.uicomponents.FadingStatusMessage;
import com.taimsoft.desktopui.util.RestClientFactory;
import com.taimsoft.desktopui.util.VistaNavigator;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static com.taimsoft.desktopui.constants.Constant.FXStyle.FX_ERROR_LABEL_COLOR;

public class SettingsOverviewController {
    @FXML
    private TabPane settingsTabPane;

    @FXML
    public void initialize(){
        settingsTabPane.getSelectionModel().clearSelection();
        settingsTabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {
                FXMLLoader fXMLLoader = new FXMLLoader();
                AnchorPane root;
                if(newValue.getText().equals("General Settings")){
                    root = fXMLLoader.load(this.getClass().getResource("/fxml/settings/GeneralSettings.fxml").openStream());
                }else{
                    root = fXMLLoader.load(this.getClass().getResource("/fxml/settings/AccountSettings.fxml").openStream());
                }
                root.prefHeightProperty().bind(settingsTabPane.heightProperty());
                root.prefWidthProperty().bind(settingsTabPane.widthProperty());
                newValue.setContent(root);
                ISettingsController controller = fXMLLoader.getController();
                controller.initData();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        settingsTabPane.getSelectionModel().selectFirst();
    }

    public SettingsOverviewController(){}
}
