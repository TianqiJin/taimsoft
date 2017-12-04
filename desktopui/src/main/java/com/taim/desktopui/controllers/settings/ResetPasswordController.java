package com.taim.desktopui.controllers.settings;

import com.taim.client.StaffClient;
import com.taim.dto.StaffDTO;
import com.taim.desktopui.constants.Constant;
import com.taim.desktopui.uicomponents.FadingStatusMessage;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.Executor;

public class ResetPasswordController {
    private StaffDTO staffDTO;
    private Executor executor;
    private StaffClient staffClient;
    private Stage dialogStage;

    @FXML
    private PasswordField oldPasswordField;
    @FXML
    private PasswordField newPasswordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private Label oldPasswordErrorLabel;
    @FXML
    private Label newPasswordErrorLabel;
    @FXML
    private Label confirmPasswordErrorLabel;
    @FXML
    private AnchorPane rootPane;

    @FXML
    private void initialize(){
        oldPasswordField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue) { // we only care about loosing focus
                if(oldPasswordField.getText().equals(this.staffDTO.getPassword())){
                    oldPasswordErrorLabel.setText("");
                }else{
                    oldPasswordErrorLabel.setText("Password is invalid");
                    oldPasswordErrorLabel.setStyle(Constant.FXStyle.FX_ERROR_LABEL_COLOR);
                }
            }
        });
        newPasswordField.focusedProperty().addListener(((observable, oldValue, newValue) -> {
            if(!newValue){
                if(!StringUtils.isEmpty(newPasswordField.getText())){
                    newPasswordErrorLabel.setText("");
                }else{
                    newPasswordErrorLabel.setText("Password cannot be null");
                    newPasswordErrorLabel.setStyle(Constant.FXStyle.FX_ERROR_LABEL_COLOR);
                }
            }
        }));
        confirmPasswordField.focusedProperty().addListener(((observable, oldValue, newValue) -> {
            if(!newValue){
                if(confirmPasswordField.getText().equals(newPasswordField.getText())){
                    confirmPasswordErrorLabel.setText("");
                }else{
                    confirmPasswordErrorLabel.setText("Password does not match");
                    confirmPasswordErrorLabel.setStyle(Constant.FXStyle.FX_ERROR_LABEL_COLOR);
                }
            }
        }));
    }

    public void initData(StaffDTO staffDTO, Executor executor, StaffClient staffClient){
        this.staffDTO = staffDTO;
        this.executor = executor;
        this.staffClient = staffClient;
    }

    @FXML
    public void handleSaveButton(){
        if(StringUtils.isEmpty(confirmPasswordErrorLabel.getText())
                && StringUtils.isEmpty(newPasswordErrorLabel.getText())
                && StringUtils.isEmpty(oldPasswordErrorLabel.getText())){
            this.staffDTO.setPassword(newPasswordField.getText());
            Task<StaffDTO> updatePasswordTask = new Task<StaffDTO>() {
                @Override
                protected StaffDTO call() throws Exception {
                    return staffClient.update(staffDTO);
                }
            };

            updatePasswordTask.setOnFailed(event -> FadingStatusMessage.flash("Failed", rootPane));
            updatePasswordTask.setOnSucceeded(event -> {
                this.staffDTO = updatePasswordTask.getValue();
                dialogStage.close();
            });

            executor.execute(updatePasswordTask);
        }
    }

    @FXML
    public void handleCancelButton(){
        dialogStage.close();
    }

    public StaffDTO getStaffDTO() {
        return staffDTO;
    }

    public Stage getDialogStage() {
        return dialogStage;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
}
