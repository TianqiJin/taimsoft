package com.taimsoft.desktopui.models;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ws.rs.core.Response;
import java.io.IOException;

/**
 * Created by tjin on 2017-08-27.
 */
public class ResponseContainer<T> {

    public static class ResponseContainerException extends Exception{
        public ResponseContainerException(String msg){
            super(msg);
        }
        public ResponseContainerException(String msg, Exception ex){
            super(msg, ex);
        }
    }

    private Response response;
    private T resourceObject;
    private static ObjectMapper mapper;
    private Class<T> tClass;

    static {
        mapper = new ObjectMapper();
    }

    public ResponseContainer(Response response, Class tClass){
        this.response = response;
        this.tClass = tClass;
    }

    public T deserializeResourceObject() throws IOException {
        return mapper.readValue(this.response.readEntity(String.class), tClass);
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public T getResourceObject() throws IOException {
        if(this.response.getStatus() % 2 != 100){
            String errorMsg = ""
            throw new ResponseContainerException()
        }else{
            this.resourceObject = this.deserializeResourceObject();
            return this.resourceObject;
        }
    }

    public void setResourceObject(T resourceObject) {
        this.resourceObject = resourceObject;
    }
}
