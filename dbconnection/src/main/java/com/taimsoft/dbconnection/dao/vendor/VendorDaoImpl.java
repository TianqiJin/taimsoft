package com.taimsoft.dbconnection.dao.vendor;

import com.taimsoft.dbconnection.dao.AbstractDao;
import com.taimsoft.dbconnection.dao.IDao;
import com.taimsoft.model.Transaction;
import com.taimsoft.model.Vendor;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * Created by Tjin on 8/19/2017.
 */
public class VendorDaoImpl extends AbstractDao implements IDao<Vendor> {

    @Override
    public void save(Vendor object) {
        persist(object);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Vendor> getAll() {
        Criteria criteria = getSession().createCriteria(Vendor.class);
        return (List<Vendor>) criteria.list();
    }

    @Override
    public Vendor findByID(Integer id) {
        Criteria criteria = getSession().createCriteria(Vendor.class);
        criteria.add(Restrictions.eq("id", id));
        return (Vendor) criteria.uniqueResult();
    }

    @Override
    public void updateObject(Vendor object) {
        update(object);
    }
}
