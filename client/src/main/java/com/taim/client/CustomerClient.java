package com.taim.client;

import com.taim.client.util.PropertiesProcessor;
import com.taim.model.Customer;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by dragonliu on 2017/8/30.
 */
public class CustomerClient {
    private static final String CUSTOMER_PATH= PropertiesProcessor.serverUrl+"/customer";

    public List<Customer> getCustomerList(){

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        RestTemplate restTemplate = new RestTemplate();
        String url = CUSTOMER_PATH+"/getAll";
        HttpEntity<String> requestEntity = new HttpEntity<String>(headers);

        ResponseEntity<Customer[]> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity,Customer[].class);
        Customer[] customers = responseEntity.getBody();
        List<Customer> customerList = new ArrayList<>();
        Arrays.stream(customers).forEach(p->customerList.add(p));

        return customerList;
    }


    public Customer addCustomer(Customer customer){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        RestTemplate restTemplate = new RestTemplate();
        String url = CUSTOMER_PATH+"/add";
        HttpEntity<Customer> requestEntity = new HttpEntity<Customer>(customer, headers);
        ResponseEntity<Customer> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Customer.class);
        return responseEntity.getBody();
    }

    public Customer getCustomerByName(String name){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        RestTemplate restTemplate = new RestTemplate();
        String url = CUSTOMER_PATH+"/getByName"+"?name="+name;
        HttpEntity<String> requestEntity = new HttpEntity<String>(headers);

        ResponseEntity<Customer> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity,Customer.class);
        return responseEntity.getBody();
    }

    public String deleteCustomerByName(String name){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        RestTemplate restTemplate = new RestTemplate();
        String url = CUSTOMER_PATH+"/deleteObject"+"?name="+name;
        HttpEntity<String> requestEntity = new HttpEntity<String>(headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity,String.class);
        return responseEntity.getBody().replace("\"", "");
    }

    public Customer updateCustomer(Customer customer){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        RestTemplate restTemplate = new RestTemplate();
        String url = CUSTOMER_PATH+"/update";
        HttpEntity<Customer> requestEntity = new HttpEntity<Customer>(customer, headers);
        ResponseEntity<Customer> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Customer.class);
        return responseEntity.getBody();
    }
}
