package com.taimsoft.dbconnection.dao.organization;


import com.taimsoft.model.Organization;

import java.util.List;

/**
 * Created by tjin on 2017-07-23.
 */
public interface IOrganizationDao {
    void saveOrganization(Organization organization);

    List<Organization> getAllOrganizations();

    Organization findByID(Integer organizationID);

    void updateOrganization(Organization organization);
}
