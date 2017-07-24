package com.taimsoft.dbconnection.dao.organization;

import com.taimsoft.dbconnection.model.Organization;
import com.taimsoft.dbconnection.model.Staff;

import java.util.List;

/**
 * Created by tjin on 2017-07-23.
 */
public interface IOrganizationDao {
    void saveOrganization(Organization organization);

    List<Organization> getAllOrganizations();

    Staff findByID(Integer organizationID);

    void updateOrganization(Organization organization);
}
