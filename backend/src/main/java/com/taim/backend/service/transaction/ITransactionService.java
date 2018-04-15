package com.taim.backend.service.transaction;

import com.taim.model.Transaction;
import com.taim.model.search.TransactionSearch;

import java.util.List;

/**
 * Created by Tjin on 8/19/2017.
 */
public interface ITransactionService {
    List<Transaction> getAllTransactions();
    Transaction saveTransaction(Transaction transaction);
    Transaction getTransactionById(Long id);
    void deleteTransaction(Transaction transaction);
    Transaction updateTransaction(Transaction transaction);
    List<Transaction> getAllTransactionsByCustomerId(Long id);
    List<Transaction> getAllTransactionsByVendorId(Long id);
    List<Transaction> getAllTransactionsByStaffId(Long id);
    Transaction saveOrUpdateTransaction(Transaction transaction);
    List<Transaction> getFilteredTransactions(TransactionSearch transactionSearch);
    Transaction saveOrUpdateAll(Transaction transaction);
}
