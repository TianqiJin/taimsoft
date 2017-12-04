package com.taim.desktopui.controllers.settings;

import com.taim.client.StaffClient;
import com.taim.desktopui.TaimDesktop;
import com.taim.desktopui.uicomponents.FadingStatusMessage;
import com.taim.desktopui.util.RestClientFactory;
import com.taim.dto.StaffDTO;
import com.taim.desktopui.util.VistaNavigator;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.taim.desktopui.constants.Constant.FXStyle.FX_ERROR_LABEL_COLOR;

public class AccountSettingsController implements ISettingsController {
    private StaffClient staffClient;
    private StaffDTO staff;
    private Executor executor;
    private Pattern pattern;
    private Matcher matcher;
    private static final Logger logger = LoggerFactory.getLogger(AccountSettingsController.class);
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    @FXML
    private TextField userNameField;
    @FXML
    private TextField fullNameField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField emailField;
    @FXML
    private Label positionLabel;
    @FXML
    private Label userNameErrorLabel;
    @FXML
    private Label emailErrorLabel;
    @FXML
    private AnchorPane rootPane;

    @FXML
    private void initialize(){
        userNameField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue) { // we only care about loosing focus
                if(StringUtils.isNotEmpty(userNameField.getText())){
                    userNameErrorLabel.setText("");
                }else{
                    userNameErrorLabel.setText("Username cannot be empty");
                    userNameErrorLabel.setStyle(FX_ERROR_LABEL_COLOR);
                }
            }
        });
        emailField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue) { // we only care about loosing focus
                matcher = pattern.matcher(emailField.getText());
                if(matcher.matches()){
                    emailErrorLabel.setText("");
                }else{
                    emailErrorLabel.setText("Email is invalid");
                    emailErrorLabel.setStyle(FX_ERROR_LABEL_COLOR);
                }
            }
        });
    }

    public AccountSettingsController(){
        pattern = Pattern.compile(EMAIL_PATTERN);
        staffClient = RestClientFactory.getStaffClient();
        this.executor = Executors.newCachedThreadPool(r -> {
            Thread t = new Thread(r);
            t.setDaemon(true);
            return t;
        });
    }

    @Override
    public void initData() {
        Task<StaffDTO> getStaffTask = new Task<StaffDTO>() {
            @Override
            protected StaffDTO call() throws Exception {
                return staffClient.getByName(VistaNavigator.getGlobalStaff().getUserName());
            }
        };

        getStaffTask.setOnSucceeded(event -> {
            this.staff = getStaffTask.getValue();
            initUserInfoFields();
        });

        executor.execute(getStaffTask);
    }

    @FXML
    public void handleSaveStaffButton(){
        if(StringUtils.isEmpty(userNameErrorLabel.getText())
                && StringUtils.isEmpty(emailErrorLabel.getText())){
            Task<StaffDTO> saveStaffTask = new Task<StaffDTO>() {
                @Override
                protected StaffDTO call() throws Exception {
                    return staffClient.update(staff);
                }
            };

            saveStaffTask.setOnSucceeded(event -> {
                FadingStatusMessage.flash("Successful", rootPane);
                this.staff = saveStaffTask.getValue();
                VistaNavigator.setGlobalStaff(this.staff);
                initUserInfoFields();
            });

            saveStaffTask.setOnFailed(event -> {
                FadingStatusMessage.flash("Failed", rootPane);
            });

            executor.execute(saveStaffTask);
        }
    }

    @FXML
    public void handleChangePasswordButton(){
        this.staff = updatePassword();
    }

    private void initUserInfoFields(){
        userNameField.textProperty().bindBidirectional(this.staff.userNameProperty());
        fullNameField.textProperty().bindBidirectional(this.staff.fullnameProperty());
        emailField.textProperty().bindBidirectional(this.staff.emailProperty());
        phoneField.textProperty().bindBidirectional(this.staff.phoneProperty());
        positionLabel.textProperty().bind(this.staff.positionProperty().asString());
    }

    private StaffDTO updatePassword(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(TaimDesktop.class.getResource("/fxml/settings/ResetPassword.fxml"));
            AnchorPane page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Reset Password");
            page.getStylesheets().add(TaimDesktop.class.getResource("/css/bootstrap3.css").toExternalForm());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            dialogStage.initModality(Modality.WINDOW_MODAL);

            ResetPasswordController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.initData(this.staff, this.executor, this.staffClient);
            dialogStage.setResizable(false);
            dialogStage.showAndWait();

            return controller.getStaffDTO();

        }catch(IOException e){
            logger.error(e.getMessage(), e);
            return null;
        }
    }

}
