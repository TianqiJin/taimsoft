package com.taimsoft.dbconnection.service.organization;

import com.taimsoft.dbconnection.dao.organization.OrganizationDaoImpl;
import com.taimsoft.dbconnection.model.Organization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by tjin on 2017-07-23.
 */
@Service("organizationService")
public class OrganizationServiceImpl implements IOrganizationService{
    @Autowired
    private OrganizationDaoImpl organizationDao;

    @Override
    public List<Organization> getAllOrganizations() {
        return organizationDao.getAllOrganizations();
    }

    @Override
    public void saveOrganization(Organization organization) {
        organizationDao.saveOrganization(organization);
    }
}
