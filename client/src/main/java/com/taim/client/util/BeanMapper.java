package com.taim.client.util;

import org.dozer.DozerBeanMapper;
import org.dozer.DozerBeanMapperBuilder;
import org.dozer.Mapper;

import java.util.Arrays;
import java.util.Collections;

public class BeanMapper<T>{
    private static Mapper mapper;

    static {
        mapper = DozerBeanMapperBuilder.create()
                .withMappingFiles("dozermapping.xml")
                .build();
    }

    public synchronized static<T, K> T map(Object obj, Class<T> aClass){
        return mapper.map(obj, aClass);
    }
}
