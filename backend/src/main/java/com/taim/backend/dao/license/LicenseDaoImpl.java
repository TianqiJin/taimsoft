package com.taim.backend.dao.license;

import com.taim.backend.dao.AbstractDao;
import com.taim.backend.dao.IDao;
import com.taim.model.License;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("licenseDao")
public class LicenseDaoImpl extends AbstractDao implements IDao<License> {
    @Override
    public License save(License object) {
        persist(object);
        return object;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<License> getAll() {
        Criteria criteria = getSession().createCriteria(License.class);
        return (List<License>) criteria.list();
    }

    @Override
    public License findByID(Integer id) {
        Criteria criteria = getSession().createCriteria(License.class);
        criteria.add(Restrictions.eq("id", id));
        return (License) criteria.uniqueResult();
    }

    @Override
    public License updateObject(License object) {
        update(object);
        return object;
    }

    @Override
    public License saveOrUpdateObject(License object) {
        saveOrUpdate(object);
        return object;
    }

    @Override
    public License findByName(String name) {
        return null;
    }

    @Override
    public void deleteObject(License object) {
        delete(object);
    }
}
