package com.taim.taimsoft.controller;

import com.taim.taimsoft.model.Customer;
import com.taim.taimsoft.service.customer.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by dragonliu on 2017/8/22.
 */
@Controller
@RequestMapping(value = "/customer")
public class CustomerController {
    @Autowired
    private ICustomerService service;

    @RequestMapping(value = "/getAll")
    @ResponseBody
    public ResponseEntity<List<Customer>> getAllCustomers() {
        List<Customer> list = service.getAllCustomers();
        return new ResponseEntity<List<Customer>>(list, HttpStatus.OK);
    }

    @RequestMapping(value = "/getByName")
    @ResponseBody
    public ResponseEntity<Customer> getCustomerByName(String name) {
        Customer customer = service.getCustomerByName(name);
        return new ResponseEntity<Customer>(customer, HttpStatus.OK);
    }

    @RequestMapping(value = "/add")
    @ResponseBody
    public ResponseEntity<Customer> create(Customer customer) {
        Customer customer1 = null;
        try {
            customer1= service.saveCustomer(customer);
        } catch (Exception ex) {
            return new ResponseEntity<Customer>(customer1, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Customer>(customer1, HttpStatus.OK);
    }

    @RequestMapping(value = "/update")
    @ResponseBody
    public ResponseEntity<Customer> update(Customer customer) {
        Customer customer1 = null;
        try {
            customer1=service.updateCustomer(customer);
        } catch (Exception ex) {
            return new ResponseEntity<Customer>(customer1, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Customer>(customer1, HttpStatus.OK);
    }

    @RequestMapping(value = "/deleteObject")
    @ResponseBody
    public ResponseEntity<String> deletCustomerByName(String name) {
        Customer customer = service.getCustomerByName(name);
        if (customer !=null){
            service.deleteCustomer(customer);
            return new ResponseEntity<String>("Deleted!", HttpStatus.OK);

        }else {
            return new ResponseEntity<String>("No such customer found!", HttpStatus.OK);
        }
    }


}
