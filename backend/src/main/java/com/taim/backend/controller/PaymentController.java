package com.taim.backend.controller;

import com.taim.backend.service.payment.IPaymentService;
import com.taim.model.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/payment")
public class PaymentController {

    @Autowired
    private IPaymentService service;

    @RequestMapping(value = "/getAll",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<Payment>> getAllPayments() {
        List<Payment> list = service.getAllPayments();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @RequestMapping(value = "/getById",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Payment> getPaymentById(@RequestParam Integer id) {
        Payment Payment = service.getPaymentById(id);
        return new ResponseEntity<>(Payment, HttpStatus.OK);
    }

    @RequestMapping(value = "/add",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Payment> create(@RequestBody Payment Payment) {
        Payment Payment1 = service.savePayment(Payment);
        return new ResponseEntity<>(Payment1, HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/update",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Payment> update(@RequestBody Payment Payment) {
        Payment Payment1 = service.updatePayment(Payment);
        return new ResponseEntity<>(Payment1, HttpStatus.ACCEPTED);
    }


    @RequestMapping(value = "/deleteObject")
    @ResponseBody
    public ResponseEntity<String> deletePaymentById(@RequestParam Integer id) {
        Payment Payment = service.getPaymentById(id);
        if (Payment !=null){
            service.deletePayment(Payment);
            return new ResponseEntity<>("Deleted!", HttpStatus.OK);

        }else {
            return new ResponseEntity<>("No such Payment found!", HttpStatus.OK);
        }
    }
}
