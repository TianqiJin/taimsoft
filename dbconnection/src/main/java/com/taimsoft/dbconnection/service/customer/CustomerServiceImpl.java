package com.taimsoft.dbconnection.service.customer;

import com.taimsoft.dbconnection.dao.IDao;
import com.taimsoft.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Tjin on 8/19/2017.
 */
public class CustomerServiceImpl implements ICustomerService{
    @Autowired
    private IDao<Customer> dao;

}
