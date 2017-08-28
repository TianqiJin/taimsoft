package com.taimsoft.desktopui.models;

/**
 * Created by tjin on 2017-08-27.
 */
public enum HttpVerb {
    GET("get"),
    POST("post"),
    DELETE("delete");

    private String value;

    HttpVerb(String vvalue){
        this.value = vvalue;
    }

    public String getValue() {
        return value;
    }
}
