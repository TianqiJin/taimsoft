package com.taimsoft.dbconnection.dao.organization;

import com.taimsoft.dbconnection.dao.AbstractDao;
import com.taimsoft.dbconnection.model.Organization;
import com.taimsoft.dbconnection.model.Staff;
import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by tjin on 2017-07-23.
 */
@Repository("organizationDao")
public class OrganizationDaoImpl extends AbstractDao implements IOrganizationDao{
    @Override
    public void saveOrganization(Organization organization) {
        persist(organization);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Organization> getAllOrganizations() {
        Criteria criteria = getSession().createCriteria(Organization.class);
        return (List<Organization>) criteria.list();
    }

    @Override
    public Staff findByID(Integer organizationID) {
        return null;
    }

    @Override
    public void updateOrganization(Organization organization) {
        update(organization);
    }
}
