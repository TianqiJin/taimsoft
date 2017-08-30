package com.taim.client;

import com.taim.model.Customer;
import junit.framework.Assert;
import org.joda.time.DateTime;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

/**
 * Created by dragonliu on 2017/8/30.
 */
public class CustomerClientTest {
    private CustomerClient client = new CustomerClient();
    private static Customer customer;

    @BeforeClass
    public static void prepareObject(){
        DateTime d1Created = DateTime.now();
        DateTime d1Modified = DateTime.now();
        customer = new Customer();
        customer.setStoreCredit(6.66);
        customer.setEmail("lameass@gmail.com");
        customer.setFullname("dummy dumb");
        customer.setPhone("911");
        customer.setDeleted(false);
        customer.setDateCreated(d1Created);
        customer.setDateModified(d1Modified);
    }


    @Test
    public void addCustomerTest()throws Exception{

        Customer cc = client.addCustomer(customer);
        Assert.assertEquals(customer.getStoreCredit(),cc.getStoreCredit());
        Assert.assertEquals(customer.getEmail(), cc.getEmail());
        Assert.assertEquals(customer.getFullname(), cc.getFullname());
        Assert.assertEquals(customer.getPhone(), cc.getPhone());
        Assert.assertEquals(customer.isDeleted(), cc.isDeleted());
        Assert.assertEquals(customer.getDateCreated().getMillis(), cc.getDateCreated().getMillis());
        Assert.assertEquals(customer.getDateModified().getMillis(), cc.getDateModified().getMillis());
    }

    @Test
    public void getCustomerListTest()throws Exception{
        Thread.sleep(2000);
        List<Customer> ccs = client.getCustomerList();
        Assert.assertEquals(1, ccs.size());
        Customer cc = ccs.get(0);
        Assert.assertEquals(customer.getStoreCredit(),cc.getStoreCredit());
        Assert.assertEquals(customer.getEmail(), cc.getEmail());
        Assert.assertEquals(customer.getFullname(), cc.getFullname());
        Assert.assertEquals(customer.getPhone(), cc.getPhone());
        Assert.assertEquals(customer.isDeleted(), cc.isDeleted());
        Assert.assertEquals(customer.getDateCreated().getMillis(), cc.getDateCreated().getMillis());
        Assert.assertEquals(customer.getDateModified().getMillis(), cc.getDateModified().getMillis());
    }

    @Test
    public void getCustomerByNameTest()throws Exception{
        Customer cc = client.getCustomerByName("dummy dumb");
        Assert.assertEquals(customer.getStoreCredit(),cc.getStoreCredit());
        Assert.assertEquals(customer.getEmail(), cc.getEmail());
        Assert.assertEquals(customer.getFullname(), cc.getFullname());
        Assert.assertEquals(customer.getPhone(), cc.getPhone());
        Assert.assertEquals(customer.isDeleted(), cc.isDeleted());
        Assert.assertEquals(customer.getDateCreated().getMillis(), cc.getDateCreated().getMillis());
        Assert.assertEquals(customer.getDateModified().getMillis(), cc.getDateModified().getMillis());
    }

    @Test
    public void updateCustomerTest()throws Exception{

        Customer cc = client.getCustomerByName("dummy dumb");
        cc.setDeleted(true);
        cc.setDateModified(DateTime.now());
        Customer c1 = client.updateCustomer(cc);

        Assert.assertEquals(cc.getStoreCredit(),c1.getStoreCredit());
        Assert.assertEquals(cc.getEmail(), c1.getEmail());
        Assert.assertEquals(cc.getFullname(), c1.getFullname());
        Assert.assertEquals(cc.getPhone(), c1.getPhone());
        Assert.assertEquals(cc.isDeleted(), c1.isDeleted());
        Assert.assertEquals(cc.getDateCreated().getMillis(), c1.getDateCreated().getMillis());
        Assert.assertEquals(cc.getDateModified().getMillis(), c1.getDateModified().getMillis());
    }


    @Test
    public void deleteCustomerByNameTest()throws Exception{
        String result = client.deleteCustomerByName("dummy dumb");
        Assert.assertEquals("Deleted!", result);
        Thread.sleep(2000);
        List<Customer> ccs = client.getCustomerList();
        Assert.assertEquals(0, ccs.size());
    }

}
