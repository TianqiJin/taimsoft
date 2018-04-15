package com.taim.backend.service.transaction;


import com.taim.backend.dao.customer.CustomerDaoImpl;
import com.taim.backend.dao.product.ProductDaoImpl;
import com.taim.backend.dao.transaction.TransactionDaoImpl;
import com.taim.backend.dao.vendor.VendorDaoImpl;
import com.taim.model.Transaction;
import com.taim.model.search.TransactionSearch;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tjin on 8/19/2017.
 */

@Service("transactionService")
@Transactional
public class TransactionServiceImpl implements ITransactionService{

    @Autowired
    private CustomerDaoImpl customerDao;
    @Autowired
    private ProductDaoImpl productDao;
    @Autowired
    private TransactionDaoImpl transactionDao;
    @Autowired
    private VendorDaoImpl vendorDao;
    @Override
    public List<Transaction> getAllTransactions() {
        return transactionDao.getAll();
    }

    @Override
    public Transaction saveTransaction(Transaction transaction) {
        return transactionDao.save(transaction);
    }

    @Override
    public Transaction getTransactionById(Long id) {
       return transactionDao.findByID(id);
    }

    @Override
    public void deleteTransaction(Transaction transaction) {
        transactionDao.deleteObject(transaction);
    }

    @Override
    public Transaction updateTransaction(Transaction transaction) {

       return transactionDao.updateObject(transaction);
    }

    @Override
    public List<Transaction> getAllTransactionsByCustomerId(Long id) {
        return transactionDao.getByCustomerId(id);
    }

    @Override
    public List<Transaction> getAllTransactionsByVendorId(Long id) {
        return transactionDao.getByVendorId(id);
    }

    @Override
    public List<Transaction> getAllTransactionsByStaffId(Long id) {
        return transactionDao.getByStaffId(id);
    }
    @Override
    public Transaction saveOrUpdateTransaction(Transaction transaction) {
        transactionDao.saveOrUpdateObject(transaction);
        transactionDao.flush();
        transactionDao.refresh(transaction);
        return transaction;
    }

    @Override
    public List<Transaction> getFilteredTransactions(TransactionSearch transactionSearch) {
        return transactionDao.findByFilter(transactionSearch);
    }

    @Override
    public Transaction saveOrUpdateAll(Transaction transaction) {
        if (transaction.getCustomer()!=null) {
            customerDao.updateObject(transaction.getCustomer());
        }
        if (transaction.getVendor()!=null){
            vendorDao.updateObject(transaction.getVendor());
        }
        transaction.getTransactionDetails().forEach(d->productDao.updateObject(d.getProduct()));
        transactionDao.saveOrUpdateObject(transaction);
        transactionDao.flush();
        transactionDao.refresh(transaction);
        return transaction;
    }
}



