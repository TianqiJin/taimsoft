package com.taimsoft.desktopui.controllers.settings;

import com.taim.client.PropertyClient;
import com.taim.client.StaffClient;
import com.taim.dto.PropertyDTO;
import com.taim.dto.StaffDTO;
import com.taimsoft.desktopui.uicomponents.FadingStatusMessage;
import com.taimsoft.desktopui.util.RestClientFactory;
import com.taimsoft.desktopui.util.VistaNavigator;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.core.Is;
import org.joda.time.DateTime;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static com.taimsoft.desktopui.constants.Constant.FXStyle.FX_ERROR_LABEL_COLOR;

public class GeneralSettingsController implements ISettingsController{
    private Executor executor;
    private PropertyClient propertyClient;
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
    private TextField companyNameField;
    @FXML
    private TextField productWarnLimitField;
    @FXML
    private TextField gstNumField;
    @FXML
    private TextField gstRateField;
    @FXML
    private TextField pstRateField;
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
                            ComboBox<String> comboBox = new ComboBox<>();
                            setGraphic(comboBox);
                            comboBox.setPromptText("SET ACTION");
                            comboBox.prefWidthProperty().bind(this.widthProperty());
                            comboBox.setItems(FXCollections.observableArrayList("ADD NEW", "DELETE"));
                            comboBox.valueProperty().addListener(((observable, oldValue, newValue) -> {
                                if(newValue.equals("DELETE")){
                                    int selectedIndex = getTableRow().getIndex();
                                    getTableView().getItems().remove(selectedIndex);
                                }else if(newValue.equals("ADD NEW")){
                                    PropertyDTO.CustomerClassDTO newCustomerClassDTO = new PropertyDTO.CustomerClassDTO();
                                    newCustomerClassDTO.setCustomerClassName("Please change customer class name");
                                    newCustomerClassDTO.setCustomerDiscount(0);
                                    newCustomerClassDTO.setDateCreated(DateTime.now());
                                    newCustomerClassDTO.setDateModified(newCustomerClassDTO.getDateCreated());
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
        this.propertyClient = RestClientFactory.getPropertyClient();
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
            }
        });

        executor.execute(getPropertyTask);
    }

    public void initWelcomeData(){
        this.property = new PropertyDTO();
        this.property.setDateCreated(DateTime.now());
        this.property.setDateModified(DateTime.now());
        initGeneralInfoTextFields();
    }

    @FXML
    public void handleSavePropertyButton(){
        if(StringUtils.isEmpty(gstRateErrorLabel.getText())
                && StringUtils.isEmpty(gstNumErrorLabel.getText())
                && StringUtils.isEmpty(productWarnLimitErrorLabel.getText())
                && StringUtils.isEmpty(companyNameErrorLabel.getText())
                && StringUtils.isEmpty(pstRateErrorLabel.getText())){
            Task<PropertyDTO> savePropertyTask = new Task<PropertyDTO>() {
                @Override
                protected PropertyDTO call() throws Exception {
                    return propertyClient.update(property);
                }
            };

            savePropertyTask.setOnSucceeded(event -> {
                FadingStatusMessage.flash("Successful", rootPane);
                this.property = savePropertyTask.getValue();
                VistaNavigator.setGlobalProperty(this.property);
                initGeneralInfoTextFields();
            });

            savePropertyTask.setOnFailed(event -> {
                System.out.println(event.getSource().exceptionProperty().toString());
                FadingStatusMessage.flash("Failed", rootPane);
            });

            executor.execute(savePropertyTask);
        }
    }

    @FXML
    public void handleSaveCustomerClassButton(){
        this.property.setCustomerClasses(this.customerClassTable.getItems());
        Task<PropertyDTO> saveCustomerClassTask = new Task<PropertyDTO>() {
            @Override
            protected PropertyDTO call() throws Exception {
                return propertyClient.update(property);
            }
        };

        saveCustomerClassTask.setOnSucceeded(event -> {
            FadingStatusMessage.flash("Successful", rootPane);
            this.property = saveCustomerClassTask.getValue();
            VistaNavigator.setGlobalProperty(this.property);
            this.customerClassTable.setItems(FXCollections.observableArrayList(this.property.getCustomerClasses()));
        });

        saveCustomerClassTask.setOnFailed(event -> {
            FadingStatusMessage.flash("Failed", rootPane);
        });

        executor.execute(saveCustomerClassTask);
    }

    private void initGeneralInfoTextFields(){
        companyNameField.textProperty().bindBidirectional(this.property.companyNameProperty());
        productWarnLimitField.textProperty().bindBidirectional(this.property.productWarnLimitProperty(), new NumberStringConverter());
        gstNumField.textProperty().bindBidirectional(this.property.gstNumberProperty());
        gstRateField.textProperty().bindBidirectional(this.property.gstRateProperty(), new NumberStringConverter());
        pstRateField.textProperty().bindBidirectional(this.property.pstRateProperty(), new NumberStringConverter());
    }
}
