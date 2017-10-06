package com.taimsoft.desktopui.controllers.overview;

import com.taim.client.IClient;
import com.taim.client.ProductClient;
import com.taim.client.TransactionClient;
import com.taim.dto.ProductDTO;
import com.taim.dto.StaffDTO;
import com.taim.dto.TransactionDTO;
import com.taim.model.Product;
import com.taimsoft.desktopui.uicomponents.LiveComboBoxTableCell;
import com.taimsoft.desktopui.util.RestClientFactory;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


/**
 * Created by Tjin on 8/30/2017.
 */
public class ProductOverviewController extends OverviewController<ProductDTO>{
    private ProductClient productClient;

    @FXML
    private TableView<ProductDTO> productTable;
    @FXML
    private TableColumn<ProductDTO, String> nameCol;
    @FXML
    private TableColumn<ProductDTO, String> skuCol;
    @FXML
    private TableColumn<ProductDTO, String> typeCol;
    @FXML
    private TableColumn<ProductDTO, String> sizeCol;
    @FXML
    private TableColumn<ProductDTO, Double> salesPriceCol;
    @FXML
    private TableColumn<ProductDTO, Double> quantityCol;
    @FXML
    private TableColumn<ProductDTO, String> actionCol;
    @FXML
    private TableColumn<ProductDTO, Boolean> checkedCol;

    @FXML
    public void initialize(){
        nameCol.setCellValueFactory(new PropertyValueFactory<>("displayName"));
        skuCol.setCellValueFactory(new PropertyValueFactory<>("sku"));
        sizeCol.setCellValueFactory(param -> {
            StringBuilder sizeBuilder = new StringBuilder();
            sizeBuilder.append(param.getValue().getLength())
                    .append("*")
                    .append(param.getValue().getWidth())
                    .append("*")
                    .append(param.getValue().getHeight());
            return new SimpleStringProperty(sizeBuilder.toString());
        });
        typeCol.setCellValueFactory(new PropertyValueFactory<>("texture"));
        salesPriceCol.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("totalNum"));
        checkedCol.setCellValueFactory(new PropertyValueFactory<>("isChecked"));
        checkedCol.setCellFactory(CheckBoxTableCell.forTableColumn(checkedCol));
        actionCol.setCellValueFactory(new PropertyValueFactory<>("action"));
        actionCol.setCellFactory(param -> {
            LiveComboBoxTableCell<ProductDTO, String> liveComboBoxTableCell = new LiveComboBoxTableCell<>(
                    FXCollections.observableArrayList("VIEW DETAILS", "EDIT", "DELETE"));
            return liveComboBoxTableCell;
        });
    }

    public ProductOverviewController(){
        this.productClient = RestClientFactory.getProductClient();
    }

    @Override
    public void initSearchField() {}

    @Override
    public IClient<ProductDTO> getOverviewClient(){
        return this.productClient;
    }

    private ComboBox<String> initActionCombox(ProductDTO productDTO){
        System.out.println(productDTO.getId());
        if(productDTO.getId() == 0){
            return new ComboBox<>(FXCollections.observableArrayList("Edit", "Print", "Delete"));
        }else{
            return new ComboBox<>(FXCollections.observableArrayList("Edit", "Print", "Delete2"));
        }
    }
}
