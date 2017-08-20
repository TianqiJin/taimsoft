package com.taimsoft.dbconnection.dao.organization;

import com.taimsoft.dbconnection.dao.AbstractDao;
import com.taimsoft.dbconnection.dao.IDao;
import com.taimsoft.model.Organization;
import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by tjin on 2017-07-23.
 */
@Repository("organizationDao")
public class OrganizationDaoImpl extends AbstractDao implements IDao<Organization>{
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
}
