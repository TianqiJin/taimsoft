package com.taim.dto;

import com.taim.dto.basedtos.BaseModelDTO;
import com.taim.model.Payment;
import javafx.beans.property.*;

public class PaymentDTO extends BaseModelDTO {
    private DoubleProperty paymentAmount;
    private ObjectProperty<Payment.PaymentType> paymentType;
    private BooleanProperty isDeposit;
    private ObjectProperty<TransactionDTO> transaction;

    public PaymentDTO(){
        paymentAmount = new SimpleDoubleProperty();
        paymentType = new SimpleObjectProperty<>();
        isDeposit = new SimpleBooleanProperty();
        transaction = new SimpleObjectProperty<>();
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

    public boolean isIsDeposit() {
        return isDeposit.get();
    }

    public BooleanProperty isDepositProperty() {
        return isDeposit;
    }

    public void setIsDeposit(boolean isDeposit) {
        this.isDeposit.set(isDeposit);
    }

    public TransactionDTO getTransaction() {
        return transaction.get();
    }

    public ObjectProperty<TransactionDTO> transactionProperty() {
        return transaction;
    }

    public void setTransaction(TransactionDTO transaction) {
        this.transaction.set(transaction);
    }
}
