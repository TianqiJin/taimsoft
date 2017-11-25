package com.taimsoft.desktopui.util;

import com.taim.client.*;
import com.taim.model.Property;

/**
 * Created by Tjin on 9/9/2017.
 */
public class RestClientFactory {
    private static TransactionClient transactionClient;
    private static CustomerClient customerClient;
    private static VendorClient vendorClient;
    private static ProductClient productClient;
    private static StaffClient staffClient;
    private static PropertyClient propertyClient;
    private static OrganizationClient organizationClient;
    private static LicenseClient licenseClient;

    public static TransactionClient getTransactionClient(){
        if(transactionClient == null){
            synchronized(RestClientFactory.class){
                if(transactionClient == null){
                    transactionClient = new TransactionClient();
                }
            }
        }
        return transactionClient;
    }

    public static CustomerClient getCustomerClient(){
        if(customerClient == null){
            synchronized (RestClientFactory.class){
                if(customerClient == null){
                    customerClient = new CustomerClient();
                }
            }
        }
        return customerClient;
    }

    public static VendorClient getVendorClient(){
        if(vendorClient == null){
            synchronized (RestClientFactory.class){
                if(vendorClient == null){
                    vendorClient = new VendorClient();
                }
            }
        }
        return vendorClient;
    }

    public static ProductClient getProductClient(){
        if(productClient == null){
            synchronized (RestClientFactory.class){
                if(productClient == null){
                    productClient = new ProductClient();
                }
            }
        }
        return productClient;
    }

    public static StaffClient getStaffClient(){
        if(staffClient == null){
            synchronized (RestClientFactory.class){
                if(staffClient == null){
                    staffClient = new StaffClient();
                }
            }
        }
        return staffClient;
    }

    public static PropertyClient getPropertyClient(){
        if(propertyClient == null){
            synchronized (RestClientFactory.class){
                if(propertyClient == null){
                    propertyClient = new PropertyClient();
                }
            }
        }
        return propertyClient;
    }

    public static LicenseClient getLicenseClient(){
        if(licenseClient == null){
            synchronized (RestClientFactory.class){
                if(licenseClient == null){
                    licenseClient = new LicenseClient();
                }
            }
        }
        return licenseClient;
    }

    public static OrganizationClient getOrganizationClient(){
        if(organizationClient == null){
            synchronized (RestClientFactory.class){
                if(organizationClient == null){
                    organizationClient = new OrganizationClient();
                }
            }
        }
        return organizationClient;
    }

}
