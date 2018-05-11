package com.taim.client;

import com.taim.client.util.BeanMapper;
import com.taim.client.util.PropertiesProcessor;
import com.taim.client.util.RestTemplateFactory;
import com.taim.dto.PropertyDTO;
import com.taim.dto.PropertyDTO;
import com.taim.model.Property;
import com.taim.model.Property;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PropertyClient implements IClient<PropertyDTO> {
    private static final String PROPERTY_PATH= PropertiesProcessor.serverUrl+"/property";
    private static HttpHeaders headers = new HttpHeaders();
    private static RestTemplate restTemplate = RestTemplateFactory.getRestTemplate();
    static {
        headers.setContentType(MediaType.APPLICATION_JSON);
    }


    public List<PropertyDTO> getList(){
        String url = PROPERTY_PATH+"/getAll";
        HttpEntity<String> requestEntity = new HttpEntity<String>(headers);

        ResponseEntity<Property[]> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, Property[].class);
        Property[] Propertys = responseEntity.getBody();
        List<PropertyDTO> PropertyList = new ArrayList<>();
        Arrays.stream(Propertys).forEach(p->PropertyList.add(BeanMapper.map(p, PropertyDTO.class)));

        return PropertyList;
    }


    public PropertyDTO add(PropertyDTO PropertyDTO){
        String url = PROPERTY_PATH+"/add";
        Property Property = BeanMapper.map(PropertyDTO, Property.class);
        HttpEntity<Property> requestEntity = new HttpEntity<>(Property, headers);
        ResponseEntity<Property> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Property.class);
        return BeanMapper.map(responseEntity.getBody(), PropertyDTO.class);
    }
    
    public PropertyDTO getById(Long id){
        String url = PROPERTY_PATH+"/getById"+"?id="+id;
        HttpEntity<String> requestEntity = new HttpEntity<String>(headers);

        ResponseEntity<Property> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity,Property.class);
        return BeanMapper.map(responseEntity.getBody(), PropertyDTO.class);
    }

    public String deleteById(Long id){
        String url = PROPERTY_PATH+"/deleteObject"+"?id="+id;
        HttpEntity<String> requestEntity = new HttpEntity<String>(headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity,String.class);
        return responseEntity.getBody().replace("\"", "");
    }

    public PropertyDTO update(PropertyDTO PropertyDTO){
        String url = PROPERTY_PATH+"/update";
        HttpEntity<Property> requestEntity = new HttpEntity<Property>(BeanMapper.map(PropertyDTO, Property.class), headers);
        ResponseEntity<Property> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Property.class);
        return BeanMapper.map(responseEntity.getBody(), PropertyDTO.class);
    }

    public PropertyDTO saveOrUpdate(PropertyDTO PropertyDTO){
        String url = PROPERTY_PATH+"/saveOrUpdate";
        HttpEntity<Property> requestEntity = new HttpEntity<Property>(BeanMapper.map(PropertyDTO, Property.class), headers);
        ResponseEntity<Property> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Property.class);
        return BeanMapper.map(responseEntity.getBody(), PropertyDTO.class);
    }
}
