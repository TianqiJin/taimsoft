package com.taimsoft.desktopui.controllers.edit;

import com.taim.dto.OrganizationDTO;
import com.taim.dto.VendorDTO;
import com.taim.model.basemodels.UserBaseModel;
import com.taimsoft.desktopui.uicomponents.FadingStatusMessage;
import com.taimsoft.desktopui.util.AlertBuilder;
import com.taimsoft.desktopui.util.RestClientFactory;
import com.taimsoft.desktopui.util.TransactionPanelLoader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.joda.time.DateTime;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static com.taimsoft.desktopui.constants.Constant.FXStyle.FX_ERROR_LABEL_COLOR;

/**
 * Created by jiawei.liu on 10/31/17.
 */
public class VendorEditDialogController {
    @FXML
    private TextField fullNameField;
    @FXML
    private TextField phoneField;
    @FXML
    private ComboBox<String> userTypeComboBox;
    @FXML
    private TextField emailField;
    @FXML
    private Label fullNameErrorLabel;
    @FXML
    private AnchorPane root;
    @FXML
    private AnchorPane orgRootPane;
    @FXML
    private SplitPane rootSplitPane;

    private static final Logger logger = LoggerFactory.getLogger(VendorEditDialogController.class);
    private Stage dialogStage;
    private VendorDTO vendor;
    private boolean okClicked;
    private OrganizationEditDialogController organizationEditDialogController;
    private Executor executor;

    private ObservableList<String> userTypeOpt;

    public VendorEditDialogController(){
        okClicked = false;
        userTypeOpt = FXCollections.observableArrayList(
                Arrays.stream(UserBaseModel.UserType.values()).map(u->u.getValue()).collect(Collectors.toList()));
        executor = Executors.newCachedThreadPool(r -> {
            Thread t = new Thread(r);
            t.setDaemon(true);
            return t;
        });
    }

    @FXML
    private void initialize(){
        initErrorLabel(fullNameField, fullNameErrorLabel, "Full name must not be empty!");
        userTypeComboBox.setItems(userTypeOpt);
        userTypeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            vendor.setUserType(UserBaseModel.UserType.getUserType(newValue));
        });
    }

    public void setDialogStage(Stage dialogStage){
        this.dialogStage = dialogStage;
    }

    public void initData(VendorDTO vendor){
        this.vendor = vendor;
        initGeneralInfoTextFields();

        try {
            FXMLLoader fXMLLoader = new FXMLLoader();
            AnchorPane orgRoot = fXMLLoader.load(this.getClass().getResource("/fxml/edit/OrganizationEditDialog.fxml").openStream());
            orgRoot.prefHeightProperty().bind(orgRootPane.heightProperty());
            orgRoot.prefWidthProperty().bind(orgRootPane.widthProperty());
            organizationEditDialogController = fXMLLoader.getController();
            organizationEditDialogController.initData(vendor.getOrganization());
            orgRootPane.getChildren().setAll(orgRoot);
        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
        }
    }
    public void handleOk(){
        scanBasicInfoFields();
        organizationEditDialogController.scanOrgInfoFields();
        if(isInputValid() && organizationEditDialogController.isOrgInfoValid()){
            Task<VendorDTO> saveUpdateVendorTask = new Task<VendorDTO>() {
                @Override
                protected VendorDTO call() throws Exception {
                    return RestClientFactory.getVendorClient().saveOrUpdate(vendor);
                }
            };
            saveUpdateVendorTask.setOnSucceeded(event -> {
                this.vendor = saveUpdateVendorTask.getValue();
                okClicked = true;
                dialogStage.close();
            });

            saveUpdateVendorTask.exceptionProperty().addListener((observable, oldValue, newValue) -> {
                if(newValue != null) {
                    Exception ex = (Exception) newValue;
                    logger.error(ExceptionUtils.getRootCause(ex).getMessage());
                    JSONObject errorMsg = new JSONObject(ExceptionUtils.getRootCause(ex).getMessage());
                    new AlertBuilder().alertType(Alert.AlertType.ERROR)
                            .alertContentText(errorMsg.getString("taimErrorMessage"))
                            .build()
                            .showAndWait();
                    if(errorMsg.getInt("taimErrorCode") == 1){
                        Task<VendorDTO> getVendorTask = new Task<VendorDTO>() {
                            @Override
                            protected VendorDTO call() throws Exception {
                                return RestClientFactory.getVendorClient().getById(vendor.getId());
                            }
                        };
                        getVendorTask.setOnSucceeded(event -> {
                            initData(getVendorTask.getValue());
                        });
                        getVendorTask.exceptionProperty().addListener((observable1, oldValue1, newValue1) -> {
                            if(newValue1 != null) {
                                Exception newEx = (Exception) newValue1;
                                String newExMsg = ExceptionUtils.getRootCause(newEx).getMessage();
                                logger.error(newExMsg);
                                JSONObject newErrorMessage = new JSONObject(newExMsg);
                                new AlertBuilder().alertType(Alert.AlertType.ERROR)
                                        .alertHeaderText(newErrorMessage.getString("taimErrorMessage"))
                                        .build()
                                        .showAndWait();
                            }});

                        executor.execute(getVendorTask);
                    }
                }
            });

            executor.execute(saveUpdateVendorTask);
        }
    }
    public void handleCancel(){
        dialogStage.close();
    }

    public VendorDTO getVendor() {
        return vendor;
    }

    public boolean isOKClicked(){
        return okClicked;
    }

    private boolean isInputValid(){
        return StringUtils.isEmpty(fullNameErrorLabel.getText());
    }

    private void scanBasicInfoFields(){
        fullNameField.requestFocus();
    }

    private void initGeneralInfoTextFields(){
        fullNameField.textProperty().bindBidirectional(this.vendor.fullnameProperty());
        emailField.textProperty().bindBidirectional(this.vendor.emailProperty());
        phoneField.textProperty().bindBidirectional(this.vendor.phoneProperty());
        if (vendor.getUserType()!=null){
            userTypeComboBox.setValue(vendor.getUserType().getValue());
        }else{
            userTypeComboBox.setValue(userTypeOpt.get(0));
        }
    }

    private void initErrorLabel(TextField field, Label errorLabel, String errorMsg){
        field.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue) { // we only care about loosing focus
                if(StringUtils.isNotEmpty(field.getText())){
                    errorLabel.setText("");
                }else{
                    errorLabel.setText(errorMsg);
                    errorLabel.setStyle(FX_ERROR_LABEL_COLOR);
                }
            }
        });
    }
}
