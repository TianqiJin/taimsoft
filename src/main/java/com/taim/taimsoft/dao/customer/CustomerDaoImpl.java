package com.taim.taimsoft.dao.customer;


import com.taim.taimsoft.dao.AbstractDao;
import com.taim.taimsoft.dao.IDao;
import com.taim.taimsoft.model.Customer;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Tjin on 8/19/2017.
 */
@Repository("customerDao")
public class CustomerDaoImpl extends AbstractDao implements IDao<Customer> {

    @Override
    public Customer save(Customer object) {
        persist(object);
        return object;
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
    public Customer updateObject(Customer object) {
        update(object);
        return object;
    }

    @Override
    public Customer findByName(String name) {
        Criteria criteria = getSession().createCriteria(Customer.class);
        criteria.add(Restrictions.eq("fullname", name));
        return (Customer)criteria.uniqueResult();

    }

    @Override
    public void deleteObject(Customer object) {
        delete(object);
    }
}
