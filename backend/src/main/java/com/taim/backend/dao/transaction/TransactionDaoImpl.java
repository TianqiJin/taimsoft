package com.taim.backend.dao.transaction;


import com.taim.backend.controller.model.TransactionSearchQueryParam;
import com.taim.backend.dao.AbstractDao;
import com.taim.backend.dao.IDao;
import com.taim.model.Transaction;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Tjin on 8/19/2017.
 */

@Repository("transactionDao")
public class TransactionDaoImpl extends AbstractDao implements IDao<Transaction> {

    @Override
    public Transaction save(Transaction object) {
        persist(object);
        return object;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Transaction> getAll() {
        Criteria criteria = getSession().createCriteria(Transaction.class);
        criteria.add(Restrictions.eq("deleted",false));
        return (List<Transaction>) criteria.list();
    }

    @Override
    public Transaction findByID(Integer id) {
        Criteria criteria = getSession().createCriteria(Transaction.class);
        criteria.add(Restrictions.eq("id", id));
        criteria.add(Restrictions.eq("deleted",false));
        return (Transaction) criteria.uniqueResult();
    }

    @Override
    public Transaction updateObject(Transaction object) {
        update(object);
        return object;
    }

    @Override
    public Transaction saveOrUpdateObject(Transaction object) {
        saveOrUpdate(object);
        return object;
    }

    @Override
    public Transaction findByName(String name) {
        return null;
    }

    public List<Transaction> findByFilter(TransactionSearchQueryParam transactionSearchQueryParam){
//        Criteria criteria = getSession().createCriteria(Transaction.class);
//        if(transactionSearchQueryParam.getCategory() != null){
//            criteria.add(Restrictions.eq(""))
//        }
//        criteria.add(Restrictions.eq("deleted",false));

        return null;
    }

    @Override
    public void deleteObject(Transaction object) {
        delete(object);
    }

    public List<Transaction> getByCustomerId(Integer id){
        Criteria criteria = getSession().createCriteria(Transaction.class);
        criteria.add(Restrictions.eq("customer.id",id));
        criteria.add(Restrictions.eq("deleted",false));
        return (List<Transaction>) criteria.list();
    }
    public List<Transaction> getByStaffId(Integer id){
        Criteria criteria = getSession().createCriteria(Transaction.class);
        criteria.add(Restrictions.eq("deleted",false));
        criteria.add(Restrictions.eq("staff.id",id));
        return (List<Transaction>) criteria.list();
    }
    public List<Transaction> getByVendorId(Integer id){
        Criteria criteria = getSession().createCriteria(Transaction.class);
        criteria.add(Restrictions.eq("deleted",false));
        criteria.add(Restrictions.eq("vendor.id",id));
        return (List<Transaction>) criteria.list();
    }
}
