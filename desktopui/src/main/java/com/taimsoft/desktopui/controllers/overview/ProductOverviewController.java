package com.taimsoft.desktopui.controllers.overview;

import com.taim.client.IClient;
import com.taim.client.ProductClient;
import com.taim.dto.ProductDTO;
import com.taimsoft.desktopui.controllers.edit.ProductEditDialogController;
import com.taimsoft.desktopui.util.AlertBuilder;
import com.taimsoft.desktopui.util.RestClientFactory;
import com.taimsoft.desktopui.util.TransactionPanelLoader;
import com.taimsoft.desktopui.util.VistaNavigator;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import org.joda.time.DateTime;


/**
 * Created by Tjin on 8/30/2017.
 */
public class ProductOverviewController extends IOverviewController<ProductDTO> {
    private ProductClient productClient;

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
    private SplitPane summarySplitPane;

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
        actionCol.setCellFactory(new Callback<TableColumn<ProductDTO, String>, TableCell<ProductDTO, String>>() {
            @Override
            public TableCell<ProductDTO, String> call(TableColumn<ProductDTO, String> param) {
                return new TableCell<ProductDTO, String>(){
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            ComboBox<String> comboBox = new ComboBox<>();
                            comboBox.setPromptText("SET ACTION");
                            comboBox.prefWidthProperty().bind(this.widthProperty());
                            ProductDTO productDTO = getTableView().getItems().get(getIndex());
                            comboBox.setItems(FXCollections.observableArrayList("VIEW DETAILS", "EDIT", "DELETE"));
                            comboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
                                if(newValue.equals("VIEW DETAILS")){
                                    VistaNavigator.loadDetailVista(VistaNavigator.VISTA_PRODUCT_DETAIL, productDTO);
                                }else if(newValue.equals("EDIT")){
                                    ProductEditDialogController controller = TransactionPanelLoader.showProductEditor(productDTO);
                                    if(controller != null && controller.isOKClicked()){
                                        getTableView().getItems().set(getIndex(), controller.getProduct());
                                    }
                                }
                            });
                            comboBox.setValue(item);
                            setGraphic(comboBox);
                        }
                    }
                };
            }
        });
    }

    @FXML
    public void handleAddProduct(){
        ProductDTO newProduct = new ProductDTO();
        newProduct.setDateCreated(DateTime.now());
        newProduct.setDateModified(DateTime.now());
        ProductEditDialogController controller = TransactionPanelLoader.showProductEditor(newProduct);
        if(controller != null && controller.isOKClicked()){
            getOverviewTable().getItems().add(controller.getProduct());
        }
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

    @Override
    public void initSummaryLabel() {}


    private ComboBox<String> initActionCombox(ProductDTO productDTO){
        if(productDTO.getId() == 0){
            return new ComboBox<>(FXCollections.observableArrayList("Edit", "Print", "Delete"));
        }else{
            return new ComboBox<>(FXCollections.observableArrayList("Edit", "Print", "Delete2"));
        }
    }
}
