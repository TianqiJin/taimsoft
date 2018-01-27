package com.taim.dto;

import com.taim.dto.basedtos.BaseModelDTO;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;

public class DeliveryDTO extends BaseModelDTO{
    private ObjectProperty<ProductDTO> product;
    private DoubleProperty deliveryAmount;

    public DeliveryDTO(){
        this.product = new SimpleObjectProperty<>();
        this.deliveryAmount = new SimpleDoubleProperty();
    }

    public ProductDTO getProduct() {
        return product.get();
    }

    public ObjectProperty<ProductDTO> productProperty() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product.set(product);
    }

    public double getDeliveryAmount() {
        return deliveryAmount.get();
    }

    public DoubleProperty deliveryAmountProperty() {
        return deliveryAmount;
    }

    public void setDeliveryAmount(double deliveryAmount) {
        this.deliveryAmount.set(deliveryAmount);
    }
}
