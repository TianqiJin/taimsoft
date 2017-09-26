package com.taim.client;

import com.taim.dto.VendorDTO;
import com.taim.model.Vendor;
import junit.framework.Assert;
import org.joda.time.DateTime;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

/**
 * Created by dragonliu on 2017/8/30.
 */
public class VendorClientTest {
    private VendorClient client = new VendorClient();
    private static VendorDTO vendor;

    @BeforeClass
    public static void prepareObject(){
        DateTime d1Created = DateTime.now();
        DateTime d1Modified = DateTime.now();
        vendor = new VendorDTO();
        vendor.setEmail("lameass@gmail.com");
        vendor.setFullname("dummy dumb");
        vendor.setPhone("911");
        vendor.setDateCreated(d1Created);
        vendor.setDateModified(d1Modified);
    }


    @Test
    public void addVendorTest()throws Exception{

        VendorDTO cc = client.addVendor(vendor);
        Assert.assertEquals(vendor.getEmail(), cc.getEmail());
        Assert.assertEquals(vendor.getFullname(), cc.getFullname());
        Assert.assertEquals(vendor.getPhone(), cc.getPhone());
        Assert.assertEquals(vendor.getDateCreated().getMillis(), cc.getDateCreated().getMillis());
        Assert.assertEquals(vendor.getDateModified().getMillis(), cc.getDateModified().getMillis());
    }

    @Test
    public void getVendorListTest()throws Exception{
        Thread.sleep(2000);
        List<VendorDTO> ccs = client.getVendorList();
        Assert.assertEquals(1, ccs.size());
        VendorDTO cc = ccs.get(0);
        Assert.assertEquals(vendor.getEmail(), cc.getEmail());
        Assert.assertEquals(vendor.getFullname(), cc.getFullname());
        Assert.assertEquals(vendor.getPhone(), cc.getPhone());
        Assert.assertEquals(vendor.getDateCreated().getMillis(), cc.getDateCreated().getMillis());
        Assert.assertEquals(vendor.getDateModified().getMillis(), cc.getDateModified().getMillis());
    }

    @Test
    public void getVendorByNameTest()throws Exception{
        VendorDTO cc = client.getVendorByName("dummy dumb");
        Assert.assertEquals(vendor.getEmail(), cc.getEmail());
        Assert.assertEquals(vendor.getFullname(), cc.getFullname());
        Assert.assertEquals(vendor.getPhone(), cc.getPhone());
        Assert.assertEquals(vendor.getDateCreated().getMillis(), cc.getDateCreated().getMillis());
        Assert.assertEquals(vendor.getDateModified().getMillis(), cc.getDateModified().getMillis());
    }

    @Test
    public void updateVendorTest()throws Exception{

        VendorDTO cc = client.getVendorByName("dummy dumb");
        cc.setDateModified(DateTime.now());
        VendorDTO c1 = client.updateVendor(cc);

        Assert.assertEquals(cc.getEmail(), c1.getEmail());
        Assert.assertEquals(cc.getFullname(), c1.getFullname());
        Assert.assertEquals(cc.getPhone(), c1.getPhone());
        Assert.assertEquals(cc.getDateCreated().getMillis(), c1.getDateCreated().getMillis());
        Assert.assertEquals(cc.getDateModified().getMillis(), c1.getDateModified().getMillis());
    }


    @Test
    public void deleteVendorByNameTest()throws Exception{
        String result = client.deleteVendorByName("dummy dumb");
        Assert.assertEquals("Deleted!", result);
        Thread.sleep(2000);
        List<VendorDTO> ccs = client.getVendorList();
        Assert.assertEquals(0, ccs.size());
    }
}