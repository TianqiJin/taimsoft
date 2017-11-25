package com.taim.backend.dao.customer;


import com.taim.backend.dao.AbstractDao;
import com.taim.backend.dao.IDao;
import com.taim.model.Customer;
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
        criteria.add(Restrictions.eq("deleted",false));
        return (List<Customer>) criteria.list();
    }

    @Override
    public Customer findByID(Integer id) {
        Criteria criteria = getSession().createCriteria(Customer.class);
        criteria.add(Restrictions.eq("id", id));
        criteria.add(Restrictions.eq("deleted",false));
        return (Customer) criteria.uniqueResult();
    }

    @Override
    public Customer updateObject(Customer object) {
        update(object);
        return object;
    }

    @Override
    public Customer saveOrUpdateObject(Customer object) {
        saveOrUpdate(object);
        return object;
    }

    @Override
    public Customer findByName(String name) {
        Criteria criteria = getSession().createCriteria(Customer.class);
        criteria.add(Restrictions.eq("fullname", name));
        criteria.add(Restrictions.eq("deleted",false));
        return (Customer)criteria.uniqueResult();

    }

    @Override
    public void deleteObject(Customer object) {
        delete(object);
    }
}
