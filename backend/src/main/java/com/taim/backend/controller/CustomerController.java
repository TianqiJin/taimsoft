package com.taim.backend.controller;

import com.taim.model.Customer;
import com.taim.backend.service.customer.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by dragonliu on 2017/8/22.
 */
@Controller
@RequestMapping(value = "/customer")
public class CustomerController {
    @Autowired
    private ICustomerService service;

    @RequestMapping(value = "/getAll",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<Customer>> getAllCustomers() {
        List<Customer> list = service.getAllCustomers();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @RequestMapping(value = "/getByName",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Customer> getCustomerByName(@RequestParam String name) {
        Customer customer = service.getCustomerByName(name);
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    @RequestMapping(value = "/getById",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Customer> getCustomerById(@RequestParam Long id) {
        Customer customer = service.getCustomerById(id);
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    @RequestMapping(value = "/add",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Customer> create(@RequestBody Customer customer) {
        Customer customer1 = service.saveCustomer(customer);
        return new ResponseEntity<>(customer1, HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/update",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Customer> update(@RequestBody Customer customer) {
        Customer customer1 = service.updateCustomer(customer);
        return new ResponseEntity<>(customer1, HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/saveOrUpdate",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Customer> saveOrUpdate(@RequestBody Customer customer) {
        Customer customer1 = service.saveOrUpdateCustomer(customer);
        return new ResponseEntity<>(customer1, HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/deleteObject")
    @ResponseBody
    public ResponseEntity<String> deletCustomerByName(@RequestParam String name) {
        Customer customer = service.getCustomerByName(name);
        if (customer !=null){
            service.deleteCustomer(customer);
            return new ResponseEntity<>("Deleted!", HttpStatus.OK);

        }else {
            return new ResponseEntity<>("No such customer found!", HttpStatus.OK);
        }
    }


}
