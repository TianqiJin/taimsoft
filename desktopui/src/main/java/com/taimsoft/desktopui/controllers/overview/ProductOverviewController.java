package com.taimsoft.desktopui.controllers.overview;

import com.taim.client.ProductClient;
import com.taim.dto.ProductDTO;
import com.taim.dto.StaffDTO;
import com.taimsoft.desktopui.uicomponents.LiveComboBoxTableCell;
import com.taimsoft.desktopui.util.RestClientFactory;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;


/**
 * Created by Tjin on 8/30/2017.
 */
public class ProductOverviewController implements OverviewController<ProductDTO>{
    private ObservableList<ProductDTO> productDTOS;
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
    private TableColumn<ProductDTO, String> orgCol;
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
        actionCol.setCellValueFactory(new PropertyValueFactory<>("action"));
        actionCol.setCellFactory(param -> new LiveComboBoxTableCell<>(FXCollections.observableArrayList("Edit", "Delete")));
    }

    public ProductOverviewController(){
        this.productClient = RestClientFactory.getProductClient();
    }

    @Override
    public void loadData() {
        productDTOS = FXCollections.observableArrayList(productClient.getProductList());
        productTable.setItems(productDTOS);
    }

    @Override
    public void initSearchField() {

    }
}
