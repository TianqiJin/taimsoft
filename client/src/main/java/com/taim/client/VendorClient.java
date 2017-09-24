package com.taim.client;

import com.taim.client.util.BeanMapper;
import com.taim.client.util.PropertiesProcessor;
import com.taim.dto.VendorDTO;
import com.taim.model.Vendor;
import org.apache.commons.beanutils.BeanMap;
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
    private static HttpHeaders headers = new HttpHeaders();
    private static RestTemplate restTemplate = new RestTemplate();
    static {
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    public List<VendorDTO> getVendorList(){
        String url = VENDOR_PATH+"/getAll";
        HttpEntity<String> requestEntity = new HttpEntity<String>(headers);

        ResponseEntity<Vendor[]> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity,Vendor[].class);
        Vendor[] vendors = responseEntity.getBody();
        List<VendorDTO> vendorList = new ArrayList<>();
        Arrays.stream(vendors).forEach(p->vendorList.add(BeanMapper.map(p, VendorDTO.class)));

        return vendorList;
    }


    public VendorDTO addVendor(VendorDTO vendorDTO){
        String url = VENDOR_PATH+"/add";
        HttpEntity<Vendor> requestEntity = new HttpEntity<Vendor>(BeanMapper.map(vendorDTO, Vendor.class), headers);
        ResponseEntity<Vendor> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Vendor.class);
        return BeanMapper.map(responseEntity.getBody(), VendorDTO.class);
    }

    public VendorDTO getVendorByName(String name){
        String url = VENDOR_PATH+"/getByName"+"?name="+name;
        HttpEntity<String> requestEntity = new HttpEntity<String>(headers);

        ResponseEntity<Vendor> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity,Vendor.class);
        return BeanMapper.map(responseEntity.getBody(), VendorDTO.class);
    }

    public String deleteVendorByName(String name){
        String url = VENDOR_PATH+"/deleteObject"+"?name="+name;
        HttpEntity<String> requestEntity = new HttpEntity<String>(headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity,String.class);
        return responseEntity.getBody().replace("\"", "");
    }

    public VendorDTO updateVendor(VendorDTO vendorDTO){
        String url = VENDOR_PATH+"/update";
        HttpEntity<Vendor> requestEntity = new HttpEntity<Vendor>(BeanMapper.map(vendorDTO, Vendor.class), headers);
        ResponseEntity<Vendor> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Vendor.class);
        return BeanMapper.map(responseEntity.getBody(), VendorDTO.class);
    }
}
