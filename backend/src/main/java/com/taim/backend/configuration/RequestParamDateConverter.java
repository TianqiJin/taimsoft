package com.taim.backend.configuration;

import com.taim.model.search.TransactionSearch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class RequestParamDateConverter extends PropertyEditorSupport {
    private static final Logger logger = LoggerFactory.getLogger(RequestParamDateConverter.class);

    @Override
    public void setAsText(final String text){

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(TransactionSearch.DATE_PATTERN);
        try {
            setValue(simpleDateFormat.parse(text));
        } catch (ParseException e) {
            logger.error(e.getMessage(), e);
        }
    }
}
