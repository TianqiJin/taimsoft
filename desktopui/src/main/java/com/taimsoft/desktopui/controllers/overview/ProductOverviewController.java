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
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import java.util.stream.Collectors;

import static com.taimsoft.desktopui.constants.Constant.FXStyle.FX_ERROR_LABEL_COLOR;


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
    private TableColumn<ProductDTO, Double> virtualQuantityCol;
    @FXML
    private TableColumn<ProductDTO, String> actionCol;
    @FXML
    private TableColumn<ProductDTO, Boolean> checkedCol;
    @FXML
    private TextField productTypeField;
    @FXML
    private TextField lengthField;
    @FXML
    private TextField widthField;
    @FXML
    private TextField heightField;
    @FXML
    private Label filterErrorLabel;
    @FXML
    private CheckBox lessThanLimitCheckBox;

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
        virtualQuantityCol.setCellValueFactory(new PropertyValueFactory<>("virtualTotalNum"));
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
//                                        initOverviewData(productClient);
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
        lengthField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue) { // we only care about loosing focus
                try{
                    Double.parseDouble(lengthField.getText());
                }catch (NumberFormatException e){
                    filterErrorLabel.setText("Length must be a number!");
                    filterErrorLabel.setStyle(FX_ERROR_LABEL_COLOR);
                }
            }
        });
        widthField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue) { // we only care about loosing focus
                try{
                    Double.parseDouble(widthField.getText());
                }catch (NumberFormatException e){
                    filterErrorLabel.setText("Width must be a number!");
                    filterErrorLabel.setStyle(FX_ERROR_LABEL_COLOR);
                }
            }
        });
        heightField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue) { // we only care about loosing focus
                try{
                    Double.parseDouble(heightField.getText());
                }catch (NumberFormatException e){
                    filterErrorLabel.setText("Height must be a number!");
                    filterErrorLabel.setStyle(FX_ERROR_LABEL_COLOR);
                }
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

    @FXML
    private void handleFilterProduct(){
        if(StringUtils.isEmpty(filterErrorLabel.getText())){
            String type = productTypeField.getText();
            String length = lengthField.getText();
            String width = widthField.getText();
            String height = heightField.getText();

            ObservableList<ProductDTO> subList = FXCollections.observableArrayList(getOverviewDTOList());
            if(StringUtils.isNotEmpty(type)){
                subList = subList.stream()
                        .filter(productDTO -> productDTO.getTexture() != null && productDTO.getTexture().equals(type))
                        .collect(Collectors.toCollection(FXCollections::observableArrayList));
            }
            if(StringUtils.isNotEmpty(length)){
                subList = subList.stream().filter(productDTO -> Double.parseDouble(length) == productDTO.getLength())
                        .collect(Collectors.toCollection(FXCollections::observableArrayList));
            }
            if(StringUtils.isNotEmpty(width)){
                subList = subList.stream().filter(productDTO -> Double.parseDouble(width) == productDTO.getWidth())
                        .collect(Collectors.toCollection(FXCollections::observableArrayList));
            }
            if(StringUtils.isNotEmpty(height)){
                subList = subList.stream().filter(productDTO -> Double.parseDouble(height) == productDTO.getHeight())
                        .collect(Collectors.toCollection(FXCollections::observableArrayList));
            }
            if(lessThanLimitCheckBox.isSelected()){
                subList = subList.stream().filter(productDTO -> productDTO.getTotalNum() < VistaNavigator.getGlobalProperty().getProductWarnLimit()).collect(Collectors.toCollection(FXCollections::observableArrayList));
            }

            getOverviewTable().setItems(subList);
        }
    }

    @FXML
    private void handleClearFilter(){
        productTypeField.clear();
        lengthField.clear();
        widthField.clear();
        heightField.clear();
        lessThanLimitCheckBox.setSelected(false);
        filterErrorLabel.setText("");
        getOverviewTable().setItems(FXCollections.observableArrayList(getOverviewDTOList()));
    }



    public ProductOverviewController(){
        this.productClient = RestClientFactory.getProductClient();
    }

    @Override
    public void initSearchField() {
        FilteredList<ProductDTO> filteredData = new FilteredList<>(FXCollections.observableArrayList(getOverviewDTOList()), p->true);
        VistaNavigator.getRootLayoutController().getSearchField().textProperty().addListener((observable,oldVal,newVal)->{
            filteredData.setPredicate(productDTO -> {
                if (newVal == null || newVal.isEmpty()){
                    return true;
                }
                String lowerCase = newVal.toLowerCase();
                if(productDTO.getDateCreated().toString().toLowerCase().contains(lowerCase)){
                    return true;
                }else if(productDTO.getDisplayName().toLowerCase().contains(lowerCase)){
                    return true;
                }else if(productDTO.getSku().toLowerCase().contains(lowerCase)){
                    return true;
                }else if(String.valueOf(productDTO.getUnitPrice()).contains(lowerCase)){
                    return true;
                }else if(String.valueOf(productDTO.getTotalNum()).contains(lowerCase)){
                    return true;
                }
                return false;
            });
            getOverviewTable().setItems(filteredData);
        });
    }

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
