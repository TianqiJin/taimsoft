package com.taim.backend.service.transaction;


import com.taim.backend.dao.IDao;
import com.taim.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by Tjin on 8/19/2017.
 */

@Service("transactionService")
@Transactional
public class TransactionServiceImpl implements ITransactionService{
    @Autowired
    private IDao<Transaction> dao;

    @Override
    public List<Transaction> getAllTransactions() {
        return dao.getAll();
    }

    @Override
    public Transaction saveTransaction(Transaction transaction) {
        return dao.save(transaction);
    }

    @Override
    public Transaction getTransactionById(Integer id) {
       return dao.findByID(id);
    }

    @Override
    public void deleteTransaction(Transaction transaction) {
        dao.deleteObject(transaction);
    }

    @Override
    public Transaction updateTransaction(Transaction transaction) {

       return dao.updateObject(transaction);
    }
}
