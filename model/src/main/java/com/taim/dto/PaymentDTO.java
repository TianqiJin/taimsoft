package com.taim.dto;

import com.taim.dto.basedtos.BaseModelDTO;
import com.taim.model.Payment;
import com.taim.model.Staff;
import javafx.beans.property.*;

public class PaymentDTO extends BaseModelDTO {
    private DoubleProperty paymentAmount;
    private ObjectProperty<Payment.PaymentType> paymentType;
    private BooleanProperty deposit;
    private ObjectProperty<StaffDTO> staff;
    private ObjectProperty<CustomerDTO> customer;
    private ObjectProperty<VendorDTO> vendor;
    private StringProperty note;

    public PaymentDTO(){
        paymentAmount = new SimpleDoubleProperty();
        paymentType = new SimpleObjectProperty<>();
        deposit = new SimpleBooleanProperty();
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

    public boolean isDeposit() {
        return deposit.get();
    }

    public BooleanProperty depositProperty() {
        return deposit;
    }

    public void setDeposit(boolean deposit) {
        this.deposit.set(deposit);
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
