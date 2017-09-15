package com.taim.client.util;

import org.dozer.DozerBeanMapper;

public class BeanMapper<T>{
    private static final DozerBeanMapper mapper;

    static {
        mapper = new DozerBeanMapper();
    }

    public synchronized static<T, K> T map(Object obj, Class<T> aClass){
        return mapper.map(obj, aClass);
    }
}
