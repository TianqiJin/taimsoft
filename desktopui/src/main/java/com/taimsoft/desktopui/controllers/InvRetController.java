package com.taimsoft.desktopui.controllers;

import com.taim.dto.TransactionDTO;
import com.taim.model.Transaction;
import com.taimsoft.desktopui.util.AlertBuilder;
import com.taimsoft.desktopui.util.RestClientFactory;
import com.taimsoft.desktopui.util.TransactionPanelLoader;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by jiawei.liu on 11/12/17.
 */
public class InvRetController {
    private Stage dialogStage;
    private TransactionDTO transactionDTO;
    private boolean confirmedClicked;
    private Transaction.TransactionType transactionType;

    @FXML
    private TextField transactionIdField;
    @FXML
    private Button confirmButton;
    @FXML
    private Button cancelButton;

    @FXML
    private void initialize(){
    }

    @FXML
    public void handleCancelButton(){
        this.dialogStage.close();
    }

    @FXML
    public void handleConfirmButton() throws IOException, SQLException {
        if (isIdtValid()){
            transactionDTO = RestClientFactory.getTransactionClient().getById(Integer.parseInt(transactionIdField.getText()));
            if (transactionDTO.getTransactionType()== Transaction.TransactionType.QUOTATION && transactionType == Transaction.TransactionType.INVOICE){
                TransactionPanelLoader.loadInvoice(transactionDTO);
            }else if (transactionDTO.getTransactionType() == Transaction.TransactionType.INVOICE && transactionType == Transaction.TransactionType.RETURN){
                TransactionPanelLoader.loadReturn(transactionDTO);
            }
            confirmedClicked = true;
            dialogStage.close();

        }else {
            new AlertBuilder()
                    .alertType(Alert.AlertType.WARNING)
                    .alertHeaderText("Please correct the invalid fields")
                    .alertTitle("Invalid Fields")
                    .build()
                    .showAndWait();
        }

    }

    public InvRetController(){
        confirmedClicked = false;
    }

    public void setDialogStage(Stage dialogStage){
        this.dialogStage = dialogStage;
    }

    public void assignType(Transaction.TransactionType type){
        transactionType = type;
    }

    public boolean isConfirmedClicked(){
        return this.confirmedClicked;
    }

    private boolean isIdtValid(){
        try{
            Integer.parseInt(transactionIdField.getText());
        }catch(NumberFormatException e){
            return false;
        }
        return true;
    }
}
