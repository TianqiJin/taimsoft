package com.taim.dto;

import com.taim.dto.basedtos.BaseModelDTO;
import com.taim.model.DeliveryStatus;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class DeliveryStatusDTO extends BaseModelDTO {
    private ObjectProperty<DeliveryStatus.Status> status;

    public DeliveryStatusDTO(){
        status = new SimpleObjectProperty<DeliveryStatus.Status>();
    }

    public DeliveryStatus.Status getStatus() {
        return status.get();
    }

    public ObjectProperty<DeliveryStatus.Status> statusProperty() {
        return status;
    }

    public void setStatus(DeliveryStatus.Status status) {
        this.status.set(status);
    }
}
