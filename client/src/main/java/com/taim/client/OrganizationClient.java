package com.taim.client;

import com.taim.client.util.PropertiesProcessor;
import com.taim.model.Organization;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by dragonliu on 2017/8/27.
 */

public class OrganizationClient {
    private static final String ORGANIZATION_PATH=PropertiesProcessor.serverUrl+"/organization";

    public List<Organization> getOrganizationList(){

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        RestTemplate restTemplate = new RestTemplate();
        String url = ORGANIZATION_PATH+"/getAll";
        HttpEntity<String> requestEntity = new HttpEntity<String>(headers);

        ResponseEntity<Organization[]> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity,Organization[].class);
        Organization[] organizations = responseEntity.getBody();
        List<Organization> organizationList = new ArrayList<>();
        Arrays.stream(organizations).forEach(p->organizationList.add(p));

        return organizationList;
    }


    public Organization addOrganization(Organization organization){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        RestTemplate restTemplate = new RestTemplate();
        String url = ORGANIZATION_PATH+"/add";
        HttpEntity<Organization> requestEntity = new HttpEntity<Organization>(organization, headers);
        ResponseEntity<Organization> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Organization.class);
        return responseEntity.getBody();
    }

    public Organization getOrganizationByName(String name){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        RestTemplate restTemplate = new RestTemplate();
        String url = ORGANIZATION_PATH+"/getByName"+"?name="+name;
        HttpEntity<String> requestEntity = new HttpEntity<String>(headers);

        ResponseEntity<Organization> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity,Organization.class);
        return responseEntity.getBody();
    }

    public String deleteOrganizationByName(String name){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        RestTemplate restTemplate = new RestTemplate();
        String url = ORGANIZATION_PATH+"/deleteObject"+"?name="+name;
        HttpEntity<String> requestEntity = new HttpEntity<String>(headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity,String.class);
        return responseEntity.getBody().replace("\"", "");
    }

    public Organization updateOrganization(Organization organization){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        RestTemplate restTemplate = new RestTemplate();
        String url = ORGANIZATION_PATH+"/update";
        HttpEntity<Organization> requestEntity = new HttpEntity<Organization>(organization, headers);
        ResponseEntity<Organization> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Organization.class);
        return responseEntity.getBody();
    }

}
