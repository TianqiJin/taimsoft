package com.taim.dto;

import com.taim.dto.basedtos.BaseModelDTO;
import com.taim.dto.basedtos.TransactionBaseModelDTO;
import com.taim.model.Payment;
import javafx.beans.property.*;

public class PaymentDTO extends TransactionBaseModelDTO {
    private DoubleProperty paymentAmount;
    private ObjectProperty<Payment.PaymentType> paymentType;
    private ObjectProperty<PaymentMethodDTO> paymentMethod;
    private ObjectProperty<StaffDTO> staff;
    private ObjectProperty<CustomerDTO> customer;
    private ObjectProperty<VendorDTO> vendor;
    private StringProperty note;

    public PaymentDTO(){
        paymentAmount = new SimpleDoubleProperty();
        paymentType = new SimpleObjectProperty<>();
        paymentMethod = new SimpleObjectProperty<>();
        staff = new SimpleObjectProperty<>();
        customer = new SimpleObjectProperty<>();
        vendor = new SimpleObjectProperty<>();
        note = new SimpleStringProperty();
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

    public StaffDTO getStaff() {
        return staff.get();
    }

    public ObjectProperty<StaffDTO> staffProperty() {
        return staff;
    }

    public void setStaff(StaffDTO staff) {
        this.staff.set(staff);
    }

    public CustomerDTO getCustomer() {
        return customer.get();
    }

    public ObjectProperty<CustomerDTO> customerProperty() {
        return customer;
    }

    public void setCustomer(CustomerDTO customer) {
        this.customer.set(customer);
    }

    public VendorDTO getVendor() {
        return vendor.get();
    }

    public ObjectProperty<VendorDTO> vendorProperty() {
        return vendor;
    }

    public void setVendor(VendorDTO vendor) {
        this.vendor.set(vendor);
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
