package com.taim.client;

import com.taim.client.util.BeanMapper;
import com.taim.client.util.PropertiesProcessor;
import com.taim.client.util.RestTemplateFactory;
import com.taim.dto.ProductDTO;
import com.taim.dto.StaffDTO;
import com.taim.model.Staff;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by dragonliu on 2017/8/30.
 */
public class StaffClient implements IClient<StaffDTO>{
    private static final String STAFF_PATH= PropertiesProcessor.serverUrl+"/staff";
    private static HttpHeaders headers = new HttpHeaders();
    private static RestTemplate restTemplate = RestTemplateFactory.getRestTemplate();
    static {
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    public List<StaffDTO> getList(){
        String url = STAFF_PATH+"/getAll";
        HttpEntity<String> requestEntity = new HttpEntity<String>(headers);

        ResponseEntity<Staff[]> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity,Staff[].class);
        Staff[] staffs = responseEntity.getBody();
        List<StaffDTO> staffList = new ArrayList<>();
        Arrays.stream(staffs).forEach(p->staffList.add(BeanMapper.map(p, StaffDTO.class)));

        return staffList;
    }


    public StaffDTO add(StaffDTO staffDTO){
        String url = STAFF_PATH+"/add";
        HttpEntity<Staff> requestEntity = new HttpEntity<Staff>(BeanMapper.map(staffDTO, Staff.class), headers);
        ResponseEntity<Staff> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Staff.class);
        return BeanMapper.map(responseEntity.getBody(), StaffDTO.class);
    }

    public StaffDTO getByName(String name){
        String url = STAFF_PATH+"/getByName"+"?name="+name;
        HttpEntity<String> requestEntity = new HttpEntity<String>(headers);

        ResponseEntity<Staff> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity,Staff.class);
        return BeanMapper.map(responseEntity.getBody(), StaffDTO.class);
    }

    public String deleteByName(String name){
        String url = STAFF_PATH+"/deleteObject"+"?name="+name;
        HttpEntity<String> requestEntity = new HttpEntity<String>(headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity,String.class);
        return responseEntity.getBody().replace("\"", "");
    }

    public StaffDTO update(StaffDTO staffDTO){
        String url = STAFF_PATH+"/update";
        HttpEntity<Staff> requestEntity = new HttpEntity<Staff>(BeanMapper.map(staffDTO, Staff.class), headers);
        ResponseEntity<Staff> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Staff.class);
        return BeanMapper.map(responseEntity.getBody(), StaffDTO.class);
    }

    public StaffDTO saveOrUpdate(StaffDTO staffDTO){
        String url = STAFF_PATH+"/saveOrUpdate";
        HttpEntity<Staff> requestEntity = new HttpEntity<Staff>(BeanMapper.map(staffDTO, Staff.class), headers);
        ResponseEntity<Staff> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Staff.class);
        return BeanMapper.map(responseEntity.getBody(), StaffDTO.class);
    }
}
