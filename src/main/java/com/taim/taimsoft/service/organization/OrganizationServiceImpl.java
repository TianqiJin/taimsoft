package com.taim.taimsoft.service.organization;


import com.taim.taimsoft.dao.IDao;
import com.taim.taimsoft.model.Organization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by tjin on 2017-07-23.
 */
@Service("organizationService")
@Transactional
public class OrganizationServiceImpl implements IOrganizationService{
    @Autowired
    private IDao<Organization> organizationDao;

    @Override
    public List<Organization> getAllOrganizations() {
        return organizationDao.getAll();
    }

    @Override
    public Organization saveOrganization(Organization organization) {
        return organizationDao.save(organization);
    }

    @Override
    public Organization getOrganizationByName(String name){
        return organizationDao.findByName(name);
    }

    @Override
    public void deleteOrganization(Organization organization) {
        organizationDao.deleteObject(organization);
    }

    @Override
    public Organization updateOrganization(Organization organization) {
       return organizationDao.updateObject(organization);
    }
}
