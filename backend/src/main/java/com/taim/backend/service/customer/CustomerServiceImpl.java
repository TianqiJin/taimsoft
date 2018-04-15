package com.taim.backend.service.customer;


import com.taim.backend.dao.IDao;
import com.taim.backend.dao.customer.CustomerDaoImpl;
import com.taim.model.Customer;
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
    private CustomerDaoImpl dao;

    @Override
    public List<Customer> getAllCustomers() {
        return dao.getAll();
    }

    @Override
    public Customer saveCustomer(Customer customer) {

       return dao.save(customer);
    }

    @Override
    public Customer getCustomerByName(String name) {
        return dao.findByName(name);
    }

    @Override
    public Customer getCustomerById(Long id) {
        return dao.findByID(id);
    }

    @Override
    public void deleteCustomer(Customer customer) {
        dao.deleteObject(customer);
    }

    @Override
    public Customer updateCustomer(Customer customer) {
       return dao.updateObject(customer);
    }

    @Override
    public Customer saveOrUpdateCustomer(Customer customer) {
        dao.saveOrUpdateObject(customer);
        dao.flush();
        dao.refresh(customer);

        return customer;
    }
}
