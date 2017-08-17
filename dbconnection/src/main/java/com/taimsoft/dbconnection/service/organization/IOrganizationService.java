package com.taimsoft.dbconnection.service.organization;


import com.taimsoft.model.Organization;

import java.util.List;

/**
 * Created by tjin on 2017-07-23.
 */
public interface IOrganizationService {
    List<Organization> getAllOrganizations();
    void saveOrganization(Organization organization);
}
