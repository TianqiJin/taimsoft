package com.taim.dto;

import com.taim.dto.basedtos.BaseModelDTO;
import com.taim.model.Payment;
import javafx.beans.property.*;

public class PaymentDTO extends BaseModelDTO {
    private DoubleProperty paymentAmount;
    private ObjectProperty<Payment.PaymentType> paymentType;
    private BooleanProperty deposit;

    public PaymentDTO(){
        paymentAmount = new SimpleDoubleProperty();
        paymentType = new SimpleObjectProperty<>();
        deposit = new SimpleBooleanProperty();
    }

    public double getPaymentAmount() {
        return paymentAmount.get();
    }

    public DoubleProperty paymentAmountProperty() {
        return paymentAmount;
    }

    public void setPaymentAmount(double paymentAmount) {
        this.paymentAmount.set(paymentAmount);
    }

    public Payment.PaymentType getPaymentType() {
        return paymentType.get();
    }

    public ObjectProperty<Payment.PaymentType> paymentTypeProperty() {
        return paymentType;
    }

    public void setPaymentType(Payment.PaymentType paymentType) {
        this.paymentType.set(paymentType);
    }

    public boolean isDeposit() {
        return deposit.get();
    }

    public BooleanProperty depositProperty() {
        return deposit;
    }

    public void setDeposit(boolean deposit) {
        this.deposit.set(deposit);
    }
}
