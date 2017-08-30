package com.taim.client;

import com.taim.client.util.PropertiesProcessor;
import com.taim.model.Staff;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by dragonliu on 2017/8/30.
 */
public class StaffClient {
    private static final String STAFF_PATH= PropertiesProcessor.serverUrl+"/staff";

    public List<Staff> getStaffList(){

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        RestTemplate restTemplate = new RestTemplate();
        String url = STAFF_PATH+"/getAll";
        HttpEntity<String> requestEntity = new HttpEntity<String>(headers);

        ResponseEntity<Staff[]> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity,Staff[].class);
        Staff[] staffs = responseEntity.getBody();
        List<Staff> staffList = new ArrayList<>();
        Arrays.stream(staffs).forEach(p->staffList.add(p));

        return staffList;
    }


    public Staff addStaff(Staff staff){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        RestTemplate restTemplate = new RestTemplate();
        String url = STAFF_PATH+"/add";
        HttpEntity<Staff> requestEntity = new HttpEntity<Staff>(staff, headers);
        ResponseEntity<Staff> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Staff.class);
        return responseEntity.getBody();
    }

    public Staff getStaffByName(String name){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        RestTemplate restTemplate = new RestTemplate();
        String url = STAFF_PATH+"/getByName"+"?name="+name;
        HttpEntity<String> requestEntity = new HttpEntity<String>(headers);

        ResponseEntity<Staff> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity,Staff.class);
        return responseEntity.getBody();
    }

    public String deleteStaffByName(String name){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        RestTemplate restTemplate = new RestTemplate();
        String url = STAFF_PATH+"/deleteObject"+"?name="+name;
        HttpEntity<String> requestEntity = new HttpEntity<String>(headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity,String.class);
        return responseEntity.getBody().replace("\"", "");
    }

    public Staff updateStaff(Staff staff){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        RestTemplate restTemplate = new RestTemplate();
        String url = STAFF_PATH+"/update";
        HttpEntity<Staff> requestEntity = new HttpEntity<Staff>(staff, headers);
        ResponseEntity<Staff> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Staff.class);
        return responseEntity.getBody();
    }
}
