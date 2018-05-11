package com.taim.client;

import com.taim.client.util.BeanMapper;
import com.taim.client.util.PropertiesProcessor;
import com.taim.client.util.RestTemplateFactory;
import com.taim.dto.CustomerDTO;
import com.taim.model.Customer;
import org.apache.commons.beanutils.BeanMap;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by dragonliu on 2017/8/30.
 */
public class CustomerClient implements IClient<CustomerDTO> {
    private static final String CUSTOMER_PATH= PropertiesProcessor.serverUrl+"/customer";
    private static HttpHeaders headers = new HttpHeaders();
    private static RestTemplate restTemplate = RestTemplateFactory.getRestTemplate();
    static {
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    public List<CustomerDTO> getList(){
        String url = CUSTOMER_PATH+"/getAll";
        HttpEntity<String> requestEntity = new HttpEntity<String>(headers);

        ResponseEntity<Customer[]> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity,Customer[].class);
        Customer[] customers = responseEntity.getBody();
        List<CustomerDTO> customerList = new ArrayList<>();
        Arrays.stream(customers).forEach(p->customerList.add(BeanMapper.map(p, CustomerDTO.class)));

        return customerList;
    }

    public CustomerDTO add(CustomerDTO customerDTO){
        String url = CUSTOMER_PATH+"/add";
        HttpEntity<Customer> requestEntity = new HttpEntity<>(BeanMapper.map(customerDTO, Customer.class), headers);
        ResponseEntity<Customer> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Customer.class);
        return BeanMapper.map(responseEntity.getBody(), CustomerDTO.class);
    }

    public CustomerDTO getByName(String name){
        String url = CUSTOMER_PATH+"/getByName"+"?name="+name;
        HttpEntity<String> requestEntity = new HttpEntity<String>(headers);

        ResponseEntity<Customer> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity,Customer.class);
        return BeanMapper.map(responseEntity.getBody(), CustomerDTO.class);
    }

    public CustomerDTO getById(Long id){
        String url = CUSTOMER_PATH+"/getById"+"?id="+id;
        HttpEntity<String> requestEntity = new HttpEntity<String>(headers);

        ResponseEntity<Customer> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity,Customer.class);
        return BeanMapper.map(responseEntity.getBody(), CustomerDTO.class);
    }

    public String deleteByName(String name){
        String url = CUSTOMER_PATH+"/deleteObject"+"?name="+name;
        HttpEntity<String> requestEntity = new HttpEntity<String>(headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity,String.class);
        return responseEntity.getBody().replace("\"", "");
    }

    public CustomerDTO update(CustomerDTO customerDTO){
        String url = CUSTOMER_PATH+"/update";
        HttpEntity<Customer> requestEntity = new HttpEntity<Customer>(BeanMapper.map(customerDTO, Customer.class), headers);
        ResponseEntity<Customer> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Customer.class);
        return BeanMapper.map(responseEntity.getBody(), CustomerDTO.class);
    }

    public CustomerDTO saveOrUpdate(CustomerDTO customerDTO){
        String url = CUSTOMER_PATH+"/saveOrUpdate";
        HttpEntity<Customer> requestEntity = new HttpEntity<Customer>(BeanMapper.map(customerDTO, Customer.class), headers);
        ResponseEntity<Customer> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Customer.class);
        return BeanMapper.map(responseEntity.getBody(), CustomerDTO.class);
    }
}
