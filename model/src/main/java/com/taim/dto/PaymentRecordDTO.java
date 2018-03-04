package com.taim.dto;

import com.taim.dto.basedtos.BaseModelDTO;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;

public class PaymentRecordDTO extends BaseModelDTO {
    private ObjectProperty<PaymentDTO> payment;
    private DoubleProperty amount;

    public PaymentRecordDTO(){
        this.payment = new SimpleObjectProperty<>();
        this.amount = new SimpleDoubleProperty();
    }

    public PaymentDTO getPayment() {
        return payment.get();
    }

    public ObjectProperty<PaymentDTO> paymentProperty() {
        return payment;
    }

    public void setPayment(PaymentDTO payment) {
        this.payment.set(payment);
    }

    public double getAmount() {
        return amount.get();
    }

    public DoubleProperty amountProperty() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount.set(amount);
    }
}
