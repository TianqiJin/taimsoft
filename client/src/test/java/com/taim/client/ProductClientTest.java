package com.taim.client;

import com.taim.dto.ProductDTO;
import com.taim.model.Product;
import junit.framework.Assert;
import org.joda.time.DateTime;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

/**
 * Created by dragonliu on 2017/8/30.
 */
public class ProductClientTest {
    private ProductClient client = new ProductClient();
    private static ProductDTO product;

    @BeforeClass
    public static void prepareObject(){
        DateTime d1Created = DateTime.now();
        DateTime d1Modified = DateTime.now();
        product = new ProductDTO();
        product.setLength(100);
        product.setWidth(5);
        product.setHeight(50);
        product.setTexture("Smooth");
        product.setTotalNum(2000);
        product.setUnitPrice(20);
        product.setDateCreated(d1Created);
        product.setDateModified(d1Modified);
    }

    @Test
    public void addProductTest()throws Exception{

        ProductDTO prod = client.addProduct(product);
        Assert.assertEquals(product.getLength(), prod.getLength());
        Assert.assertEquals(product.getHeight(), prod.getHeight());
        Assert.assertEquals(product.getWidth(), prod.getWidth());
        Assert.assertEquals(product.getTexture(), prod.getTexture());
        Assert.assertEquals(product.getTotalNum(), prod.getTotalNum());
        Assert.assertEquals(product.getUnitPrice(), prod.getUnitPrice());
        Assert.assertEquals(product.getDateCreated().getMillis(), prod.getDateCreated().getMillis());
        Assert.assertEquals(product.getDateModified().getMillis(), prod.getDateModified().getMillis());
    }

    @Test
    public void getProductListTest()throws Exception{
        Thread.sleep(2000);
        List<ProductDTO> prods = client.getProductList();
        Assert.assertEquals(1, prods.size());
        ProductDTO prod = prods.get(0);
        Assert.assertEquals(product.getLength(), prod.getLength());
        Assert.assertEquals(product.getHeight(), prod.getHeight());
        Assert.assertEquals(product.getWidth(), prod.getWidth());
        Assert.assertEquals(product.getTexture(), prod.getTexture());
        Assert.assertEquals(product.getTotalNum(), prod.getTotalNum());
        Assert.assertEquals(product.getUnitPrice(), prod.getUnitPrice());
        Assert.assertEquals(product.getDateCreated().getMillis(), prod.getDateCreated().getMillis());
        Assert.assertEquals(product.getDateModified().getMillis(), prod.getDateModified().getMillis());
    }

    @Test
    public void getProductByTextureTest()throws Exception{
        ProductDTO prod = client.getProductByTexture("Smooth");
        Assert.assertEquals(product.getLength(), prod.getLength());
        Assert.assertEquals(product.getHeight(), prod.getHeight());
        Assert.assertEquals(product.getWidth(), prod.getWidth());
        Assert.assertEquals(product.getTexture(), prod.getTexture());
        Assert.assertEquals(product.getTotalNum(), prod.getTotalNum());
        Assert.assertEquals(product.getUnitPrice(), prod.getUnitPrice());
        Assert.assertEquals(product.getDateCreated().getMillis(), prod.getDateCreated().getMillis());
        Assert.assertEquals(product.getDateModified().getMillis(), prod.getDateModified().getMillis());
    }

    @Test
    public void getProductByIdTest()throws Exception{
        ProductDTO prod = client.getProductById(1);
        Assert.assertEquals(product.getLength(), prod.getLength());
        Assert.assertEquals(product.getHeight(), prod.getHeight());
        Assert.assertEquals(product.getWidth(), prod.getWidth());
        Assert.assertEquals(product.getTexture(), prod.getTexture());
        Assert.assertEquals(product.getTotalNum(), prod.getTotalNum());
        Assert.assertEquals(product.getUnitPrice(), prod.getUnitPrice());
        Assert.assertEquals(product.getDateCreated().getMillis(), prod.getDateCreated().getMillis());
        Assert.assertEquals(product.getDateModified().getMillis(), prod.getDateModified().getMillis());
    }

    @Test
    public void updateProductTest()throws Exception{

        ProductDTO prod = client.getProductById(1);
        prod.setTexture("Rough");
        prod.setDateModified(DateTime.now());
        ProductDTO p1 = client.updateProduct(prod);

        Assert.assertEquals(prod.getLength(), p1.getLength());
        Assert.assertEquals(prod.getHeight(), p1.getHeight());
        Assert.assertEquals(prod.getWidth(), p1.getWidth());
        Assert.assertEquals(prod.getTexture(), p1.getTexture());
        Assert.assertEquals(prod.getTotalNum(), p1.getTotalNum());
        Assert.assertEquals(prod.getUnitPrice(), p1.getUnitPrice());
        Assert.assertEquals(prod.getDateCreated().getMillis(), p1.getDateCreated().getMillis());
        Assert.assertEquals(prod.getDateModified().getMillis(), p1.getDateModified().getMillis());
    }


    @Test
    public void deleteProductByNameTest()throws Exception{
        String result = client.deleteProductById(1);
        Assert.assertEquals("Deleted!", result);
        Thread.sleep(2000);
        List<ProductDTO> prods = client.getProductList();
        Assert.assertEquals(0, prods.size());
    }


}