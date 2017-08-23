package com.taim.taimsoft.controller;

import com.taim.taimsoft.model.Transaction;
import com.taim.taimsoft.service.transaction.ITransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by dragonliu on 2017/8/23.
 */

@Controller
@RequestMapping(value = "/transaction")
public class TransactionController {

    @Autowired
    private ITransactionService service;

    @RequestMapping(value = "/getAll")
    @ResponseBody
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        List<Transaction> list = service.getAllTransactions();
        return new ResponseEntity<List<Transaction>>(list, HttpStatus.OK);
    }


    @RequestMapping(value = "/getById")
    @ResponseBody
    public ResponseEntity<Transaction> getTransactionById(Integer id) {
        Transaction transaction = service.getTransactionById(id);
        return new ResponseEntity<Transaction>(transaction, HttpStatus.OK);
    }


    @RequestMapping(value = "/add")
    @ResponseBody
    public ResponseEntity<String> create(Transaction transaction) {
        try {
            service.saveTransaction(transaction);
        } catch (Exception ex) {
            return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<String>("Transaction successfully saved!", HttpStatus.OK);
    }

    @RequestMapping(value = "/update")
    @ResponseBody
    public ResponseEntity<String> update(Transaction transaction) {
        try {
            service.updateTransaction(transaction);
        } catch (Exception ex) {
            return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<String>("Transaction successfully updated!", HttpStatus.OK);
    }

    @RequestMapping(value = "/deleteObject")
    @ResponseBody
    public ResponseEntity<String> deleteTransactionById(Integer id) {
        Transaction transaction = service.getTransactionById(id);
        if (transaction !=null){
            service.deleteTransaction(transaction);
            return new ResponseEntity<String>("Deleted!", HttpStatus.OK);

        }else {
            return new ResponseEntity<String>("No such transaction found!", HttpStatus.OK);
        }
    }



}
