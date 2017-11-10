package com.taim.backend.dao.organization;


import com.taim.backend.dao.AbstractDao;
import com.taim.backend.dao.IDao;
import com.taim.model.Organization;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by tjin on 2017-07-23.
 */
@Repository("organizationDao")
public class OrganizationDaoImpl extends AbstractDao implements IDao<Organization> {
    @Override
    public Organization save(Organization organization) {
        persist(organization);
        return organization;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Organization> getAll() {
        Criteria criteria = getSession().createCriteria(Organization.class);
        return (List<Organization>) criteria.list();
    }

    @Override
    public Organization findByID(Integer organizationID) {
        return null;
    }

    @Override
    public Organization updateObject(Organization organization) {
        update(organization);
        return organization;
    }

    @Override
    public Organization saveOrUpdateObject(Organization object) {
        return null;
    }

    @Override
    public Organization findByName(String name) {
        Criteria criteria = getSession().createCriteria(Organization.class);
        criteria.add(Restrictions.eq("orgName", name));
        return (Organization)criteria.uniqueResult();
    }

    @Override
    public void deleteObject(Organization organization) {
        delete(organization);
    }
}
