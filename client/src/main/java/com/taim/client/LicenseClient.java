package com.taim.client;

import com.taim.client.util.BeanMapper;
import com.taim.client.util.PropertiesProcessor;
import com.taim.client.util.RestTemplateFactory;
import com.taim.dto.LicenseDTO;
import com.taim.model.License;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LicenseClient {
    private static final String License_PATH= PropertiesProcessor.serverUrl+"/license";
    private static HttpHeaders headers = new HttpHeaders();
    private static RestTemplate restTemplate = RestTemplateFactory.getRestTemplate();
    static {
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    public List<LicenseDTO> getList(){
        String url = License_PATH+"/getAll";
        HttpEntity<String> requestEntity = new HttpEntity<String>(headers);

        ResponseEntity<License[]> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity,License[].class);
        License[] Licenses = responseEntity.getBody();
        List<LicenseDTO> LicenseList = new ArrayList<>();
        Arrays.stream(Licenses).forEach(p->LicenseList.add(BeanMapper.map(p, LicenseDTO.class)));

        return LicenseList;
    }

    public LicenseDTO add(LicenseDTO licenseDTO){
        String url = License_PATH+"/add";
        HttpEntity<License> requestEntity = new HttpEntity<>(BeanMapper.map(licenseDTO, License.class), headers);
        ResponseEntity<License> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, License.class);
        return BeanMapper.map(responseEntity.getBody(), LicenseDTO.class);
    }

    public LicenseDTO getByName(String name){
        String url = License_PATH+"/getByName"+"?name="+name;
        HttpEntity<String> requestEntity = new HttpEntity<String>(headers);

        ResponseEntity<License> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity,License.class);
        return BeanMapper.map(responseEntity.getBody(), LicenseDTO.class);
    }

    public LicenseDTO getById(Integer id){
        String url = License_PATH+"/getById"+"?id="+id;
        HttpEntity<String> requestEntity = new HttpEntity<String>(headers);

        ResponseEntity<License> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity,License.class);
        return BeanMapper.map(responseEntity.getBody(), LicenseDTO.class);
    }

    public LicenseDTO saveOrUpdate(LicenseDTO licenseDTO){
        String url = License_PATH+"/saveOrUpdate";
        HttpEntity<License> requestEntity = new HttpEntity<License>(BeanMapper.map(licenseDTO, License.class), headers);
        ResponseEntity<License> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, License.class);
        return BeanMapper.map(responseEntity.getBody(), LicenseDTO.class);
    }

}
