package com.taim.backend.dao.vendor;


import com.taim.backend.dao.AbstractDao;
import com.taim.backend.dao.IDao;
import com.taim.model.Vendor;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Tjin on 8/19/2017.
 */

@Repository("vendorDao")
public class VendorDaoImpl extends AbstractDao implements IDao<Vendor> {

    @Override
    public Vendor save(Vendor object) {
        persist(object);
        return object;
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
    public Vendor updateObject(Vendor object) {
        update(object);
        return object;
    }

    @Override
    public Vendor findByName(String name) {
        Criteria criteria = getSession().createCriteria(Vendor.class);
        criteria.add(Restrictions.eq("fullname", name));
        return (Vendor)criteria.uniqueResult();

    }

    @Override
    public void deleteObject(Vendor object) {
        delete(object);
    }
}
