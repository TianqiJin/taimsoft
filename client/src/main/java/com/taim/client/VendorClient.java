package com.taim.client;

import com.taim.client.util.PropertiesProcessor;
import com.taim.model.Vendor;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by dragonliu on 2017/8/30.
 */
public class VendorClient {
    private static final String VENDOR_PATH= PropertiesProcessor.serverUrl+"/vendor";

    public List<Vendor> getVendorList(){

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        RestTemplate restTemplate = new RestTemplate();
        String url = VENDOR_PATH+"/getAll";
        HttpEntity<String> requestEntity = new HttpEntity<String>(headers);

        ResponseEntity<Vendor[]> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity,Vendor[].class);
        Vendor[] vendors = responseEntity.getBody();
        List<Vendor> vendorList = new ArrayList<>();
        Arrays.stream(vendors).forEach(p->vendorList.add(p));

        return vendorList;
    }


    public Vendor addVendor(Vendor vendor){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        RestTemplate restTemplate = new RestTemplate();
        String url = VENDOR_PATH+"/add";
        HttpEntity<Vendor> requestEntity = new HttpEntity<Vendor>(vendor, headers);
        ResponseEntity<Vendor> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Vendor.class);
        return responseEntity.getBody();
    }

    public Vendor getVendorByName(String name){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        RestTemplate restTemplate = new RestTemplate();
        String url = VENDOR_PATH+"/getByName"+"?name="+name;
        HttpEntity<String> requestEntity = new HttpEntity<String>(headers);

        ResponseEntity<Vendor> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity,Vendor.class);
        return responseEntity.getBody();
    }

    public String deleteVendorByName(String name){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        RestTemplate restTemplate = new RestTemplate();
        String url = VENDOR_PATH+"/deleteObject"+"?name="+name;
        HttpEntity<String> requestEntity = new HttpEntity<String>(headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity,String.class);
        return responseEntity.getBody().replace("\"", "");
    }

    public Vendor updateVendor(Vendor vendor){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        RestTemplate restTemplate = new RestTemplate();
        String url = VENDOR_PATH+"/update";
        HttpEntity<Vendor> requestEntity = new HttpEntity<Vendor>(vendor, headers);
        ResponseEntity<Vendor> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Vendor.class);
        return responseEntity.getBody();
    }
}
