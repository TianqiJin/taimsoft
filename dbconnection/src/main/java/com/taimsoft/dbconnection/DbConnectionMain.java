package com.taimsoft.dbconnection;

import com.taimsoft.dbconnection.configuration.AppConfig;
import com.taimsoft.dbconnection.service.organization.IOrganizationService;
import com.taimsoft.dbconnection.service.staff.IStaffService;

import com.taimsoft.model.Organization;
import org.joda.time.DateTime;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

/**
 * Created by Tjin on 7/15/2017.
 */
public class DbConnectionMain {
    public static void main(String[] args){
        AbstractApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        IStaffService staffService = (IStaffService) context.getBean("staffService");
        IOrganizationService organizationService = (IOrganizationService)context.getBean("organizationService");

//        Organization organization = new Organization();
//        organization.setCity("Calgary");
//        organization.setCountry("Canada");
//        organization.setPostalCode("T3P0H1");
//        organization.setStreet("Evanston Drive NW");
//        organization.setOrgName("Test Org");
//        organization.setStreetNum("457");
//        organization.setDateCreated(DateTime.now());
//        organization.setDateModified(DateTime.now());
//
//        organizationService.saveOrganization(organization);

//        Staff staff = staffService.getAllStaffs().get(0);
//        System.out.println(staff.getOrganization().getOrgName());
//        Organization organization = organizationService.getAllOrganizations().get(0);
//        System.out.println(staff.getFullname());
//        System.out.println(organization.getOrgName());
//        staff.setOrganization(organization);
//
//        staffService.updateStaff(staff);

        context.close();
    }
}
