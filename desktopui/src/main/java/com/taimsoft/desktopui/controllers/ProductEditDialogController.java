package com.taimsoft.desktopui.controllers;

import com.taim.dto.ProductDTO;
import com.taim.model.Product;
import com.taim.model.Staff;
import com.taimsoft.desktopui.util.AlertBuilder;
import com.taimsoft.desktopui.util.VistaNavigator;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


/**
 * Created by tjin on 12/2/2015.
 */
public class ProductEditDialogController {
    @FXML
    private TextField productIdField;
    @FXML
    private TextField textureField;
    @FXML
    private TextField unitPriceField;
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

    private Stage dialogStage;
    private ProductDTO product;
    private String errorMsg;
    private boolean okClicked;

    @FXML
    private void initialize(){}

    public ProductEditDialogController(){
        errorMsg = "";
        okClicked = false;
    }
    public void setDialogStage(Stage dialogStage){
        this.dialogStage = dialogStage;
    }
    public void setTextField(ProductDTO product){
        this.product = product;
        if(product.getSku() != null){
            productIdField.setText(product.getSku());
            displayNameField.setText(product.getDisplayName());
            textureField.setText(product.getTexture());
            unitPriceField.setText(String.valueOf(product.getUnitPrice()));
            piecesPerBoxField.setText(String.valueOf(product.getPiecePerBox()));
            lengthField.setText(String.valueOf(product.getLength()));
            widthField.setText(String.valueOf(product.getWidth()));
            heightField.setText(String.valueOf(product.getHeight()));
        }
        else{
            productIdField.setText("");
            displayNameField.setText("");
            textureField.setText("");
            unitPriceField.setText("");
            piecesPerBoxField.setText("");
            lengthField.setText("");
            widthField.setText("");
            heightField.setText("");
        }

    }
    public void handleOk(){
        if(isInputValid()){
            product.setSku(productIdField.getText());
            product.setDisplayName(displayNameField.getText());
            product.setTexture(textureField.getText());
            product.setUnitPrice(Float.valueOf(unitPriceField.getText()));
            product.setLength(Double.valueOf(lengthField.getText()));
            product.setWidth(Double.valueOf(widthField.getText()));
            product.setHeight(Double.valueOf(heightField.getText()));

            product.setPiecePerBox(Integer.parseInt(piecesPerBoxField.getText()));

            okClicked = true;
            dialogStage.close();
        }
        else{
            new AlertBuilder()
                    .alertType(Alert.AlertType.WARNING)
                    .alertHeaderText("Please correct the invalid fields")
                    .alertTitle("Invalid Fields")
                    .alertContentText(errorMsg)
                    .build()
                    .showAndWait();
        }
    }
    public void handleCancel(){
        dialogStage.close();
    }

    public boolean isOKClicked(){
        return okClicked;
    }

    public void setMainClass(boolean isEditClicked) {
        if(VistaNavigator.getGlobalStaff().getPosition()!= Staff.Position.MANAGER && !isEditClicked){
            unitPriceField.setDisable(true);
        }
        if(!isEditClicked){
            productIdField.setDisable(true);
        }
    }

    private boolean isInputValid(){
        errorMsg  = "";
        if(productIdField.getText() == null || productIdField.getText().length() == 0){
            errorMsg += "Product SKU should not be empty! \n";
        }
        if(displayNameField.getText() == null || displayNameField.getText().length() == 0){
            errorMsg += "Product Display Name should not be empty! \n";
        }
        try{
            Double.parseDouble(lengthField.getText());
        }catch (NumberFormatException e){
            errorMsg += "Product Length must be number! \n";
        }
        try{
            Double.parseDouble(widthField.getText());
        }catch (NumberFormatException e){
            errorMsg += "Product Width must be number! \n";
        }
        try{
            Double.parseDouble(heightField.getText());
        }catch (NumberFormatException e){
            errorMsg += "Product Height must be number! \n";
        }
        try{
            Float.parseFloat(unitPriceField.getText());
        }catch (NumberFormatException e){
            errorMsg += "Unit Price must be number! \n";
        }
        try{
            Integer.parseInt(piecesPerBoxField.getText());
        }catch (NumberFormatException e){
            errorMsg += "Pieces per box must be integer! \n";
        }
        if(errorMsg.length() == 0){
            return true;
        }
        return false;
    }
}
