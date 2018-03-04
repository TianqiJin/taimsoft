package com.taim.backend.dao.payment;

import com.taim.backend.dao.AbstractDao;
import com.taim.backend.dao.IDao;
import com.taim.model.Payment;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.yaml.snakeyaml.events.Event;

import java.util.List;

public class PaymentDaoImpl extends AbstractDao implements IDao<Payment> {
    @Override
    public Payment save(Payment object) {
        persist(object);
        return object;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Payment> getAll() {
        Criteria criteria = getSession().createCriteria(Payment.class);
        criteria.add(Restrictions.eq("deleted",false));
        return (List<Payment>) criteria.list();
    }

    @Override
    public Payment findByID(Integer id) {
        Criteria criteria = getSession().createCriteria(Payment.class);
        criteria.add(Restrictions.eq("id", id));
        criteria.add(Restrictions.eq("deleted", false));
        return (Payment) criteria.uniqueResult();
    }

    @Override
    public Payment updateObject(Payment object) {
        update(object);
        return object;
    }

    @Override
    public Payment saveOrUpdateObject(Payment object) {
        saveOrUpdate(object);
        return object;
    }

    @Override
    public Payment findByName(String name) {
        return null;
    }

    @Override
    public void deleteObject(Payment object) {
        delete(object);
    }
}
