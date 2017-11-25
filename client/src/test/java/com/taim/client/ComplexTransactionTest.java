package com.taim.client;

import com.taim.dto.*;
import com.taim.model.Customer;
import com.taim.model.Product;
import com.taim.model.Transaction;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by jiawei.liu on 11/24/17.
 */
public class ComplexTransactionTest {
    private static TransactionClient client = new TransactionClient();
    private static CustomerClient customerClient = new CustomerClient();
    private static ProductClient productClient = new ProductClient();
    private static VendorClient vendorClient = new VendorClient();
    private static StaffClient staffClient = new StaffClient();
    private static TransactionDTO transaction;
    private static CustomerDTO customer;
    private static VendorDTO vendor;
    private static ProductDTO product;
    private static StaffDTO staff;


    @Test
    public void productOnlyTest(){
        product = productClient.getById(4);
        customer = customerClient.getByName("Customer1");
        staff = staffClient.getByName("admin");

        DateTime d1Created = DateTime.now();
        DateTime d1Modified = DateTime.now();


        TransactionDetailDTO transactionDetail = new TransactionDetailDTO();

        transactionDetail.setDateCreated(d1Created);
        //transactionDetail.setDateModified(d1Modified);
        transactionDetail.setDiscount(0);
        transactionDetail.setQuantity(10);
        transactionDetail.setSaleAmount(50);
        transactionDetail.setProduct(product);
        List<TransactionDetailDTO> list = new ArrayList<>();
        list.add(transactionDetail);

        transaction = new TransactionDTO();
        transaction.setDateCreated(d1Created);
        //transaction.setDateModified(d1Modified);
        transaction.setTransactionDetails(list);
        transaction.setSaleAmount(65);
        transaction.setGst(7);
        transaction.setPst(8);
        transaction.setTransactionType(Transaction.TransactionType.QUOTATION);
        transaction.setCustomer(customer);
        transaction.setStaff(staff);
        transaction.setNote("unitTest!");

        product.setVirtualTotalNum(product.getVirtualTotalNum()-10);
        product.setDateModified(d1Modified);
        client.saveOrUpdateAll(transaction);

    }


    @Test
    public void customerTest(){
        product = productClient.getById(4);
        customer = customerClient.getByName("Customer1");
        customer.setEmail("eee@gmail.com");
        DateTime d1Created = DateTime.now();
        DateTime d1Modified = DateTime.now();
        TransactionDetailDTO transactionDetail = new TransactionDetailDTO();

        transactionDetail.setDateCreated(d1Created);
        transactionDetail.setDateModified(d1Modified);
        transactionDetail.setDiscount(0);
        transactionDetail.setQuantity(10);
        transactionDetail.setSaleAmount(50);
        transactionDetail.setProduct(product);
        List<TransactionDetailDTO> list = new ArrayList<>();
        list.add(transactionDetail);

        transaction = new TransactionDTO();
        transaction.setDateCreated(d1Created);
        transaction.setDateModified(d1Modified);
        transaction.setTransactionDetails(list);
        transaction.setSaleAmount(65);
        transaction.setGst(7);
        transaction.setPst(8);
        transaction.setCustomer(customer);
        transaction.setNote("unitTest3");

        product.setVirtualTotalNum(product.getVirtualTotalNum()-10);

        client.saveOrUpdateAll(transaction);

    }


}
