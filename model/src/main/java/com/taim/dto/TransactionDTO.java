package com.taim.dto;

import com.taim.dto.basedtos.BaseModelDTO;
import com.taim.model.*;
import javafx.beans.property.*;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class TransactionDTO extends BaseModelDTO {
    private DoubleProperty saleAmount;
    private DoubleProperty gst;
    private DoubleProperty pst;
    private ObjectProperty<StaffDTO> staff;
    private ObjectProperty<CustomerDTO> customer;
    private ObjectProperty<VendorDTO> vendor;
    private ObjectProperty<Transaction.TransactionType> transactionType;
    private ObjectProperty<Transaction.PaymentStatus> paymentStatus;
    private ObjectProperty<DateTime> paymentDueDate;
    private ObjectProperty<DeliveryStatusDTO> deliveryStatus;
    private ObjectProperty<DateTime> deliveryDueDate;
    private List<TransactionDetailDTO> transactionDetails;
    private List<PaymentDTO> payments;
    private IntegerProperty refId;
    private StringProperty note;
    private BooleanProperty isChecked;
    public TransactionDTO(){
        saleAmount = new SimpleDoubleProperty();
        gst = new SimpleDoubleProperty();
        pst = new SimpleDoubleProperty();
        staff = new SimpleObjectProperty<>();
        customer = new SimpleObjectProperty<>();
        vendor = new SimpleObjectProperty<>();
        transactionType = new SimpleObjectProperty<>();
        paymentStatus = new SimpleObjectProperty<>();
        deliveryStatus = new SimpleObjectProperty<>();
        transactionDetails = new ArrayList<>();
        payments = new ArrayList<>();
        refId = new SimpleIntegerProperty();
        note = new SimpleStringProperty();
        deliveryDueDate = new SimpleObjectProperty<>();
        paymentDueDate = new SimpleObjectProperty<>();
        isChecked = new SimpleBooleanProperty();
    }

    public double getSaleAmount() {
        return saleAmount.get();
    }

    public DoubleProperty saleAmountProperty() {
        return saleAmount;
    }

    public void setSaleAmount(double saleAmount) {
        this.saleAmount.set(saleAmount);
    }

    public double getGst() {
        return gst.get();
    }

    public DoubleProperty gstProperty() {
        return gst;
    }

    public void setGst(double gst) {
        this.gst.set(gst);
    }

    public double getPst() {
        return pst.get();
    }

    public DoubleProperty pstProperty() {
        return pst;
    }

    public void setPst(double pst) {
        this.pst.set(pst);
    }

    public Transaction.TransactionType getTransactionType() {
        return transactionType.get();
    }

    public ObjectProperty<Transaction.TransactionType> transactionTypeProperty() {
        return transactionType;
    }

    public void setTransactionType(Transaction.TransactionType transactionType) {
        this.transactionType.set(transactionType);
    }

    public Transaction.PaymentStatus getPaymentStatus() {
        return paymentStatus.get();
    }

    public ObjectProperty<Transaction.PaymentStatus> paymentStatusProperty() {
        return paymentStatus;
    }

    public void setPaymentStatus(Transaction.PaymentStatus paymentStatus) {
        this.paymentStatus.set(paymentStatus);
    }

    public List<TransactionDetailDTO> getTransactionDetails() {
        return transactionDetails;
    }

    public void setTransactionDetails(List<TransactionDetailDTO> transactionDetails) {
        this.transactionDetails = transactionDetails;
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

    public DeliveryStatusDTO getDeliveryStatus() {
        return deliveryStatus.get();
    }

    public ObjectProperty<DeliveryStatusDTO> deliveryStatusProperty() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(DeliveryStatusDTO deliveryStatus) {
        this.deliveryStatus.set(deliveryStatus);
    }

    public List<PaymentDTO> getPayments() {
        return payments;
    }

    public void setPayments(List<PaymentDTO> payments) {
        this.payments = payments;
    }

    public boolean isIsChecked() {
        return isChecked.get();
    }

    public BooleanProperty isCheckedProperty() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked.set(isChecked);
    }

    public int getRefId() {
        return refId.get();
    }

    public IntegerProperty refIdProperty() {
        return refId;
    }

    public void setRefId(int refId) {
        this.refId.set(refId);
    }

    public DateTime getPaymentDueDate() {
        return paymentDueDate.get();
    }

    public ObjectProperty<DateTime> paymentDueDateProperty() {
        return paymentDueDate;
    }

    public void setPaymentDueDate(DateTime paymentDueDate) {
        this.paymentDueDate.set(paymentDueDate);
    }

    public DateTime getDeliveryDueDate() {
        return deliveryDueDate.get();
    }

    public ObjectProperty<DateTime> deliveryDueDateProperty() {
        return deliveryDueDate;
    }

    public void setDeliveryDueDate(DateTime deliveryDueDate) {
        this.deliveryDueDate.set(deliveryDueDate);
    }
}
