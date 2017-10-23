package com.taim.client;

import com.taim.dto.PropertyDTO;
import com.taim.model.Property;
import static junit.framework.Assert.*;
import org.joda.time.DateTime;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class PropertyClientTest {
    private PropertyClient client = new PropertyClient();
    private static PropertyDTO property;

    @BeforeClass
    public static void prepareObject(){
        DateTime d1Created = DateTime.now();
        DateTime d1Modified = DateTime.now();
        Set<PropertyDTO.CustomerClassDTO> customerClassDTOS = new HashSet<>();
        PropertyDTO.CustomerClassDTO customerClassDTO1 = new PropertyDTO.CustomerClassDTO();
        customerClassDTO1.setCustomerClassName("Class 1");
        customerClassDTO1.setCustomerDiscount(20);
        PropertyDTO.CustomerClassDTO customerClassDTO2 = new PropertyDTO.CustomerClassDTO();
        customerClassDTO2.setCustomerClassName("Class 2");
        customerClassDTO2.setCustomerDiscount(30);
        customerClassDTO1.setDateCreated(d1Created);
        customerClassDTO1.setDateModified(d1Modified);
        customerClassDTO2.setDateCreated(d1Created);
        customerClassDTO2.setDateModified(d1Modified);
        property = new PropertyDTO();
        property.setDateCreated(d1Created);
        property.setDateModified(d1Modified);
        property.setGstNumber("gst123");
        property.setGstRate(7);
        property.setPstRate(5);
        property.setProductWarnLimit(500);
        customerClassDTOS.add(customerClassDTO1);
        customerClassDTOS.add(customerClassDTO2);
        property.setCustomerClasses(customerClassDTOS);

    }

    @Test
    public void addProductTest()throws Exception{
        PropertyDTO prod = client.add(property);
    }
}
