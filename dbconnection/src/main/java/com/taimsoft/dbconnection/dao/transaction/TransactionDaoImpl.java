package com.taimsoft.dbconnection.dao.transaction;

import com.taimsoft.dbconnection.dao.AbstractDao;
import com.taimsoft.dbconnection.dao.IDao;
import com.taimsoft.model.Transaction;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * Created by Tjin on 8/19/2017.
 */
public class TransactionDaoImpl extends AbstractDao implements IDao<Transaction>{

    @Override
    public void save(Transaction object) {
        persist(object);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Transaction> getAll() {
        Criteria criteria = getSession().createCriteria(Transaction.class);
        return (List<Transaction>) criteria.list();
    }

    @Override
    public Transaction findByID(Integer id) {
        Criteria criteria = getSession().createCriteria(Transaction.class);
        criteria.add(Restrictions.eq("id", id));
        return (Transaction) criteria.uniqueResult();
    }

    @Override
    public void updateObject(Transaction object) {
        update(object);
    }
}
