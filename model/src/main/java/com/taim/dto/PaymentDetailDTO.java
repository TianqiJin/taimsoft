package com.taim.dto;

import com.taim.dto.basedtos.BaseModelDTO;
import javafx.beans.property.*;

public class PaymentDetailDTO extends BaseModelDTO {
    private ObjectProperty<TransactionDTO> transaction;
    private DoubleProperty amount;
    private BooleanProperty deposit;
    private StringProperty note;

    public PaymentDetailDTO(){
        this.transaction = new SimpleObjectProperty<>();
        this.amount = new SimpleDoubleProperty();
        this.deposit = new SimpleBooleanProperty();
        note = new SimpleStringProperty();
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

    public String getNote() {
        return note.get();
    }

    public StringProperty noteProperty() {
        return note;
    }

    public void setNote(String note) {
        this.note.set(note);
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
