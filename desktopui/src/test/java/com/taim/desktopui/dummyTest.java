package com.taim.desktopui;

import com.taim.dto.ProductDTO;
import com.taim.dto.TransactionDTO;
import com.taim.dto.TransactionDetailDTO;
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

        RestClientFactory.getProductClient().addProduct(product1);
        RestClientFactory.getProductClient().addProduct(product2);

    }



    @Test
    public void getTransactionDetails()throws Exception{
        TransactionDTO transactionDTO = RestClientFactory.getTransactionClient().getTransactionById(1);
        System.out.println(transactionDTO.getSaleAmount()+" -> "+transactionDTO.getTransactionDetails().size());
        //transactionDTO.getTransactionDetails().stream().forEach(d-> System.out.println(d.getProduct().getSku()+": "+d.getQuantity()+" -> "+d.getSaleAmount()));

    }

    @Test
    public void addTransactionDTOTest()throws Exception{
        TransactionDetailDTO detail1 = new TransactionDetailDTO();
        detail1.setProduct(RestClientFactory.getProductClient().getProductById(1));
        detail1.setQuantity(10);
        detail1.setSaleAmount(100);

        TransactionDetailDTO detail2 = new TransactionDetailDTO();
        detail2.setProduct(RestClientFactory.getProductClient().getProductById(2));
        detail2.setQuantity(10);
        detail2.setSaleAmount(100);

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

        RestClientFactory.getTransactionClient().addTransaction(transaction);


    }
}
