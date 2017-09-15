package com.taim.client;

import com.taim.client.util.BeanMapper;
import com.taim.client.util.PropertiesProcessor;
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
public class TransactionClient {
    private static final String TRANSACTION_PATH= PropertiesProcessor.serverUrl+"/transaction";

    public List<TransactionDTO> getTransactionList(){

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        RestTemplate restTemplate = new RestTemplate();
        String url = TRANSACTION_PATH+"/getAll";
        HttpEntity<String> requestEntity = new HttpEntity<String>(headers);

        ResponseEntity<Transaction[]> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity,Transaction[].class);
        Transaction[] transactions = responseEntity.getBody();
        List<TransactionDTO> transactionList = new ArrayList<>();
        Arrays.stream(transactions).forEach(p->transactionList.add(BeanMapper.map(p, TransactionDTO.class)));

        return transactionList;
    }


    public TransactionDTO addTransaction(TransactionDTO transactionDTO){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        RestTemplate restTemplate = new RestTemplate();
        String url = TRANSACTION_PATH+"/add";
        HttpEntity<Transaction> requestEntity = new HttpEntity<Transaction>(BeanMapper.map(transactionDTO, Transaction.class), headers);
        ResponseEntity<Transaction> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Transaction.class);
        return BeanMapper.map(responseEntity.getBody(), TransactionDTO.class);
    }

    public TransactionDTO getTransactionById(Integer id){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        RestTemplate restTemplate = new RestTemplate();
        String url = TRANSACTION_PATH+"/getById"+"?id="+id;
        HttpEntity<String> requestEntity = new HttpEntity<String>(headers);

        ResponseEntity<Transaction> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity,Transaction.class);
        return BeanMapper.map(responseEntity.getBody(), TransactionDTO.class);
    }

    public String deleteTransactionById(Integer id){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        RestTemplate restTemplate = new RestTemplate();
        String url = TRANSACTION_PATH+"/deleteObject"+"?id="+id;
        HttpEntity<String> requestEntity = new HttpEntity<String>(headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity,String.class);
        return responseEntity.getBody().replace("\"", "");
    }

    public TransactionDTO updateTransaction(TransactionDTO transactionDTO){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        RestTemplate restTemplate = new RestTemplate();
        String url = TRANSACTION_PATH+"/update";
        HttpEntity<Transaction> requestEntity = new HttpEntity<Transaction>(BeanMapper.map(transactionDTO, Transaction.class), headers);
        ResponseEntity<Transaction> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Transaction.class);
        return BeanMapper.map(responseEntity.getBody(), TransactionDTO.class);
    }
}
