package com.taim.desktopui;

import com.taim.dto.CustomerDTO;
import com.taim.dto.ProductDTO;
import com.taim.dto.TransactionDTO;
import com.taim.dto.TransactionDetailDTO;
import com.taim.model.Customer;
import com.taimsoft.desktopui.util.RestClientFactory;
import org.joda.time.DateTime;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiawei.liu on 10/4/17.
 */
public class dummyTest {

    @Test
    public void addFewProducts()throws Exception{
        ProductDTO product1 = new ProductDTO();
        product1.setSku("p-1");
        product1.setDisplayName("wahaha");
        product1.setHeight(40);
        product1.setWidth(30);
        product1.setLength(100);
        product1.setTexture("lol");
        product1.setTotalNum(300);
        product1.setVirtualTotalNum(300);
        product1.setUnitPrice(10);
        product1.setDateCreated(DateTime.now());
        product1.setDateModified(DateTime.now());


        ProductDTO product2 = new ProductDTO();
        product2.setSku("p-2");
        product2.setDisplayName("dummyProd");
        product2.setHeight(33);
        product2.setWidth(22);
        product2.setLength(80);
        product2.setTexture("zack");
        product2.setTotalNum(800);
        product2.setVirtualTotalNum(800);
        product2.setUnitPrice(5);
        product2.setDateCreated(DateTime.now());
        product2.setDateModified(DateTime.now());

        RestClientFactory.getProductClient().add(product1);
        RestClientFactory.getProductClient().add(product2);

    }



    @Test
    public void getTransactionDetails()throws Exception{
        TransactionDTO transactionDTO = RestClientFactory.getTransactionClient().getById(1);
        System.out.println(transactionDTO.getSaleAmount()+" -> "+transactionDTO.getTransactionDetails().size());
        //transactionDTO.getTransactionDetails().stream().forEach(d-> System.out.println(d.getProduct().getSku()+": "+d.getQuantity()+" -> "+d.getSaleAmount()));

    }

    @Test
    public void addTransactionDTOTest()throws Exception{
        TransactionDetailDTO detail1 = new TransactionDetailDTO();
        detail1.setProduct(RestClientFactory.getProductClient().getById(1));
        detail1.setQuantity(10);
        detail1.setSaleAmount(100);
        detail1.setDateCreated(DateTime.now());
        detail1.setDateModified(DateTime.now());

        TransactionDetailDTO detail2 = new TransactionDetailDTO();
        detail2.setProduct(RestClientFactory.getProductClient().getById(2));
        detail2.setQuantity(10);
        detail2.setSaleAmount(100);
        detail2.setDateCreated(DateTime.now());
        detail2.setDateModified(DateTime.now());

        List<TransactionDetailDTO> detailList = new ArrayList<>();
        detailList.add(detail1);
        detailList.add(detail2);

        TransactionDTO transaction = new TransactionDTO();
        transaction.getTransactionDetails().addAll(detailList);
        transaction.setSaleAmount(Double.valueOf(100.14));
        transaction.setGst(Double.valueOf(12.1));
        transaction.setPst(Double.valueOf(13.2));
        transaction.setNote("WHY");
        transaction.setDateModified(DateTime.now());
        transaction.setDateCreated(DateTime.now());

        RestClientFactory.getTransactionClient().add(transaction);


    }

    @Test
    public void addCustomers()throws Exception{
        CustomerDTO customer1 = new CustomerDTO();
        customer1.setDateCreated(DateTime.now());
        customer1.setDateModified(DateTime.now());
        customer1.setEmail("tm@gmail.com");
        customer1.setFullname("Tim Hortons");
        customer1.setPhone("5698974624");
//        customer1.setCustomerClass(Customer.CustomerClass.CLASSB);

        CustomerDTO customer2 = new CustomerDTO();
        customer2.setDateCreated(DateTime.now());
        customer2.setDateModified(DateTime.now());
        customer2.setEmail("dummyDum@hotmail.com");
        customer2.setFullname("Dummy dum");
        customer2.setPhone("1234698712");
//        customer2.setCustomerClass(Customer.CustomerClass.CLASSC);

        RestClientFactory.getCustomerClient().add(customer1);
        RestClientFactory.getCustomerClient().add(customer2);


    }

    @Test
    public void getCustomerTransactionList(){
        System.out.println(RestClientFactory.getCustomerClient().getByName("Tim Hortons").getTransactionList().size());
    }


    @Test
    public void addCustomerTransactionDTOTest()throws Exception{
        TransactionDetailDTO detail1 = new TransactionDetailDTO();
        detail1.setProduct(RestClientFactory.getProductClient().getById(1));
        detail1.setQuantity(2);
        detail1.setSaleAmount(20);
        detail1.setDateCreated(DateTime.now());
        detail1.setDateModified(DateTime.now());

        List<TransactionDetailDTO> detailList = new ArrayList<>();
        detailList.add(detail1);

        TransactionDTO transaction = new TransactionDTO();
        transaction.getTransactionDetails().addAll(detailList);
        transaction.setSaleAmount(Double.valueOf(20));
        transaction.setGst(Double.valueOf(1.2));
        transaction.setPst(Double.valueOf(1));
        transaction.setNote("WHY");
        transaction.setDateModified(DateTime.now());
        transaction.setDateCreated(DateTime.now());

        transaction.setCustomer(RestClientFactory.getCustomerClient().getByName("Tim Hortons"));

        RestClientFactory.getTransactionClient().add(transaction);


    }
}
