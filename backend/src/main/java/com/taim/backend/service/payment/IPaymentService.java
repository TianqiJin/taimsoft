package com.taim.backend.service.payment;

import com.taim.model.Payment;

import java.util.List;

public interface IPaymentService {
    List<Payment> getAllPayments();
    Payment savePayment(Payment payment);
    Payment getPaymentById(Integer id);
    void deletePayment(Payment payment);
    Payment updatePayment(Payment payment);
    Payment saveOrUpdatePayment(Payment payment);
}
