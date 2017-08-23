package com.taim.taimsoft.service.customer;

import com.taim.taimsoft.model.Customer;

import java.util.List;

/**
 * Created by Tjin on 8/19/2017.
 */
public interface ICustomerService {
    List<Customer> getAllCustomers();
    void saveCustomer(Customer customer);
    Customer getCustomerByName(String name);
    void deleteCustomer(Customer customer);
    void updateCustomer(Customer customer);
}
