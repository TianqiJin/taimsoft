package com.taim.backend.controller;

import com.taim.model.*;
import com.taim.backend.service.transaction.ITransactionService;
import com.taim.model.basemodels.UserBaseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by dragonliu on 2017/8/23.
 */

@Controller
@RequestMapping(value = "/transaction")
public class TransactionController {

    @Autowired
    private ITransactionService service;

    @RequestMapping(value = "/getAll",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        List<Transaction> list = service.getAllTransactions();
        return new ResponseEntity<List<Transaction>>(list, HttpStatus.OK);
    }


    @RequestMapping(value = "/getById",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Transaction> getTransactionById(@RequestParam Integer id) {
        Transaction transaction = service.getTransactionById(id);
        return new ResponseEntity<Transaction>(transaction, HttpStatus.OK);
    }

    @RequestMapping(value = "/getByProductId",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<Transaction>> getTransactionByProductId(@RequestParam Integer id) {
        List<Transaction> list = service.getAllTransactions();
        List<Transaction> realList = list.stream().filter(t -> t.getTransactionDetails().stream()
                .anyMatch(td -> td.getProduct().getId() == id)).collect(Collectors.toList());
        return new ResponseEntity<List<Transaction>>(realList, HttpStatus.OK);
    }

    @RequestMapping(value = "/getByCustomerId",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<Transaction>> getTransactionByCustomerId(@RequestParam Integer id) {
        List<Transaction> list = service.getAllTransactionsByCustomerId(id);
        return new ResponseEntity<List<Transaction>>(list, HttpStatus.OK);
    }

    @RequestMapping(value = "/getByVendorId",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<Transaction>> getTransactionByVendorId(@RequestParam Integer id) {
        List<Transaction> list = service.getAllTransactionsByVendorId(id);
        return new ResponseEntity<List<Transaction>>(list, HttpStatus.OK);
    }

    @RequestMapping(value = "/getByStaffId",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<Transaction>> getTransactionByStaffId(@RequestParam Integer id) {
        List<Transaction> list = service.getAllTransactionsByStaffId(id);
        return new ResponseEntity<List<Transaction>>(list, HttpStatus.OK);
    }


    @RequestMapping(value = "/add",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Transaction> create(@RequestBody Transaction transaction) {
        Transaction transaction1 = service.saveTransaction(transaction);
        return new ResponseEntity<Transaction>(transaction1, HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/update",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Transaction> update(@RequestBody Transaction transaction) {
        Transaction transaction1 = service.updateTransaction(transaction);
        return new ResponseEntity<Transaction>(transaction1, HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/saveOrUpdateAll",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Transaction> updateAll(@RequestBody Transaction transaction) {
        Transaction transaction1 = service.saveOrUpdateAll(transaction);
        return new ResponseEntity<Transaction>(transaction1, HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/deleteObject")
    @ResponseBody
    public ResponseEntity<String> deleteTransactionById(@RequestParam Integer id) {
        Transaction transaction = service.getTransactionById(id);
        if (transaction !=null){
            service.deleteTransaction(transaction);
            return new ResponseEntity<>("Deleted!", HttpStatus.OK);

        }else {
            return new ResponseEntity<>("No such transaction found!", HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/filter",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<Transaction>> getFilteredTransactions(){

    }
}
