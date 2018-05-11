package com.taim.client;

import com.taim.client.util.BeanMapper;
import com.taim.client.util.PropertiesProcessor;
import com.taim.client.util.RestTemplateFactory;
import com.taim.dto.PaymentDTO;
import com.taim.model.Payment;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PaymentClient implements IClient<PaymentDTO>{
    private static final String PAYMENT_PATH= PropertiesProcessor.serverUrl+"/payment";
    private static HttpHeaders headers = new HttpHeaders();
    private static RestTemplate restTemplate = RestTemplateFactory.getRestTemplate();
    static {
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    public List<PaymentDTO> getList(){
        String url = PAYMENT_PATH + "/getAll";
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<Payment[]> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, Payment[].class);
        Payment[] payments = responseEntity.getBody();
        List<PaymentDTO> paymentList = new ArrayList<>();
        Arrays.stream(payments).forEach(p -> paymentList.add(BeanMapper.map(p, PaymentDTO.class)));

        return paymentList;
    }

    public PaymentDTO add(PaymentDTO PaymentDTO){
        String url = PAYMENT_PATH + "/add";
        Payment payment = BeanMapper.map(PaymentDTO, Payment.class);
        HttpEntity<Payment> requestEntity = new HttpEntity<>(payment, headers);
        ResponseEntity<Payment> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Payment.class);
        return BeanMapper.map(responseEntity.getBody(), PaymentDTO.class);
    }

    public PaymentDTO getById(Long id){
        String url = PAYMENT_PATH + "/getById"+"?id="+id;
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<Payment> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity,Payment.class);
        return BeanMapper.map(responseEntity.getBody(), PaymentDTO.class);
    }

    public String deleteById(Integer id){
        String url = PAYMENT_PATH + "/deleteObject"+"?id="+id;
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity,String.class);
        return responseEntity.getBody().replace("\"", "");
    }

    public PaymentDTO update(PaymentDTO PaymentDTO){
        String url = PAYMENT_PATH + "/update";
        HttpEntity<Payment> requestEntity = new HttpEntity<>(BeanMapper.map(PaymentDTO, Payment.class), headers);
        ResponseEntity<Payment> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Payment.class);
        return BeanMapper.map(responseEntity.getBody(), PaymentDTO.class);
    }

    public PaymentDTO saveOrUpdate(PaymentDTO PaymentDTO){
        String url = PAYMENT_PATH + "/saveOrUpdate";
        HttpEntity<Payment> requestEntity = new HttpEntity<>(BeanMapper.map(PaymentDTO, Payment.class), headers);
        ResponseEntity<Payment> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Payment.class);
        return BeanMapper.map(responseEntity.getBody(), PaymentDTO.class);
    }
}
