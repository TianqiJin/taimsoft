package com.taim.desktopui.controllers.invoices;

import com.taim.dto.TransactionDetailDTO;
import com.taim.model.TransactionDetail;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class InvoiceTransactionPreviewController {
    @FXML
    private TableView<TransactionDetailDTO> transactionDetaiTableView;
    @FXML
    private TableColumn<TransactionDetailDTO, String> productIdCol;
    @FXML
    private TableColumn<TransactionDetailDTO, String> displayNameCol;
    @FXML
    private TableColumn<TransactionDetailDTO, Double> qtyCol;
    @FXML
    private TableColumn<TransactionDetailDTO, Double> subTotalCol;
    @FXML
    private TableColumn<TransactionDetailDTO, Double> discountCol;
    @FXML
    private TableColumn<TransactionDetailDTO, Integer> boxNumCol;
    @FXML
    private TableColumn<TransactionDetailDTO, Integer> pieceNumCol;
    @FXML
    private TableColumn<TransactionDetailDTO, Double> unitPriceCol;
    @FXML
    private TableColumn<TransactionDetailDTO, String> remarkCol;

    @FXML
    private void initialize(){
        productIdCol.setCellValueFactory(param -> param.getValue().getProduct().skuProperty());
        displayNameCol.setCellValueFactory(param -> param.getValue().getProduct().displayNameProperty());
        qtyCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        subTotalCol.setCellValueFactory(new PropertyValueFactory<>("saleAmount"));
        discountCol.setCellValueFactory(new PropertyValueFactory<>("discount"));
        boxNumCol.setCellValueFactory(param -> {
            if(param.getValue().getPackageInfo() != null){
                return  param.getValue().getPackageInfo().boxProperty().asObject();
            }
            return null;
        });
        pieceNumCol.setCellValueFactory(param -> {
            if(param.getValue().getPackageInfo() != null){
                return param.getValue().getPackageInfo().piecesProperty().asObject();
            }
            return null;
        });
        unitPriceCol.setCellValueFactory(param -> param.getValue().getProduct().unitPriceProperty().asObject());
        remarkCol.setCellValueFactory(new PropertyValueFactory<>("note"));
    }

    public void initTransactionDetailsData(List<TransactionDetailDTO> transactionDetails){
        this.transactionDetaiTableView.setItems(FXCollections.observableArrayList(transactionDetails));
    }
}
