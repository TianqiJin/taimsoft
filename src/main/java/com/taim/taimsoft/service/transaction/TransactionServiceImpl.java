package com.taim.taimsoft.service.transaction;


import com.taim.taimsoft.dao.IDao;
import com.taim.taimsoft.model.Transaction;
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
    public void saveTransaction(Transaction transaction) {
        dao.save(transaction);
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
    public void updateTransaction(Transaction transaction) {
        dao.updateObject(transaction);
    }
}
