package com.taim.desktopui.controllers.edit;

import com.taim.desktopui.util.AlertBuilder;
import com.taim.desktopui.util.RestClientFactory;
import com.taim.dto.ProductDTO;
import com.taim.model.Staff;
import com.taim.desktopui.util.VistaNavigator;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static com.taim.desktopui.constants.Constant.FXStyle.FX_ERROR_LABEL_COLOR;


/**
 * Created by tjin on 12/2/2015.
 */
public class ProductEditDialogController {
    private static final Logger logger = LoggerFactory.getLogger(ProductEditDialogController.class);
    private Stage dialogStage;
    private ProductDTO product;
    private boolean okClicked;
    private Executor executor;

    @FXML
    private TextField productIdField;
    @FXML
    private TextField textureField;
    @FXML
    private TextField unitPriceField;
    @FXML
    private TextField stockUnitPriceField;
    @FXML
    private TextField lengthField;
    @FXML
    private TextField widthField;
    @FXML
    private TextField piecesPerBoxField;
    @FXML
    private TextField heightField;
    @FXML
    private TextField displayNameField;
    @FXML
    private Label productIdErrorLabel;
    @FXML
    private Label displayNameErrorLabel;
    @FXML
    private Label unitPriceErrorLabel;
    @FXML
    private Label piecesPerBoxErrorLabel;
    @FXML
    private Label stockUnitPriceErrorLabel;
    @FXML
    private AnchorPane root;

    @FXML
    private void initialize(){
        productIdErrorLabel.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue) { // we only care about loosing focus
                if(StringUtils.isNotEmpty(productIdField.getText())){
                    productIdErrorLabel.setText("");
                }else{
                    productIdErrorLabel.setText("SKU must not be empty!");
                    productIdErrorLabel.setStyle(FX_ERROR_LABEL_COLOR);
                }
            }
        });
        displayNameField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue) { // we only care about loosing focus
                if(StringUtils.isNotEmpty(displayNameField.getText())){
                    displayNameErrorLabel.setText("");
                }else{
                    displayNameErrorLabel.setText("Display name must not be empty!");
                    displayNameErrorLabel.setStyle(FX_ERROR_LABEL_COLOR);
                }
            }
        });
        unitPriceField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue) { // we only care about loosing focus
                if(StringUtils.isNotEmpty(unitPriceField.getText())){
                    unitPriceErrorLabel.setText("");
                }else{
                    unitPriceErrorLabel.setText("Unit price must not be empty!");
                    unitPriceErrorLabel.setStyle(FX_ERROR_LABEL_COLOR);
                }

                try{
                    Double.parseDouble(unitPriceField.getText());
                }catch (NumberFormatException e){
                    unitPriceErrorLabel.setText("Unit price must a number!");
                    unitPriceErrorLabel.setStyle(FX_ERROR_LABEL_COLOR);
                }
            }
        });
        piecesPerBoxField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue) { // we only care about loosing focus
                if(StringUtils.isNotEmpty(piecesPerBoxField.getText())){
                    piecesPerBoxErrorLabel.setText("");
                }else{
                    piecesPerBoxErrorLabel.setText("Pieces per box must not be empty!");
                    piecesPerBoxErrorLabel.setStyle(FX_ERROR_LABEL_COLOR);
                }

                try{
                    Integer.parseInt(piecesPerBoxField.getText());
                }catch (NumberFormatException e){
                    piecesPerBoxErrorLabel.setText("Pieces per box must be an integer!");
                    piecesPerBoxErrorLabel.setStyle(FX_ERROR_LABEL_COLOR);
                }

                if(Integer.parseInt(piecesPerBoxField.getText()) <= 0){
                    piecesPerBoxErrorLabel.setText("Pieces per box must be greater than 0!");
                    piecesPerBoxErrorLabel.setStyle(FX_ERROR_LABEL_COLOR);
                }
            }
        });
        stockUnitPriceField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue) { // we only care about loosing focus
                if(StringUtils.isNotEmpty(stockUnitPriceField.getText())){
                    try{
                        Double.parseDouble(stockUnitPriceField.getText());
                        stockUnitPriceErrorLabel.setText("");
                    }catch (NumberFormatException e){
                        stockUnitPriceErrorLabel.setText("Stock unit price must be a number!");
                        stockUnitPriceErrorLabel.setStyle(FX_ERROR_LABEL_COLOR);
                    }
                }else{
                    stockUnitPriceErrorLabel.setText("");
                }
            }
        });
    }

    public ProductEditDialogController(){
        okClicked = false;
        executor = Executors.newCachedThreadPool(r -> {
            Thread t = new Thread(r);
            t.setDaemon(true);
            return t;
        });
    }

    public void setDialogStage(Stage dialogStage){
        this.dialogStage = dialogStage;
    }
    public void initData(ProductDTO product){
        this.product = product;
        initProductInfoTextFields();
    }

    public void handleOk(){
        scanBasicInfoFields();
        if(isInputValid()){
            Task<ProductDTO> saveUpdateProductTask = new Task<ProductDTO>() {
                @Override
                protected ProductDTO call() throws Exception {
                    return RestClientFactory.getProductClient().saveOrUpdate(product);
                }
            };
            
            saveUpdateProductTask.setOnSucceeded(event -> {
                this.product = saveUpdateProductTask.getValue();
                okClicked = true;
                dialogStage.close();
            });

            saveUpdateProductTask.exceptionProperty().addListener((observable, oldValue, newValue) -> {
                if(newValue != null) {
                    Exception ex = (Exception) newValue;
                    logger.error(ExceptionUtils.getRootCause(ex).getMessage());
                    JSONObject errorMsg = new JSONObject(ExceptionUtils.getRootCause(ex).getMessage());
                    new AlertBuilder().alertType(Alert.AlertType.ERROR)
                            .alertContentText(errorMsg.getString("taimErrorMessage"))
                            .build()
                            .showAndWait();
                    if(errorMsg.getInt("taimErrorCode") == 1){
                        Task<ProductDTO> getProductTask = new Task<ProductDTO>() {
                            @Override
                            protected ProductDTO call() throws Exception {
                                return RestClientFactory.getProductClient().getById(product.getId());
                            }
                        };
                        getProductTask.setOnSucceeded(event -> {
                            initData(getProductTask.getValue());
                        });
                        getProductTask.exceptionProperty().addListener((observable1, oldValue1, newValue1) -> {
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

                        executor.execute(getProductTask);
                    }
                }
            });

            executor.execute(saveUpdateProductTask);
        }

    }
    public void handleCancel(){
        dialogStage.close();
    }

    public boolean isOKClicked(){
        return okClicked;
    }

    private boolean isInputValid(){
        return StringUtils.isEmpty(productIdErrorLabel.getText())
                && StringUtils.isEmpty(displayNameErrorLabel.getText())
                && StringUtils.isEmpty(piecesPerBoxErrorLabel.getText())
                && StringUtils.isEmpty(unitPriceErrorLabel.getText())
                && StringUtils.isEmpty(stockUnitPriceErrorLabel.getText());
    }

    private void scanBasicInfoFields(){
        productIdField.requestFocus();
        displayNameField.requestFocus();
        piecesPerBoxField.requestFocus();
        unitPriceField.requestFocus();
        stockUnitPriceField.requestFocus();
    }

    private void initProductInfoTextFields(){
        productIdField.textProperty().bindBidirectional(this.product.skuProperty());
        displayNameField.textProperty().bindBidirectional(this.product.displayNameProperty());
        lengthField.textProperty().bindBidirectional(this.product.lengthProperty(), new NumberStringConverter());
        widthField.textProperty().bindBidirectional(this.product.widthProperty(), new NumberStringConverter());
        heightField.textProperty().bindBidirectional(this.product.heightProperty(), new NumberStringConverter());
        piecesPerBoxField.textProperty().bindBidirectional(this.product.piecePerBoxProperty(), new NumberStringConverter());
        textureField.textProperty().bindBidirectional(this.product.textureProperty());
        unitPriceField.textProperty().bindBidirectional(this.product.unitPriceProperty(), new NumberStringConverter());
        stockUnitPriceField.textProperty().bindBidirectional(this.product.stockUnitPriceProperty(), new NumberStringConverter());

        if(this.product.getId() != 0){
            if(VistaNavigator.getGlobalStaff().getPosition().equals(Staff.Position.MANAGER)){
                unitPriceField.setDisable(false);
            }
            productIdField.setDisable(true);
        }
    }

    public ProductDTO getProduct() {
        return this.product;
    }
}
