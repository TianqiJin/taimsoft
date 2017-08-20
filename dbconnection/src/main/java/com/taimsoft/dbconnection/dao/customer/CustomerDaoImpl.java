package com.taimsoft.dbconnection.dao.customer;

import com.taimsoft.dbconnection.dao.AbstractDao;
import com.taimsoft.dbconnection.dao.IDao;
import com.taimsoft.model.Customer;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * Created by Tjin on 8/19/2017.
 */
public class CustomerDaoImpl extends AbstractDao implements IDao<Customer> {

    @Override
    public void save(Customer object) {
        persist(object);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Customer> getAll() {
        Criteria criteria = getSession().createCriteria(Customer.class);
        return (List<Customer>) criteria.list();
    }

    @Override
    public Customer findByID(Integer id) {
        Criteria criteria = getSession().createCriteria(Customer.class);
        criteria.add(Restrictions.eq("id", id));
        return (Customer) criteria.uniqueResult();
    }

    @Override
    public void updateObject(Customer object) {
        update(object);
    }
}
