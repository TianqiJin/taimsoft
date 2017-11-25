package com.taim.model;

import com.taim.dto.LicenseDTO;
import com.taim.model.basemodels.BaseModel;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "property")
public class Property extends BaseModel{
    @Column(name = "product_warn_limit")
    private int productWarnLimit;
    @Column(name = "pst_rate", nullable = false)
    private int pstRate;
    @Column(name = "gst_rate", nullable = false)
    private int gstRate;
    @Column(name = "gst_num")
    private String gstNumber;
    @Column(name = "company_name", nullable = false)
    private String companyName;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Fetch(FetchMode.SUBSELECT)
    @JoinColumn(name = "property_id")
    private List<CustomerClass> customerClasses;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "license_id")
    private License license;

    @Entity
    @Table(name = "customer_class")
    public static class CustomerClass extends BaseModel{
        @Column(name = "customer_class_name")
        private String customerClassName;
        @Column(name = "customer_discount")
        private double customerDiscount;

        public CustomerClass(){}

        public String getCustomerClassName() {
            return customerClassName;
        }

        public void setCustomerClassName(String customerClassName) {
            this.customerClassName = customerClassName;
        }

        public double getCustomerDiscount() {
            return customerDiscount;
        }

        public void setCustomerDiscount(double customerDiscount) {
            this.customerDiscount = customerDiscount;
        }
    }

    public int getProductWarnLimit() {
        return productWarnLimit;
    }

    public void setProductWarnLimit(int productWarnLimit) {
        this.productWarnLimit = productWarnLimit;
    }

    public int getPstRate() {
        return pstRate;
    }

    public void setPstRate(int pstRate) {
        this.pstRate = pstRate;
    }

    public int getGstRate() {
        return gstRate;
    }

    public void setGstRate(int gstRate) {
        this.gstRate = gstRate;
    }

    public String getGstNumber() {
        return gstNumber;
    }

    public void setGstNumber(String gstNumber) {
        this.gstNumber = gstNumber;
    }

    public List<CustomerClass> getCustomerClasses() {
        return customerClasses;
    }

    public void setCustomerClasses(List<CustomerClass> customerClasses) {
        this.customerClasses = customerClasses;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public License getLicense() {
        return license;
    }

    public void setLicense(License license) {
        this.license = license;
    }
}
