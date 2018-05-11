package com.taim.client;

import java.io.IOException;
import java.util.List;

public interface IClient<T> {
    public static class ClientException extends IOException {
        public ClientException(String msg){
            super(msg);
        }
        public ClientException(String msg, Exception ex){
            super(msg, ex);
        }
    }

    T add(T obj);
    List<T> getList();
    T update(T obj);
    T getById(Long id);
}
