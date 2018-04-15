package com.taim.dto;

import com.taim.dto.basedtos.BaseModelDTO;
import com.taim.dto.basedtos.TransactionBaseModelDTO;
import com.taim.model.Payment;
import javafx.beans.property.*;

import java.util.ArrayList;
import java.util.List;

public class PaymentDTO extends TransactionBaseModelDTO {
    private DoubleProperty paymentAmount;
    private ObjectProperty<Payment.PaymentType> paymentType;
    private ObjectProperty<PaymentMethodDTO> paymentMethod;
    private LongProperty staffID;
    private LongProperty userID;
    private List<PaymentDetailDTO> paymentDetails;
    private StringProperty note;

    public PaymentDTO(){
        paymentAmount = new SimpleDoubleProperty();
        paymentType = new SimpleObjectProperty<>();
        paymentMethod = new SimpleObjectProperty<>();
        staffID = new SimpleLongProperty();
        userID = new SimpleLongProperty();
        paymentDetails = new ArrayList<>();
        note = new SimpleStringProperty();
    }

    public List<PaymentDetailDTO> getPaymentDetails() {
        return paymentDetails;
    }

    public void setPaymentDetails(List<PaymentDetailDTO> paymentDetails) {
        this.paymentDetails = paymentDetails;
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

    public PaymentMethodDTO getPaymentMethod() {
        return paymentMethod.get();
    }

    public ObjectProperty<PaymentMethodDTO> paymentMethodProperty() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethodDTO paymentMethod) {
        this.paymentMethod.set(paymentMethod);
    }

    public long getStaffID() {
        return staffID.get();
    }

    public LongProperty staffIDProperty() {
        return staffID;
    }

    public void setStaffID(long staffID) {
        this.staffID.set(staffID);
    }

    public long getUserID() {
        return userID.get();
    }

    public LongProperty userIDProperty() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID.set(userID);
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
}
