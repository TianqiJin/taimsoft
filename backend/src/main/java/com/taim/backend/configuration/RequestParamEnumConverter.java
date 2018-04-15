package com.taim.backend.configuration;

import java.beans.PropertyEditorSupport;

public class RequestParamEnumConverter<T extends Enum<T>> extends PropertyEditorSupport {
    private final Class<T> typeParameterClass;

    public RequestParamEnumConverter(Class<T> typeParameterClass){
        super();
        this.typeParameterClass = typeParameterClass;
    }

    @Override
    public void setAsText(final String text){
        for(T t: typeParameterClass.getEnumConstants()){
            if(t.name().equalsIgnoreCase(text)){
                setValue(T.valueOf(typeParameterClass, t.name()));
            }
        }
    }
}
