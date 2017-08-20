package com.taimsoft.dbconnection.dao.product;

import com.taimsoft.dbconnection.dao.AbstractDao;
import com.taimsoft.dbconnection.dao.IDao;
import com.taimsoft.model.Product;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * Created by Tjin on 8/19/2017.
 */
public class ProductDaoImpl extends AbstractDao implements IDao<Product> {

    @Override
    public void save(Product object) {
        persist(object);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Product> getAll() {
        Criteria criteria = getSession().createCriteria(Product.class);
        return (List<Product>) criteria.list();
    }

    @Override
    public Product findByID(Integer id) {
        Criteria criteria = getSession().createCriteria(Product.class);
        criteria.add(Restrictions.eq("id", id));
        return (Product) criteria.uniqueResult();
    }

    @Override
    public void updateObject(Product object) {
        update(object);
    }
}
