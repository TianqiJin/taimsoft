package com.taimsoft.desktopui.reports;

import com.itextpdf.text.zugferd.checkers.basic.DateFormatCode;
import com.itextpdf.text.zugferd.checkers.basic.DocumentTypeCode;
import com.itextpdf.text.zugferd.profiles.BasicProfileImp;
import com.taim.dto.CustomerDTO;
import com.taim.dto.StaffDTO;
import com.taim.dto.basedtos.UserBaseModelDTO;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by jiawei.liu on 2/4/16.
 */
public class InvoiceData {

    public class AdvancedProfileImp extends BasicProfileImp{
        protected String sellerPhoneNumber;
        protected String buyerPhoneNumber;
        protected String buyerCompanyName;
        protected String sellerCompanyName;
        protected String sellerEmail;
        protected String buyerEmail;

        public String getSellerPhoneNumber() {
            return sellerPhoneNumber;
        }

        public void setSellerPhoneNumber(String sellerPhoneNumber) {
            this.sellerPhoneNumber = sellerPhoneNumber;
        }

        public String getBuyerPhoneNumber() {
            return buyerPhoneNumber;
        }

        public void setBuyerPhoneNumber(String buyerPhoneNumber) {
            this.buyerPhoneNumber = buyerPhoneNumber;
        }

        public String getBuyerCompanyName() {
            return buyerCompanyName;
        }

        public void setBuyerCompanyName(String buyerCompanyName) {
            this.buyerCompanyName = buyerCompanyName;
        }

        public String getSellerCompanyName() {
            return sellerCompanyName;
        }

        public void setSellerCompanyName(String sellerCompanyName) {
            this.sellerCompanyName = sellerCompanyName;
        }

        public String getSellerEmail() {
            return sellerEmail;
        }

        public void setSellerEmail(String sellerEmail) {
            this.sellerEmail = sellerEmail;
        }

        public String getBuyerEmail() {
            return buyerEmail;
        }

        public void setBuyerEmail(String buyerEmail) {
            this.buyerEmail = buyerEmail;
        }
    }

    public InvoiceData() {
    }

    public AdvancedProfileImp createBasicProfileData(Invoice invoice) {
        AdvancedProfileImp profileImp = new AdvancedProfileImp();
        importData(profileImp, invoice);
        importBasicData(profileImp, invoice);
        return profileImp;
    }

    public void importData(AdvancedProfileImp profileImp, Invoice invoice) {
        profileImp.setTest(true);
        profileImp.setId(String.format("I/%05d", invoice.getId()));
        profileImp.setName("INVOICE");
        profileImp.setTypeCode(DocumentTypeCode.COMMERCIAL_INVOICE);
        profileImp.setDate(invoice.getInvoiceDate().toDate(), DateFormatCode.YYYYMMDD);
        UserBaseModelDTO staff = invoice.getStaff();
        profileImp.setSellerName(staff.getFullname());
        profileImp.setSellerLineOne(String.format("%s %s", staff.getOrganization().getStreetNum(), staff.getOrganization().getStreet()));
        profileImp.setSellerPostcode(staff.getOrganization().getPostalCode());
        profileImp.setSellerCityName(staff.getOrganization().getCity());
        profileImp.setSellerCountryID(staff.getOrganization().getCountry());
        profileImp.setSellerPhoneNumber(staff.getPhone());
        profileImp.setSellerEmail(staff.getEmail());
        UserBaseModelDTO customer = invoice.getCustomer();
        profileImp.setBuyerName(customer.getFullname());
        profileImp.setBuyerLineOne(String.format("%s %s", customer.getOrganization().getStreetNum(), customer.getOrganization().getStreet()));
        profileImp.setBuyerCityName(customer.getOrganization().getCity());
        profileImp.setBuyerPostcode(customer.getOrganization().getPostalCode());
        profileImp.setBuyerCountryID(customer.getOrganization().getCountry());
        profileImp.setBuyerPhoneNumber(customer.getPhone());
        profileImp.setBuyerEmail(customer.getEmail());
        profileImp.setPaymentReference(String.format("%09d", invoice.getId()));
        profileImp.setInvoiceCurrencyCode("CAD");
    }

    public void importBasicData(BasicProfileImp profileImp, Invoice invoice) {
        profileImp.addNote(
                new String[]{"This is a test invoice.\nNothing on this invoice is real.\nThis invoice is part of a tutorial."});
        profileImp.addPaymentMeans("", "", "BE 41 7360 0661 9710", "", "", "KREDBEBB", "", "KBC");
        profileImp.addPaymentMeans("", "", "BE 56 0015 4298 7888", "", "", "GEBABEBB", "", "BNP Paribas");
        Map<Double,Double> taxes = new TreeMap<Double, Double>();
        double tax;
    }

    public static double round(double d) {
        d = d * 100;
        long tmp = Math.round(d);
        return (double) tmp / 100;
    }

    public static String format2dec(double d) {
        return String.format("%.2f", d);
    }

    public static String format4dec(double d) {
        return String.format("%.4f", d);
    }

}
