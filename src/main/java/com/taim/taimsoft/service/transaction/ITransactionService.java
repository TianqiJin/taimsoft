package com.taim.taimsoft.service.transaction;

import com.taim.taimsoft.model.Transaction;

import java.util.List;

/**
 * Created by Tjin on 8/19/2017.
 */
public interface ITransactionService {
    List<Transaction> getAllTransactions();
    Transaction saveTransaction(Transaction transaction);
    Transaction getTransactionById(Integer id);
    void deleteTransaction(Transaction transaction);
    Transaction updateTransaction(Transaction transaction);
}
