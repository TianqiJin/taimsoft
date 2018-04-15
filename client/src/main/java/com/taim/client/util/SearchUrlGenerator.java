package com.taim.client.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SearchUrlGenerator<T> {
    private static final Logger logger = LoggerFactory.getLogger(SearchUrlGenerator.class);

    public static<T> String generate(T t){
        StringBuilder sb = new StringBuilder();
        for(Field field: t.getClass().getDeclaredFields()){
            field.setAccessible(true);
            try {
                String value;
                if(field.getType().isAssignableFrom(Date.class)){
                    value = new SimpleDateFormat("yyyyMMdd").format(field.get(t));
                }else{
                    value = String.valueOf(field.get(t));
                }
                sb.append(field.getName()).append("=").append(value).append("&");
            } catch (IllegalAccessException e) {
                logger.error(e.getMessage(), e);
            }
        }
        sb.setLength(sb.length() - 1);

        return sb.toString();
    }
}
