package com.taim.backend.service.customer;

import com.taim.model.Customer;

import java.util.List;

/**
 * Created by Tjin on 8/19/2017.
 */
public interface ICustomerService {
    List<Customer> getAllCustomers();
    Customer saveCustomer(Customer customer);
    Customer getCustomerByName(String name);
    void deleteCustomer(Customer customer);
    Customer updateCustomer(Customer customer);
    Customer saveOrUpdateCustomer(Customer customer);
}
