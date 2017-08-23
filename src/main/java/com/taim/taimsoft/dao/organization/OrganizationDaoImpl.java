package com.taim.taimsoft.dao.organization;


import com.taim.taimsoft.dao.AbstractDao;
import com.taim.taimsoft.dao.IDao;
import com.taim.taimsoft.model.Organization;
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
    public void save(Organization organization) {
        persist(organization);
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
    public void updateObject(Organization organization) {
        update(organization);
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
