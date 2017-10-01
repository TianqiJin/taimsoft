package com.taim.client.util;

import com.taim.client.configuration.ClientResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

public class RestTemplateFactory {
    public static RestTemplate getRestTemplate(){
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new ClientResponseErrorHandler());
        return restTemplate;
    }
}
