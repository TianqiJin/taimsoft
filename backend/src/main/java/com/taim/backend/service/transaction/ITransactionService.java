package com.taim.backend.service.transaction;

import com.taim.model.Product;
import com.taim.model.Transaction;
import com.taim.model.basemodels.UserBaseModel;

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
    List<Transaction> getAllTransactionsByCustomerId(Integer id);
    List<Transaction> getAllTransactionsByVendorId(Integer id);
    List<Transaction> getAllTransactionsByStaffId(Integer id);
    Transaction saveOrUpdateTransaction(Transaction transaction);
    Transaction saveOrUpdateAll(Transaction transaction);
}
