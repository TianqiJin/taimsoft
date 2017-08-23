package com.taim.taimsoft.service.organization;



import com.taim.taimsoft.model.Organization;

import java.util.List;

/**
 * Created by tjin on 2017-07-23.
 */
public interface IOrganizationService {
    List<Organization> getAllOrganizations();
    void saveOrganization(Organization organization);
    Organization getOrganizationByName(String name);
    void deleteOrganization(Organization organization);
    void updateOrganization(Organization organization);
}
