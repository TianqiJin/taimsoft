package com.taim.backend.dao.property;

import com.taim.backend.dao.AbstractDao;
import com.taim.backend.dao.IDao;
import com.taim.model.Property;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("propertyDao")
public class PropertyDaoImpl extends AbstractDao implements IDao<Property> {
    @Override
    public Property save(Property object) {
        persist(object);
        return object;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Property> getAll() {
        Criteria criteria = getSession().createCriteria(Property.class);
        return (List<Property>) criteria.list();
    }

    @Override
    public Property findByID(Integer id) {
        Criteria criteria = getSession().createCriteria(Property.class);
        criteria.add(Restrictions.eq("id", id));
        return (Property) criteria.uniqueResult();
    }

    @Override
    public Property updateObject(Property object) {
        update(object);
        return object;
    }

    @Override
    public Property findByName(String name) {
        return null;
    }

    @Override
    public void deleteObject(Property object) {
        delete(object);
    }
}
