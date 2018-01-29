package com.taim.desktopui.controllers.settings;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.taim.client.LicenseClient;
import com.taim.client.PropertyClient;
import com.taim.desktopui.TaimDesktop;
import com.taim.desktopui.constants.Constant;
import com.taim.desktopui.uicomponents.FadingStatusMessage;
import com.taim.desktopui.util.AlertBuilder;
import com.taim.desktopui.util.RestClientFactory;
import com.taim.dto.LicenseDTO;
import com.taim.dto.PropertyDTO;
import com.taim.licensegen.GenerateLicense;
import com.taim.licensegen.model.LicenseFile;
import com.taim.licensegen.model.licensever.LicenseV1;
import com.taim.desktopui.util.VistaNavigator;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.joda.time.DateTime;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static com.taim.desktopui.constants.Constant.FXStyle.FX_ERROR_LABEL_COLOR;

public class GeneralSettingsController implements ISettingsController{
    private static final Logger logger = LoggerFactory.getLogger(GeneralSettingsController.class);
    private static final String DESKTOP_PRODUCT= "Taim Desktop";
    private GenerateLicense generateLicense;
    private Executor executor;
    private PropertyClient propertyClient;
    private LicenseClient licenseClient;
    private PropertyDTO property;

    @FXML
    private TableView<PropertyDTO.CustomerClassDTO> customerClassTable;
    @FXML
    private TableColumn<PropertyDTO.CustomerClassDTO, String> customerClassCol;
    @FXML
    private TableColumn<PropertyDTO.CustomerClassDTO, Double> discountCol;
    @FXML
    private TableColumn<PropertyDTO.CustomerClassDTO, String> actionCol;
    @FXML
    private JFXTextField companyNameField;
    @FXML
    private JFXTextField productWarnLimitField;
    @FXML
    private JFXTextField gstNumField;
    @FXML
    private JFXTextField gstRateField;
    @FXML
    private JFXTextField pstRateField;
    @FXML
    private Label gstRateErrorLabel;
    @FXML
    private Label gstNumErrorLabel;
    @FXML
    private Label productWarnLimitErrorLabel;
    @FXML
    private Label companyNameErrorLabel;
    @FXML
    private Label pstRateErrorLabel;
    @FXML
    private JFXButton saveCompanyInfoButton;
    @FXML
    private JFXButton saveCustomerClassInfoButton;
    @FXML
    private JFXButton uploadLicenseButton;
    @FXML
    private JFXButton modifyTermsButton;
    @FXML
    private Label licenseProductLabel;
    @FXML
    private Label licenseCustomerLabel;
    @FXML
    private Label licenseProjectLabel;
    @FXML
    private Label licenseValidFromLabel;
    @FXML
    private Label licenseValidUntilLabel;
    @FXML
    private Label licenseRecordIdLabel;
    @FXML
    private Label licenseMaxTransactionsLabel;
    @FXML
    private Label licenseMaxUsersLabel;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private SplitPane generalSettingPane;

    @FXML
    public void initialize(){
        customerClassCol.setCellValueFactory(new PropertyValueFactory<>("customerClassName"));
        discountCol.setCellValueFactory(new PropertyValueFactory<>("customerDiscount"));
        customerClassCol.setOnEditCommit(event -> (event.getTableView().getItems().get(event.getTablePosition().getRow()))
                .setCustomerClassName(event.getNewValue()));
        customerClassCol.setCellFactory(TextFieldTableCell.forTableColumn());
        discountCol.setOnEditCommit(event -> (event.getTableView().getItems().get(event.getTablePosition().getRow()))
                .setCustomerDiscount(event.getNewValue()));
        discountCol.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Double>() {
            @Override
            public String toString(Double object) {
                return String.valueOf(object);
            }

            @Override
            public Double fromString(String string) {
                return Double.valueOf(string);
            }
        }));

        actionCol.setCellValueFactory(new PropertyValueFactory<>("action"));
        actionCol.setCellFactory(new Callback<TableColumn<PropertyDTO.CustomerClassDTO, String>, TableCell<PropertyDTO.CustomerClassDTO, String>>() {
            @Override
            public TableCell<PropertyDTO.CustomerClassDTO, String> call(TableColumn<PropertyDTO.CustomerClassDTO, String> param) {
                return new TableCell<PropertyDTO.CustomerClassDTO, String>(){
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            JFXComboBox<String> comboBox = new JFXComboBox<>();
                            setGraphic(comboBox);
                            comboBox.setPromptText("SET ACTION");
                            comboBox.prefWidthProperty().bind(this.widthProperty());
                            if(getIndex() == 0){
                                comboBox.setItems(FXCollections.observableArrayList("ADD NEW"));
                            }else{
                                comboBox.setItems(FXCollections.observableArrayList("ADD NEW", "DELETE"));
                            }
                            comboBox.valueProperty().addListener(((observable, oldValue, newValue) -> {
                                if(newValue.equals("DELETE")){
                                    int selectedIndex = getTableRow().getIndex();
                                    getTableView().getItems().remove(selectedIndex);
                                }else if(newValue.equals("ADD NEW")){
                                    PropertyDTO.CustomerClassDTO newCustomerClassDTO = createCustomerClass();
                                    customerClassTable.getItems().add(newCustomerClassDTO);
                                }
                            }));
                            comboBox.setValue(item);
                        }
                    }
                };
            }
        });
        pstRateField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue) { // we only care about loosing focus
                try{
                    Integer.parseInt(pstRateField.getText());
                    pstRateErrorLabel.setText("");
                }catch (NumberFormatException e){
                    pstRateErrorLabel.setText("PST must be an integer");
                    pstRateErrorLabel.setStyle(FX_ERROR_LABEL_COLOR);
                }

            }
        });
        gstRateField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue) { // we only care about loosing focus
                try{
                    Integer.parseInt(gstRateField.getText());
                    gstRateErrorLabel.setText("");
                }catch (NumberFormatException e){
                    gstRateErrorLabel.setText("GST must be an integer");
                    gstRateErrorLabel.setStyle(FX_ERROR_LABEL_COLOR);
                }

            }
        });
        gstNumField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue) { // we only care about loosing focus
                if(StringUtils.isNotEmpty(gstNumField.getText())){
                    gstNumErrorLabel.setText("");
                }else{
                    gstNumErrorLabel.setText("GST number cannot be empty");
                    gstNumErrorLabel.setStyle(FX_ERROR_LABEL_COLOR);
                }
            }
        });
        productWarnLimitField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue) { // we only care about loosing focus
                try{
                    Double.parseDouble(productWarnLimitField.getText());
                    productWarnLimitErrorLabel.setText("");
                }catch (NumberFormatException e){
                    productWarnLimitErrorLabel.setText("Product limit warning must be a number");
                    productWarnLimitErrorLabel.setStyle(FX_ERROR_LABEL_COLOR);
                }
            }
        });
        companyNameField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue) { // we only care about loosing focus
                if(StringUtils.isNotEmpty(companyNameField.getText())){
                    companyNameErrorLabel.setText("");
                }else{
                    companyNameErrorLabel.setText("Company name cannot be empty");
                    companyNameErrorLabel.setStyle(FX_ERROR_LABEL_COLOR);
                }
            }
        });
    }

    public GeneralSettingsController(){
        this.generateLicense = new GenerateLicense();
        this.propertyClient = RestClientFactory.getPropertyClient();
        this.licenseClient = RestClientFactory.getLicenseClient();
        this.executor = Executors.newCachedThreadPool(r -> {
            Thread t = new Thread(r);
            t.setDaemon(true);
            return t;
        });
    }

    public void initData(){
        Task<List<PropertyDTO>> getPropertyTask = new Task<List<PropertyDTO>>() {
            @Override
            protected List<PropertyDTO> call() throws Exception {
                return propertyClient.getList();
            }
        };
        getPropertyTask.setOnSucceeded(event -> {
            if(getPropertyTask.getValue().size() != 0){
                this.property = getPropertyTask.getValue().get(0);
                customerClassTable.setItems(FXCollections.observableArrayList(this.property.getCustomerClasses()));
                initGeneralInfoTextFields();
                initLicenseFields();
            }else{
                PropertyDTO.CustomerClassDTO newCustomerClassDTO = createCustomerClass();
                customerClassTable.setItems(FXCollections.observableArrayList());
                customerClassTable.getItems().add(newCustomerClassDTO);
            }
        });

        executor.execute(getPropertyTask);
    }

    private void initUI(PropertyDTO property){
        this.property = property;
        VistaNavigator.setGlobalProperty(this.property);
        initGeneralInfoTextFields();
        customerClassTable.setItems(FXCollections.observableArrayList(this.property.getCustomerClasses()));
        initLicenseFields();
    }

    public void initWelcomeData(){
        this.property = new PropertyDTO();
        this.property.setDateCreated(DateTime.now());
        this.property.setDateModified(DateTime.now());

        saveCompanyInfoButton.setVisible(false);
        saveCustomerClassInfoButton.setVisible(false);
        modifyTermsButton.setDisable(true);
        initGeneralInfoTextFields();
        uploadLicenseButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("DAT files (*.dat)", "*.dat");
            fileChooser.getExtensionFilters().add(extFilter);
            fileChooser.setTitle("Upload New License");
            File licenseFile = fileChooser.showOpenDialog(rootPane.getScene().getWindow());

            boolean isLicenseValid = false;
            try {
                generateLicense.verifyLicense(licenseFile, DESKTOP_PRODUCT);
                isLicenseValid = true;
            } catch (GenerateLicense.GenerateLicenseException e) {
                logger.error(e.getMessage(), e);
                new AlertBuilder()
                        .alertType(Alert.AlertType.ERROR)
                        .alertContentText(e.getMessage())
                        .build()
                        .showAndWait();
            }

            if(isLicenseValid){
                LicenseDTO licenseDTO = new LicenseDTO();
                licenseDTO.setDateModified(DateTime.now());
                licenseDTO.setDateCreated(DateTime.now());
                try {
                    licenseDTO.setLicenseFile(Files.readAllBytes(licenseFile.toPath()));
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
                property.setLicense(licenseDTO);
                initLicenseFields();
            }

        });
        PropertyDTO.CustomerClassDTO newCustomerClassDTO = createCustomerClass();
        customerClassTable.setItems(FXCollections.observableArrayList());
        customerClassTable.getItems().add(newCustomerClassDTO);
    }

    @FXML
    public void handleSavePropertyButton(){
        if(isCompanyInfoValid()){
            Task<PropertyDTO> savePropertyTask = generateSavePropertyTask("SUCCESSFULLY UPDATED GENERAL PROPERTY");

            executor.execute(savePropertyTask);
        }
    }

    @FXML
    public void handleSaveCustomerClassButton(){
        this.property.setCustomerClasses(this.customerClassTable.getItems());
        Task<PropertyDTO> saveCustomerClassTask = generateSavePropertyTask("SUCCESSFULLY UPDATED CUSTOMER CLASS PROPERTY");

        executor.execute(saveCustomerClassTask);
    }

    @FXML
    public void handleModifyTermsButton(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(TaimDesktop.class.getResource("/fxml/settings/ModifyTerms.fxml"));
            AnchorPane page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Modify Invoice Terms");
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            dialogStage.initModality(Modality.WINDOW_MODAL);

            ModifyTermsController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.initData(this.property, this.executor, this.propertyClient);
            dialogStage.setResizable(false);
            dialogStage.getIcons().add(new Image(TaimDesktop.class.getResourceAsStream(Constant.Image.appIconPath)));
            dialogStage.showAndWait();

            initUI(controller.getProperty());
        }catch(IOException e){
            logger.error(e.getMessage(), e);
        }
    }

    @FXML
    public void handleUploadLicenseButton(){
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("DAT files (*.dat)", "*.dat");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setTitle("Upload New License");
        File licenseFile = fileChooser.showOpenDialog(rootPane.getScene().getWindow());

        boolean isLicenseValid = false;
        try{
            generateLicense.verifyLicense(licenseFile, DESKTOP_PRODUCT);
            isLicenseValid = true;
        }catch (GenerateLicense.GenerateLicenseException e){
            logger.error(e.getMessage(), e);
            new AlertBuilder()
                    .alertType(Alert.AlertType.ERROR)
                    .alertContentText(e.getMessage())
                    .build()
                    .showAndWait();
        }

        if(isLicenseValid){
            try {
                property.getLicense().setLicenseFile(Files.readAllBytes(licenseFile.toPath()));
                Task<PropertyDTO> savePropertyTask = generateSavePropertyTask("SUCCESSFULLY UPLOADED LICENSE");

                executor.execute(savePropertyTask);
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    public void scanCompanyInfoFields(){
        companyNameField.requestFocus();
        productWarnLimitField.requestFocus();
        gstNumField.requestFocus();
        gstRateField.requestFocus();
        pstRateField.requestFocus();
        gstRateErrorLabel.requestFocus();
        gstNumErrorLabel.requestFocus();
    }

    public boolean isCompanyInfoValid(){
        return  StringUtils.isEmpty(gstRateErrorLabel.getText())
                && StringUtils.isEmpty(gstNumErrorLabel.getText())
                && StringUtils.isEmpty(productWarnLimitErrorLabel.getText())
                && StringUtils.isEmpty(companyNameErrorLabel.getText())
                && StringUtils.isEmpty(pstRateErrorLabel.getText());
    }

    public TableView<PropertyDTO.CustomerClassDTO> getCustomerClassTable() {
        return customerClassTable;
    }

    public PropertyDTO getProperty() {
        return property;
    }

    public PropertyClient getPropertyClient() {
        return propertyClient;
    }

    public Executor getExecutor() {
        return executor;
    }

    private void initGeneralInfoTextFields(){
        companyNameField.textProperty().bindBidirectional(this.property.companyNameProperty());
        productWarnLimitField.textProperty().bindBidirectional(this.property.productWarnLimitProperty(), new NumberStringConverter());
        gstNumField.textProperty().bindBidirectional(this.property.gstNumberProperty());
        gstRateField.textProperty().bindBidirectional(this.property.gstRateProperty(), new NumberStringConverter());
        pstRateField.textProperty().bindBidirectional(this.property.pstRateProperty(), new NumberStringConverter());
    }

    private PropertyDTO.CustomerClassDTO createCustomerClass(){
        PropertyDTO.CustomerClassDTO newCustomerClassDTO = new PropertyDTO.CustomerClassDTO();
        newCustomerClassDTO.setCustomerClassName("Please change customer class name");
        newCustomerClassDTO.setCustomerDiscount(0);
        newCustomerClassDTO.setDateCreated(DateTime.now());
        newCustomerClassDTO.setDateModified(newCustomerClassDTO.getDateCreated());

        return newCustomerClassDTO;
    }

    private void initLicenseFields(){
        File tmpLicense = null;
        try{
            tmpLicense = File.createTempFile("tmplicense", ".dat");
            Files.write(tmpLicense.toPath(), property.getLicense().getLicenseFile());
        }catch (IOException e){
            logger.error(e.getMessage());
        }

        try{
            generateLicense.setTargetFile(tmpLicense);
            generateLicense.unmarshall();

            generateLicense.getLicenses().getLicense().forEach(licenseBase -> {
                if(licenseBase.getProduct().equals(DESKTOP_PRODUCT)){
                    LicenseV1 license = (LicenseV1) licenseBase;

                    Optional<LicenseV1.Option> validFrom = license.getOptions()
                            .stream().filter(option -> option.getName().equals("valid-from")).findFirst();
                    Optional<LicenseV1.Option> recordId = license.getOptions()
                            .stream().filter(option -> option.getName().equals("record-id")).findFirst();
                    Optional<LicenseV1.Option> maxTransactions = license.getOptions()
                            .stream().filter(option -> option.getName().equals("max-transactions")).findFirst();
                    Optional<LicenseV1.Option> maxUsers = license.getOptions()
                            .stream().filter(option -> option.getName().equals("user-count")).findFirst();

                    licenseProductLabel.setText(license.getProduct());
                    licenseCustomerLabel.setText(license.getCustomer());
                    licenseProjectLabel.setText(license.getProject());
                    licenseValidFromLabel.setText(validFrom.isPresent()? String.valueOf(validFrom.get().getValue()) : "");
                    licenseValidUntilLabel.setText(license.getValidUntil());
                    licenseRecordIdLabel.setText(recordId.isPresent()? String.valueOf(recordId.get().getValue()): "");
                    licenseMaxTransactionsLabel.setText(maxTransactions.isPresent()? String.valueOf(maxTransactions.get().getValue()): "");
                    licenseMaxUsersLabel.setText(maxUsers.isPresent()? String.valueOf(maxUsers.get().getValue()): "");
                }
            });
        }catch (LicenseFile.LicenseException e){
            logger.error(e.getMessage(), e);
            new AlertBuilder()
                    .alertType(Alert.AlertType.ERROR)
                    .alertContentText("Unable to read license information. The license might be malformed. Please upload the license again.")
                    .build()
                    .showAndWait();
        }
    }

    private Task<PropertyDTO> generateSavePropertyTask(String successfulMsg){
        Task<PropertyDTO> savePropertyTask = new Task<PropertyDTO>() {
            @Override
            protected PropertyDTO call() throws Exception {
                return propertyClient.update(property);
            }
        };

        savePropertyTask.setOnSucceeded(event -> {
            FadingStatusMessage.flash(successfulMsg , rootPane);
            initUI(savePropertyTask.getValue());
        });

        savePropertyTask.exceptionProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null) {
                Exception ex = (Exception) newValue;
                logger.error(ExceptionUtils.getRootCause(ex).getMessage());
                JSONObject errorMsg = new JSONObject(ExceptionUtils.getRootCause(ex).getMessage());
                new AlertBuilder().alertType(Alert.AlertType.ERROR)
                        .alertContentText(errorMsg.getString("taimErrorMessage"))
                        .build()
                        .showAndWait();
                if(errorMsg.getInt("taimErrorCode") == 1){
                    Task<PropertyDTO> getPropertyTask = new Task<PropertyDTO>() {
                        @Override
                        protected PropertyDTO call() throws Exception {
                            return RestClientFactory.getPropertyClient().getById(property.getId());
                        }
                    };
                    getPropertyTask.setOnSucceeded(event -> {
                        initUI(getPropertyTask.getValue());
                    });
                    getPropertyTask.exceptionProperty().addListener((observable1, oldValue1, newValue1) -> {
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

                    executor.execute(getPropertyTask);
                }
            }
        });

        return savePropertyTask;
    }
}
