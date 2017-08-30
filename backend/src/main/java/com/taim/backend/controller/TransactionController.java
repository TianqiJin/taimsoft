package com.taim.backend.controller;

import com.taim.model.Transaction;
import com.taim.backend.service.transaction.ITransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Transaction> getTransactionById(@RequestParam Integer id) {
        Transaction transaction = service.getTransactionById(id);
        return new ResponseEntity<Transaction>(transaction, HttpStatus.OK);
    }


    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Transaction> create(@RequestBody Transaction transaction) {
        Transaction transaction1=null;
        try {
            transaction1 = service.saveTransaction(transaction);
        } catch (Exception ex) {
            return new ResponseEntity<Transaction>(transaction1, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Transaction>(transaction1, HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Transaction> update(@RequestBody Transaction transaction) {
        Transaction transaction1=null;
        try {
            transaction1=service.updateTransaction(transaction);
        } catch (Exception ex) {
            return new ResponseEntity<Transaction>(transaction1, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Transaction>(transaction1, HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/deleteObject")
    @ResponseBody
    public ResponseEntity<String> deleteTransactionById(@RequestParam Integer id) {
        Transaction transaction = service.getTransactionById(id);
        if (transaction !=null){
            service.deleteTransaction(transaction);
            return new ResponseEntity<String>("Deleted!", HttpStatus.OK);

        }else {
            return new ResponseEntity<String>("No such transaction found!", HttpStatus.OK);
        }
    }



}
