package com.taim.client;

import com.taim.client.util.BeanMapper;
import com.taim.client.util.PropertiesProcessor;
import com.taim.client.util.RestTemplateFactory;
import com.taim.dto.ProductDTO;
import com.taim.model.Product;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by dragonliu on 2017/8/30.
 */
public class ProductClient implements IClient<ProductDTO>{
    private static final String PRODUCT_PATH= PropertiesProcessor.serverUrl+"/product";
    private static HttpHeaders headers = new HttpHeaders();
    private static RestTemplate restTemplate = RestTemplateFactory.getRestTemplate();
    static {
        headers.setContentType(MediaType.APPLICATION_JSON);
    }


    public List<ProductDTO> getList(){
        String url = PRODUCT_PATH+"/getAll";
        HttpEntity<String> requestEntity = new HttpEntity<String>(headers);

        ResponseEntity<Product[]> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, Product[].class);
        Product[] products = responseEntity.getBody();
        List<ProductDTO> productList = new ArrayList<>();
        Arrays.stream(products).forEach(p->productList.add(BeanMapper.map(p, ProductDTO.class)));

        return productList;
    }


    public ProductDTO add(ProductDTO productDTO){
        String url = PRODUCT_PATH+"/add";
        Product product = BeanMapper.map(productDTO, Product.class);
        HttpEntity<Product> requestEntity = new HttpEntity<Product>(product, headers);
        ResponseEntity<Product> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Product.class);
        return BeanMapper.map(responseEntity.getBody(), ProductDTO.class);
    }

    public ProductDTO getByTexture(String texture){
        String url = PRODUCT_PATH+"/getByTexture"+"?texture="+texture;
        HttpEntity<String> requestEntity = new HttpEntity<String>(headers);

        ResponseEntity<Product> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity,Product.class);
        return BeanMapper.map(responseEntity.getBody(), ProductDTO.class);
    }

    public ProductDTO getById(Integer id){
        String url = PRODUCT_PATH+"/getById"+"?id="+id;
        HttpEntity<String> requestEntity = new HttpEntity<String>(headers);

        ResponseEntity<Product> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity,Product.class);
        return BeanMapper.map(responseEntity.getBody(), ProductDTO.class);
    }

    public String deleteById(Integer id){
        String url = PRODUCT_PATH+"/deleteObject"+"?id="+id;
        HttpEntity<String> requestEntity = new HttpEntity<String>(headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity,String.class);
        return responseEntity.getBody().replace("\"", "");
    }

    public ProductDTO update(ProductDTO productDTO){
        String url = PRODUCT_PATH+"/update";
        HttpEntity<Product> requestEntity = new HttpEntity<Product>(BeanMapper.map(productDTO, Product.class), headers);
        ResponseEntity<Product> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Product.class);
        return BeanMapper.map(responseEntity.getBody(), ProductDTO.class);
    }

}
