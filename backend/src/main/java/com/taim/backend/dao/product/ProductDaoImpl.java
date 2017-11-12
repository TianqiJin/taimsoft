package com.taim.backend.dao.product;


import com.taim.backend.dao.AbstractDao;
import com.taim.backend.dao.IDao;
import com.taim.model.Product;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Tjin on 8/19/2017.
 */
@Repository("productDao")
public class ProductDaoImpl extends AbstractDao implements IDao<Product> {

    @Override
    public Product save(Product object) {
        persist(object);
        return object;
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
    public Product updateObject(Product object) {
        update(object);
        return object;
    }

    @Override
    public Product saveOrUpdateObject(Product object) {
        saveOrUpdate(object);
        return object;
    }

    @Override
    public Product findByName(String name) {
       return null;
    }

    public Product findByTexture(String texture) {
        Criteria criteria = getSession().createCriteria(Product.class);
        criteria.add(Restrictions.eq("texture", texture));
        return (Product)criteria.uniqueResult();
    }

    @Override
    public void deleteObject(Product object) {
        delete(object);
    }
}
