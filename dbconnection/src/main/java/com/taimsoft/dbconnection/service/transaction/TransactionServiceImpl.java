package com.taimsoft.dbconnection.service.transaction;

import com.taimsoft.dbconnection.dao.IDao;
import com.taimsoft.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Tjin on 8/19/2017.
 */
public class TransactionServiceImpl implements ITransactionService{
    @Autowired
    private IDao<Transaction> dao;
}
