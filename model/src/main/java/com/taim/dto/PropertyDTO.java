package com.taim.dto;

import com.taim.dto.basedtos.BaseModelDTO;
import javafx.beans.property.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PropertyDTO extends BaseModelDTO {
    private IntegerProperty productWarnLimit;
    private IntegerProperty pstRate;
    private IntegerProperty gstRate;
    private StringProperty gstNumber;
    private StringProperty companyName;
    private List<CustomerClassDTO> customerClasses;
    private ObjectProperty<LicenseDTO> license;
    private StringProperty terms;

    public PropertyDTO(){
        productWarnLimit = new SimpleIntegerProperty();
        pstRate = new SimpleIntegerProperty();
        gstRate = new SimpleIntegerProperty();
        gstNumber = new SimpleStringProperty();
        companyName = new SimpleStringProperty();
        customerClasses = new ArrayList<>();
        license = new SimpleObjectProperty<>();
        terms = new SimpleStringProperty();
    }

    public static class CustomerClassDTO extends BaseModelDTO{
        private StringProperty customerClassName;
        private DoubleProperty customerDiscount;

        public CustomerClassDTO(){
            customerClassName = new SimpleStringProperty();
            customerDiscount = new SimpleDoubleProperty();
        }

        public String getCustomerClassName() {
            return customerClassName.get();
        }

        public StringProperty customerClassNameProperty() {
            return customerClassName;
        }

        public void setCustomerClassName(String customerClassName) {
            this.customerClassName.set(customerClassName);
        }

        public double getCustomerDiscount() {
            return customerDiscount.get();
        }

        public DoubleProperty customerDiscountProperty() {
            return customerDiscount;
        }

        public void setCustomerDiscount(double customerDiscount) {
            this.customerDiscount.set(customerDiscount);
        }
    }

    public int getProductWarnLimit() {
        return productWarnLimit.get();
    }

    public IntegerProperty productWarnLimitProperty() {
        return productWarnLimit;
    }

    public void setProductWarnLimit(int productWarnLimit) {
        this.productWarnLimit.set(productWarnLimit);
    }

    public int getPstRate() {
        return pstRate.get();
    }

    public IntegerProperty pstRateProperty() {
        return pstRate;
    }

    public void setPstRate(int pstRate) {
        this.pstRate.set(pstRate);
    }

    public int getGstRate() {
        return gstRate.get();
    }

    public IntegerProperty gstRateProperty() {
        return gstRate;
    }

    public void setGstRate(int gstRate) {
        this.gstRate.set(gstRate);
    }

    public String getGstNumber() {
        return gstNumber.get();
    }

    public StringProperty gstNumberProperty() {
        return gstNumber;
    }

    public void setGstNumber(String gstNumber) {
        this.gstNumber.set(gstNumber);
    }

    public List<CustomerClassDTO> getCustomerClasses() {
        return customerClasses;
    }

    public void setCustomerClasses(List<CustomerClassDTO> customerClasses) {
        this.customerClasses = customerClasses;
    }

    public String getCompanyName() {
        return companyName.get();
    }

    public StringProperty companyNameProperty() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName.set(companyName);
    }

    public LicenseDTO getLicense() {
        return license.get();
    }

    public ObjectProperty<LicenseDTO> licenseProperty() {
        return license;
    }

    public void setLicense(LicenseDTO license) {
        this.license.set(license);
    }

    public String getTerms() {
        return terms.get();
    }

    public StringProperty termsProperty() {
        return terms;
    }

    public void setTerms(String terms) {
        this.terms.set(terms);
    }
}
