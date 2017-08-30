package com.taim.client;


import com.taim.model.Organization;
import junit.framework.Assert;
import org.joda.time.DateTime;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

/**
 * Created by dragonliu on 2017/8/27.
 */
public class OrganizationClientTest {
    private OrganizationClient client = new OrganizationClient();
    private static Organization organization;

    @BeforeClass
    public static void prepareObject(){
        DateTime d1Created = DateTime.now();
        DateTime d1Modified = DateTime.now();
        organization = new Organization();
        organization.setCity("Toronto");
        organization.setCountry("Canada");
        organization.setPostalCode("M5V4A9");
        organization.setStreet("Iceboat Terrace");
        organization.setOrgName("WTF Org");
        organization.setStreetNum("21");
        organization.setDateCreated(d1Created);
        organization.setDateModified(d1Modified);
    }


    @Test
    public void addOrganizationTest()throws Exception{

        Organization org = client.addOrganization(organization);
        Assert.assertEquals(organization.getCity(), org.getCity());
        Assert.assertEquals(organization.getCountry(), org.getCountry());
        Assert.assertEquals(organization.getPostalCode(), org.getPostalCode());
        Assert.assertEquals(organization.getStreet(), org.getStreet());
        Assert.assertEquals(organization.getOrgName(), org.getOrgName());
        Assert.assertEquals(organization.getStreetNum(), org.getStreetNum());
        Assert.assertEquals(organization.getDateCreated().getMillis(), org.getDateCreated().getMillis());
        Assert.assertEquals(organization.getDateModified().getMillis(), org.getDateModified().getMillis());
    }

    @Test
    public void getOrganizationListTest()throws Exception{
        Thread.sleep(2000);
        List<Organization> orgs = client.getOrganizationList();
        Assert.assertEquals(1, orgs.size());
        Organization org = orgs.get(0);
        Assert.assertEquals(organization.getCity(), org.getCity());
        Assert.assertEquals(organization.getCountry(), org.getCountry());
        Assert.assertEquals(organization.getPostalCode(), org.getPostalCode());
        Assert.assertEquals(organization.getStreet(), org.getStreet());
        Assert.assertEquals(organization.getOrgName(), org.getOrgName());
        Assert.assertEquals(organization.getStreetNum(), org.getStreetNum());
        Assert.assertEquals(organization.getDateCreated().getMillis(),org.getDateCreated().getMillis());
        Assert.assertEquals(organization.getDateModified().getMillis(),org.getDateModified().getMillis());

    }

    @Test
    public void getOrganizationByNameTest()throws Exception{
        Organization org = client.getOrganizationByName("WTF Org");
        Assert.assertEquals(organization.getCity(), org.getCity());
        Assert.assertEquals(organization.getCountry(), org.getCountry());
        Assert.assertEquals(organization.getPostalCode(), org.getPostalCode());
        Assert.assertEquals(organization.getStreet(), org.getStreet());
        Assert.assertEquals(organization.getOrgName(), org.getOrgName());
        Assert.assertEquals(organization.getStreetNum(), org.getStreetNum());
        Assert.assertEquals(organization.getDateCreated().getMillis(),org.getDateCreated().getMillis());
        Assert.assertEquals(organization.getDateModified().getMillis(), org.getDateModified().getMillis());
    }

    @Test
    public void updateOrganizationTest()throws Exception{

        Organization org = client.getOrganizationByName("WTF Org");
        org.setOrgName("Nvm Org");
        org.setDateModified(DateTime.now());
        Organization a1 = client.updateOrganization(org);

        Assert.assertEquals(org.getCity(), a1.getCity());
        Assert.assertEquals(org.getCountry(), a1.getCountry());
        Assert.assertEquals(org.getPostalCode(), a1.getPostalCode());
        Assert.assertEquals(org.getStreet(), a1.getStreet());
        Assert.assertEquals(org.getOrgName(), a1.getOrgName());
        Assert.assertEquals(org.getStreetNum(), a1.getStreetNum());
        Assert.assertEquals(org.getDateCreated().getMillis(), a1.getDateCreated().getMillis());
        Assert.assertEquals(org.getDateModified().getMillis(), a1.getDateModified().getMillis());
    }


    @Test
    public void deleteOrganizationByNameTest()throws Exception{
        String result = client.deleteOrganizationByName("Nvm Org");
        Assert.assertEquals("Deleted!", result);
        Thread.sleep(2000);
        List<Organization> orgs = client.getOrganizationList();
        Assert.assertEquals(0, orgs.size());
    }






    public void dummy(){
        Organization organization = new Organization();
        organization.setCity("Calgary");
        organization.setCountry("Canada");
        organization.setPostalCode("T3P0H1");
        organization.setStreet("Evanston Drive NW");
        organization.setOrgName("Test Org");
        organization.setStreetNum("457");
        organization.setDateCreated(DateTime.now());
        organization.setDateModified(DateTime.now());
    }


}
