package com.taim.client;

import com.taim.client.util.PropertiesProcessor;
import com.taim.model.Product;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by dragonliu on 2017/8/30.
 */
public class ProductClient {
    private static final String PRODUCT_PATH= PropertiesProcessor.serverUrl+"/product";

    public List<Product> getProductList(){

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        RestTemplate restTemplate = new RestTemplate();
        String url = PRODUCT_PATH+"/getAll";
        HttpEntity<String> requestEntity = new HttpEntity<String>(headers);

        ResponseEntity<Product[]> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity,Product[].class);
        Product[] products = responseEntity.getBody();
        List<Product> productList = new ArrayList<>();
        Arrays.stream(products).forEach(p->productList.add(p));

        return productList;
    }


    public Product addProduct(Product product){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        RestTemplate restTemplate = new RestTemplate();
        String url = PRODUCT_PATH+"/add";
        HttpEntity<Product> requestEntity = new HttpEntity<Product>(product, headers);
        ResponseEntity<Product> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Product.class);
        return responseEntity.getBody();
    }

    public Product getProductByTexture(String texture){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        RestTemplate restTemplate = new RestTemplate();
        String url = PRODUCT_PATH+"/getByTexture"+"?texture="+texture;
        HttpEntity<String> requestEntity = new HttpEntity<String>(headers);

        ResponseEntity<Product> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity,Product.class);
        return responseEntity.getBody();
    }

    public Product getProductById(Integer id){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        RestTemplate restTemplate = new RestTemplate();
        String url = PRODUCT_PATH+"/getById"+"?id="+id;
        HttpEntity<String> requestEntity = new HttpEntity<String>(headers);

        ResponseEntity<Product> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity,Product.class);
        return responseEntity.getBody();
    }

    public String deleteProductById(Integer id){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        RestTemplate restTemplate = new RestTemplate();
        String url = PRODUCT_PATH+"/deleteObject"+"?id="+id;
        HttpEntity<String> requestEntity = new HttpEntity<String>(headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity,String.class);
        return responseEntity.getBody().replace("\"", "");
    }

    public Product updateProduct(Product product){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        RestTemplate restTemplate = new RestTemplate();
        String url = PRODUCT_PATH+"/update";
        HttpEntity<Product> requestEntity = new HttpEntity<Product>(product, headers);
        ResponseEntity<Product> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Product.class);
        return responseEntity.getBody();
    }

}
