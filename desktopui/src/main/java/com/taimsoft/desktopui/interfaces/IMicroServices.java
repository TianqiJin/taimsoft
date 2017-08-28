package com.taimsoft.desktopui.interfaces;

//import com.taimsoft.desktopui.models.HttpMethod;
import com.taimsoft.desktopui.models.HeadersCollection;
import com.taimsoft.desktopui.models.ResponseContainer;

import javax.ws.rs.HttpMethod;

/**
 * Created by tjin on 2017-08-27.
 */
public interface IMicroServices {
    String getBaseUrl();

    void setBaseUrl(String baseUrl);

    String getBasePath();

    void setBasePath(String basePath)


    <T> ResponseContainer<T> httpCall(HttpMethod method, String uri, String requestType, String responseType, Object object, Class<? extends Object> resourceType);

    <T> ResponseContainer<T> httpCall(HttpMethod method, String uri, String requestType, String responseType, Object object, HeadersCollection headersCollection);

    <T> ResponseContainer<T> httpCall(HttpMethod method, String uri, String requestType, String responseType, Object object);

    <T> ResponseContainer<T> httpCall(HttpMethod method, String uri, String requestType, String responseType, Class<? extends Object> resourceType);

    <T> ResponseContainer<T> httpCall(HttpMethod method, String uri, String requestType, String responseType);

    <T> ResponseContainer<T> httpCall(HttpMethod method, String uri, String requestType, String responseType, Object object, Class<? extends Object> resourceType, HeadersCollection headersCollection);

    <T> ResponseContainer<T> httpCall(HttpMethod method, String uri, String requestType, String responseType, Class<? extends Object> resourceType, HeadersCollection headersCollection);

    <T> ResponseContainer<T> getCall(String uri, String requestType, String responseType, Class<? extends Object> resourceType, HeadersCollection headersCollection);

    <T> ResponseContainer<T> postCall(String uri, String requestType, String responseType, Object entity, Class<? extends Object> resourceType, HeadersCollection headersCollection);

    <T> ResponseContainer<T> patchCall(String uri, String requestType, String responseType, Object entity, Class<? extends Object> resourceType, HeadersCollection headersCollection);

    <T> ResponseContainer<T> deleteCall(String uri, String requestType, String responseType, HeadersCollection headersCollection);
}
