package com.taim.backend.dao.transaction;


import com.taim.backend.dao.AbstractDao;
import com.taim.backend.dao.IDao;
import com.taim.model.Transaction;
import com.taim.model.search.TransactionSearch;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
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
    public Transaction findByID(Long id) {
        Criteria criteria = getSession().createCriteria(Transaction.class);
        criteria.add(Restrictions.eq("id", id));
        criteria.add(Restrictions.eq("deleted",false));
        return (Transaction) criteria.uniqueResult();
    }

    public Transaction findByPresentID(String id){
        Criteria criteria = getSession().createCriteria(Transaction.class);
        criteria.add(Restrictions.eq("present_id", id));
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

    @SuppressWarnings("unchecked")
    public List<Transaction> findByFilter(TransactionSearch transactionSearch){
        Criteria criteria = getSession().createCriteria(Transaction.class);
        if(StringUtils.isNotEmpty(transactionSearch.getTransactionID())){
            criteria.add(Restrictions.eq("present_id", transactionSearch.getTransactionID()));
        }else{
            if(transactionSearch.getTransactionType() != null){
                criteria.add(Restrictions.eq("transaction_type", transactionSearch.getTransactionType().name()));
            }
            if(transactionSearch.getTransactionCategory() != null){
                criteria.add(Restrictions.eq("transaction_category", transactionSearch.getTransactionCategory().name()));
            }
            if(transactionSearch.getPaymentStatus() != null){
                criteria.add(Restrictions.eq("transaction_category", transactionSearch.getTransactionCategory().name()));
            }
            if(transactionSearch.getPaymentStatus() != null){
                criteria.add(Restrictions.eq("payment_status", transactionSearch.getPaymentStatus().name()));
            }
            if(transactionSearch.getDeliveryStatus() != null){
                criteria.add(Restrictions.eq("delivery_status", transactionSearch.getDeliveryStatus().name()));
            }
            if(transactionSearch.getVendorID() != 0){
                criteria.add(Restrictions.eq("vendor_id", transactionSearch.getVendorID()));
            }
            if(transactionSearch.getStaffID() != 0){
                criteria.add(Restrictions.eq("staff_id", transactionSearch.getStaffID()));
            }
            if(transactionSearch.getFromDate() != null){
                criteria.add(Restrictions.ge("date_created", transactionSearch.getFromDate()));
            }
            if(transactionSearch.getToDate() != null){
                criteria.add(Restrictions.le("date_created", transactionSearch.getToDate()));
            }
        }

        criteria.add(Restrictions.eq("is_finalized",transactionSearch.isFinalized()));
        criteria.add(Restrictions.eq("deleted",false));

        return criteria.list();
    }

    @Override
    public void deleteObject(Transaction object) {
        delete(object);
    }

    @SuppressWarnings("unchecked")
    public List<Transaction> getByCustomerId(Long id){
        Criteria criteria = getSession().createCriteria(Transaction.class);
        criteria.add(Restrictions.eq("customer.id",id));
        criteria.add(Restrictions.eq("deleted",false));
        return criteria.list();
    }

    @SuppressWarnings("unchecked")
    public List<Transaction> getByStaffId(Long id){
        Criteria criteria = getSession().createCriteria(Transaction.class);
        criteria.add(Restrictions.eq("deleted",false));
        criteria.add(Restrictions.eq("staff.id",id));
        return criteria.list();
    }

    @SuppressWarnings("unchecked")
    public List<Transaction> getByVendorId(Long id){
        Criteria criteria = getSession().createCriteria(Transaction.class);
        criteria.add(Restrictions.eq("deleted",false));
        criteria.add(Restrictions.eq("vendor.id",id));
        return criteria.list();
    }
}
