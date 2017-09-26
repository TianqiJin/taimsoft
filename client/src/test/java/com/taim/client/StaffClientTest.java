package com.taim.client;

import com.taim.dto.StaffDTO;
import com.taim.model.Staff;
import junit.framework.Assert;
import org.joda.time.DateTime;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

/**
 * Created by dragonliu on 2017/8/30.
 */
public class StaffClientTest {
    private StaffClient client = new StaffClient();
    private static StaffDTO staff;

    @BeforeClass
    public static void prepareObject(){
        DateTime d1Created = DateTime.now();
        DateTime d1Modified = DateTime.now();
        staff = new StaffDTO();
        staff.setUserName("whyme");
        staff.setPassword("lol");
        staff.setEmail("lameass@gmail.com");
        staff.setFullname("dummy dumb");
        staff.setPhone("911");
        staff.setDateCreated(d1Created);
        staff.setDateModified(d1Modified);
    }


    @Test
    public void addStaffTest()throws Exception{

        StaffDTO sf = client.addStaff(staff);
        Assert.assertEquals(staff.getUserName(), sf.getUserName());
        Assert.assertEquals(staff.getPassword(), sf.getPassword());
        Assert.assertEquals(staff.getEmail(), sf.getEmail());
        Assert.assertEquals(staff.getFullname(), sf.getFullname());
        Assert.assertEquals(staff.getPhone(), sf.getPhone());
        Assert.assertEquals(staff.getDateCreated().getMillis(), sf.getDateCreated().getMillis());
        Assert.assertEquals(staff.getDateModified().getMillis(), sf.getDateModified().getMillis());
    }

    @Test
    public void getStaffListTest()throws Exception{
        Thread.sleep(2000);
        List<StaffDTO> sfs = client.getStaffList();
        Assert.assertEquals(1, sfs.size());
        StaffDTO sf = sfs.get(0);
        Assert.assertEquals(staff.getUserName(), sf.getUserName());
        Assert.assertEquals(staff.getPassword(), sf.getPassword());
        Assert.assertEquals(staff.getEmail(), sf.getEmail());
        Assert.assertEquals(staff.getFullname(), sf.getFullname());
        Assert.assertEquals(staff.getPhone(), sf.getPhone());
        Assert.assertEquals(staff.getDateCreated().getMillis(), sf.getDateCreated().getMillis());
        Assert.assertEquals(staff.getDateModified().getMillis(), sf.getDateModified().getMillis());
    }

    @Test
    public void getStaffByNameTest()throws Exception{
        StaffDTO sf = client.getStaffByName("dummy dumb");
        Assert.assertEquals(staff.getUserName(), sf.getUserName());
        Assert.assertEquals(staff.getPassword(), sf.getPassword());
        Assert.assertEquals(staff.getEmail(), sf.getEmail());
        Assert.assertEquals(staff.getFullname(), sf.getFullname());
        Assert.assertEquals(staff.getPhone(), sf.getPhone());
        Assert.assertEquals(staff.getDateCreated().getMillis(), sf.getDateCreated().getMillis());
        Assert.assertEquals(staff.getDateModified().getMillis(), sf.getDateModified().getMillis());
    }

    @Test
    public void updateStaffTest()throws Exception{

        StaffDTO sf = client.getStaffByName("dummy dumb");
        sf.setDateModified(DateTime.now());
        StaffDTO s1 = client.updateStaff(sf);

        Assert.assertEquals(sf.getUserName(), s1.getUserName());
        Assert.assertEquals(sf.getPassword(), s1.getPassword());
        Assert.assertEquals(sf.getEmail(), s1.getEmail());
        Assert.assertEquals(sf.getFullname(), s1.getFullname());
        Assert.assertEquals(sf.getPhone(), s1.getPhone());
        Assert.assertEquals(sf.getDateCreated().getMillis(), s1.getDateCreated().getMillis());
        Assert.assertEquals(sf.getDateModified().getMillis(), s1.getDateModified().getMillis());
    }


    @Test
    public void deleteStaffByNameTest()throws Exception{
        String result = client.deleteStaffByName("dummy dumb");
        Assert.assertEquals("Deleted!", result);
        Thread.sleep(2000);
        List<StaffDTO> sfs = client.getStaffList();
        Assert.assertEquals(0, sfs.size());
    }
}
