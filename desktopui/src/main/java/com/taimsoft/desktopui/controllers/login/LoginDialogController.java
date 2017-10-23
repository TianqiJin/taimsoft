package com.taimsoft.desktopui.controllers.login;

import com.taim.client.PropertyClient;
import com.taim.client.StaffClient;
import com.taim.dto.PropertyDTO;
import com.taim.dto.StaffDTO;
import com.taimsoft.desktopui.constants.Constant;
import com.taimsoft.desktopui.util.RestClientFactory;
import com.taimsoft.desktopui.util.VistaNavigator;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class LoginDialogController {
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
