package com.taim.desktopui.util;

import com.taim.dto.ProductDTO;
import com.taim.dto.TransactionDetailDTO;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Map;

/**
 * Created by jiawei.liu on 9/18/17.
 */
public class ButtonCell extends TableCell<TransactionDetailDTO, Boolean> {
    Image image = new Image(getClass().getResourceAsStream("/pics/delete.png"));
    Button cellButton = new Button();
    public ButtonCell(TableView<TransactionDetailDTO> tableView, Map<Long, Double> map, boolean edit,boolean add){
        setAlignment(Pos.CENTER_LEFT);
        cellButton.setGraphic(new ImageView(image));
        cellButton.setPadding(Insets.EMPTY);
        cellButton.setOnAction(t -> {
            int selectedIndex = getTableRow().getIndex();
            if (edit) {
                TransactionDetailDTO td = tableView.getItems().get(selectedIndex);
                ProductDTO prod = RestClientFactory.getProductClient().getById(td.getProduct().getId());
                double newNum;
                if (add){
                    newNum = prod.getVirtualTotalNum()+ td.getProduct().getVirtualTotalNum()+td.getQuantity()-map.get(td.getProduct().getId());

                }else{
                    newNum = prod.getVirtualTotalNum()+ td.getProduct().getVirtualTotalNum()-td.getQuantity()-map.get(td.getProduct().getId());
                }
                prod.setVirtualTotalNum(newNum);
                RestClientFactory.getProductClient().saveOrUpdate(prod);
            }
            tableView.getItems().remove(selectedIndex);
        });
    }
    //Display button if the row is not empty
    @Override
    protected void updateItem(Boolean t, boolean empty) {
        super.updateItem(t, empty);
        if(!empty){
            setGraphic(cellButton);
        }
        else{
            setGraphic(null);
        }
    }
}
