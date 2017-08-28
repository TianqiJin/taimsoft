package com.taimsoft.desktopui.models;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tjin on 2017-08-27.
 */
public class HeadersCollection {
    private Map<String, String> headers;

    public HeadersCollection() {
        headers = new HashMap<String, String>();
    }

    public HeadersCollection(HashMap<String, String> headers) {
        this.headers = headers;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(HashMap<String, String> headers) {
        this.headers = headers;
    }

    public HeadersCollection addHeader(String key, String value) {
        this.getHeaders().put(key, value);
        return this;
    }
}
