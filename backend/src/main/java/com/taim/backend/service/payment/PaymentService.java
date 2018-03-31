package com.taim.backend.service.payment;

import com.taim.backend.dao.payment.PaymentDaoImpl;
import com.taim.model.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class PaymentService implements IPaymentService {

    @Autowired
    private PaymentDaoImpl paymentDao;

    @Override
    public List<Payment> getAllPayments() {
        return paymentDao.getAll();
    }

    @Override
    public Payment savePayment(Payment payment) {
        return paymentDao.save(payment);
    }

    @Override
    public Payment getPaymentById(Integer id) {
        return paymentDao.findByID(id);
    }

    @Override
    public void deletePayment(Payment payment) {
        paymentDao.deleteObject(payment);
    }

    @Override
    public Payment updatePayment(Payment payment) {
        return paymentDao.updateObject(payment);
    }

    @Override
    public Payment saveOrUpdatePayment(Payment payment) {
        return paymentDao.saveOrUpdateObject(payment);
    }
}
