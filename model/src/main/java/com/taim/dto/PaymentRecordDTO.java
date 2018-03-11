package com.taim.dto;

import com.taim.dto.basedtos.BaseModelDTO;
import javafx.beans.property.*;

public class PaymentRecordDTO extends BaseModelDTO {
    private ObjectProperty<PaymentDTO> payment;
    private DoubleProperty amount;
    private BooleanProperty deposit;

    public PaymentRecordDTO(){
        this.payment = new SimpleObjectProperty<>();
        this.amount = new SimpleDoubleProperty();
        deposit = new SimpleBooleanProperty();
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
