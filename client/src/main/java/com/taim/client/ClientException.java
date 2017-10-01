package com.taim.client;

import java.io.IOException;

public class ClientException extends IOException {
    public ClientException(String msg){
        super(msg);
    }
    public ClientException(String msg, Exception ex){
        super(msg, ex);
    }
}
