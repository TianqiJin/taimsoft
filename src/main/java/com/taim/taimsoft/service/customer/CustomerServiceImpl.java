package com.taim.taimsoft.service.customer;


import com.taim.taimsoft.dao.IDao;
import com.taim.taimsoft.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by Tjin on 8/19/2017.
 */
@Service("customerService")
@Transactional
public class CustomerServiceImpl implements ICustomerService{
    @Autowired
    private IDao<Customer> dao;

    @Override
    public List<Customer> getAllCustomers() {
        return dao.getAll();
    }

    @Override
    public void saveCustomer(Customer customer) {
        dao.save(customer);
    }

    @Override
    public Customer getCustomerByName(String name) {
        return dao.findByName(name);
    }

    @Override
    public void deleteCustomer(Customer customer) {
        dao.deleteObject(customer);
    }

    @Override
    public void updateCustomer(Customer customer) {
        dao.updateObject(customer);
    }
}
