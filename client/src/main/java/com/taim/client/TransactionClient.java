package com.taim.client;

import com.taim.client.util.BeanMapper;
import com.taim.client.util.PropertiesProcessor;
import com.taim.client.util.RestTemplateFactory;
import com.taim.dto.TransactionDTO;
import com.taim.model.Transaction;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by dragonliu on 2017/8/30.
 */
public class TransactionClient implements IClient<TransactionDTO>{
    private static final String TRANSACTION_PATH= PropertiesProcessor.serverUrl+"/transaction";
    private static HttpHeaders headers = new HttpHeaders();
    private static RestTemplate restTemplate = RestTemplateFactory.getRestTemplate();
    static {
        headers.setContentType(MediaType.APPLICATION_JSON);
    }


    public List<TransactionDTO> getList(){
        String url = TRANSACTION_PATH+"/getAll";
        HttpEntity<String> requestEntity = new HttpEntity<String>(headers);

        ResponseEntity<Transaction[]> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity,Transaction[].class);
        Transaction[] transactions = responseEntity.getBody();
        List<TransactionDTO> transactionList = new ArrayList<>();
        Arrays.stream(transactions).forEach(p->transactionList.add(BeanMapper.map(p, TransactionDTO.class)));
        System.out.println(transactionList.get(0).getDeliveryDueDate());
        return transactionList;
    }


    public TransactionDTO add(TransactionDTO transactionDTO){
        String url = TRANSACTION_PATH+"/add";
        HttpEntity<Transaction> requestEntity = new HttpEntity<Transaction>(BeanMapper.map(transactionDTO, Transaction.class), headers);
        ResponseEntity<Transaction> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Transaction.class);
        return BeanMapper.map(responseEntity.getBody(), TransactionDTO.class);
    }

    public TransactionDTO getById(Integer id){
        String url = TRANSACTION_PATH+"/getById"+"?id="+id;
        HttpEntity<String> requestEntity = new HttpEntity<String>(headers);

        ResponseEntity<Transaction> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity,Transaction.class);
        return BeanMapper.map(responseEntity.getBody(), TransactionDTO.class);
    }

    public List<TransactionDTO> getListByProductID(Integer id){
        String url = TRANSACTION_PATH+"/getByProductId"+"?id="+id;
        HttpEntity<String> requestEntity = new HttpEntity<String>(headers);

        ResponseEntity<Transaction[]> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity,Transaction[].class);
        Transaction[] transactions = responseEntity.getBody();
        List<TransactionDTO> transactionList = new ArrayList<>();
        Arrays.stream(transactions).forEach(p->transactionList.add(BeanMapper.map(p, TransactionDTO.class)));

        return transactionList;
    }

    public String deleteById(Integer id){
        String url = TRANSACTION_PATH+"/deleteObject"+"?id="+id;
        HttpEntity<String> requestEntity = new HttpEntity<String>(headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity,String.class);
        return responseEntity.getBody().replace("\"", "");
    }

    public TransactionDTO update(TransactionDTO transactionDTO){
        String url = TRANSACTION_PATH+"/update";
        HttpEntity<Transaction> requestEntity = new HttpEntity<>(BeanMapper.map(transactionDTO, Transaction.class), headers);
        ResponseEntity<Transaction> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Transaction.class);
        return BeanMapper.map(responseEntity.getBody(), TransactionDTO.class);
    }
}
