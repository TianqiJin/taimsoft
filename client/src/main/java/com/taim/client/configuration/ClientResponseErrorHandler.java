package com.taim.client.configuration;

import com.taim.client.IClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ClientResponseErrorHandler implements ResponseErrorHandler {
    private final Logger logger = LoggerFactory.getLogger(ClientResponseErrorHandler.class);

    @Override
    public boolean hasError(ClientHttpResponse clientHttpResponse) throws IOException {
        return (!clientHttpResponse.getStatusCode().is2xxSuccessful());
    }

    @Override
    public void handleError(ClientHttpResponse clientHttpResponse) throws IOException {
        throw new IClient.ClientException(getBody(clientHttpResponse.getBody()));
    }

    private String getBody(InputStream inputStream){
        StringBuilder result = new StringBuilder();
        try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))){
            String inputLine;
            while((inputLine = bufferedReader.readLine()) != null) {
                result.append(inputLine).append("\n");
            }
        } catch (IOException e) {
            logger.error("Error reading response body: ", e);
        }
        return result.toString();
    }
}
