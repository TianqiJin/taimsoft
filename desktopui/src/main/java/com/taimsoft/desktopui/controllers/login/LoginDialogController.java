package com.taimsoft.desktopui.controllers.login;

import com.taim.client.PropertyClient;
import com.taim.client.StaffClient;
import com.taim.dto.OrganizationDTO;
import com.taim.dto.PropertyDTO;
import com.taim.dto.StaffDTO;
import com.taimsoft.desktopui.constants.Constant;
import com.taimsoft.desktopui.controllers.edit.StaffEditDialogController;
import com.taimsoft.desktopui.util.RestClientFactory;
import com.taimsoft.desktopui.util.VistaNavigator;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class LoginDialogController {
    private static final Logger logger = LoggerFactory.getLogger(LoginDialogController.class);
    private Stage dialogStage;
    private StaffClient staffClient;
    private PropertyClient propertyClient;
    private Executor executor;
    private boolean successful;

    @FXML
    private TextField userNameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;
    @FXML
    private Label errorLMsgLabel;
    @FXML
    private Hyperlink signUpLink;

    @FXML
    private void initialize(){
        userNameField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(!userNameField.getText().trim().isEmpty()){
                    loginButton.setDisable(false);
                }
                else{
                    loginButton.setDisable(true);
                }
            }
        });
        userNameField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue) { // we only care about loosing focus
               errorLMsgLabel.setText("");
            }
        });
        passwordField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue) { // we only care about loosing focus
                errorLMsgLabel.setText("");
            }
        });
        signUpLink.setOnAction(event -> {
            try{
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(LoginDialogController.class.getResource("/fxml/edit/StaffEditDialog.fxml"));
                AnchorPane page = loader.load();

                Stage dialogStage = new Stage();
                dialogStage.setTitle("Edit Staff");
                page.getStylesheets().add(LoginDialogController.class.getResource("/css/bootstrap3.css").toExternalForm());
                Scene scene = new Scene(page);
                dialogStage.setScene(scene);
                dialogStage.initModality(Modality.WINDOW_MODAL);
                //Set the stage bound to the maximum of the screen
                Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
                dialogStage.setX(bounds.getMinX());
                dialogStage.setY(bounds.getMinY());
                dialogStage.setWidth(bounds.getWidth());
                dialogStage.setHeight(bounds.getHeight());
                StaffEditDialogController controller = loader.getController();
                controller.setStage(dialogStage);
                StaffDTO staffDTO = new StaffDTO();
                staffDTO.setDateCreated(DateTime.now());
                staffDTO.setDateModified(DateTime.now());
                OrganizationDTO organization = new OrganizationDTO();
                organization.setDateCreated(DateTime.now());
                organization.setDateModified(DateTime.now());
                staffDTO.setOrganization(organization);
                controller.initData(staffDTO);

                dialogStage.showAndWait();
            }catch (IOException e){
                logger.error(e.getMessage(), e);
            }

        });
    }

    @FXML
    public void handleLogin(){
        loginButton.setDisable(true);
        String userName = userNameField.getText();
        String password = passwordField.getText();

        Task<StaffDTO> staffDTOTask = new Task<StaffDTO>() {
            @Override
            protected StaffDTO call() throws Exception {
                return staffClient.getByName(userName);
            }
        };
        Task<List<PropertyDTO>> propertyTask = new Task<List<PropertyDTO>>() {
            @Override
            protected List<PropertyDTO> call() throws Exception {
                return propertyClient.getList();
            }
        };

        propertyTask.setOnSucceeded(event -> {
            if(propertyTask.getValue().size() != 0){
                VistaNavigator.setGlobalProperty(propertyTask.getValue().get(0));
                successful = true;
                dialogStage.close();
            }else{
                try {
                    FXMLLoader fXMLLoader = new FXMLLoader();
                    AnchorPane root = fXMLLoader.load(this.getClass().getResource("/fxml/login/WelcomeDialog.fxml").openStream());
                    root.getStylesheets().add(WelcomeDialogController.class.getResource("/css/bootstrap3.css").toExternalForm());
                    Scene scene = new Scene(root);

                    Stage welcomeStage = new Stage();
                    welcomeStage.setTitle("Welcome");
                    welcomeStage.setScene(scene);
                    welcomeStage.initModality(Modality.WINDOW_MODAL);

                    WelcomeDialogController controller = fXMLLoader.getController();
                    controller.setStage(welcomeStage);

                    welcomeStage.showAndWait();
                    successful = true;
                    dialogStage.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        staffDTOTask.setOnSucceeded(event -> {
            StaffDTO staffDTO = staffDTOTask.getValue();
            if(staffDTO.getPassword().equals(password)){
                VistaNavigator.setGlobalStaff(staffDTO);
                executor.execute(propertyTask);
            }else{
                errorLMsgLabel.setText("Username/Password is incorrect");
                errorLMsgLabel.setStyle(Constant.FXStyle.FX_ERROR_LABEL_COLOR);
            }
        });

        staffDTOTask.setOnFailed(event -> {
            System.out.println(event.getSource().exceptionProperty().toString());
            errorLMsgLabel.setText("Username does not exist");
            errorLMsgLabel.setStyle(Constant.FXStyle.FX_ERROR_LABEL_COLOR);
        });

        executor.execute(staffDTOTask);
    }

    @FXML
    public void handleCancel(){
        dialogStage.close();
    }

    public LoginDialogController() {
        this.propertyClient = RestClientFactory.getPropertyClient();
        this.staffClient = RestClientFactory.getStaffClient();
        executor = Executors.newCachedThreadPool(r -> {
            Thread t = new Thread(r);
            t.setDaemon(true);
            return t;
        });
    }

    public void setDialogStage(Stage dialogStage){
        this.dialogStage = dialogStage;
    }

    public boolean isSuccessful() {
        return successful;
    }
}
