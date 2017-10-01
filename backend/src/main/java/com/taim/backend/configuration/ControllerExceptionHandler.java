package com.taim.backend.configuration;

import com.taim.backend.controller.model.ExceptionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class ControllerExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    @ControllerAdvice
    @Order(Ordered.LOWEST_PRECEDENCE)
    public static class GenericExceptionHandler{
        @ExceptionHandler(Exception.class)
        public ResponseEntity<ExceptionResponse> handleGenericException(Exception ex){
            return exceptionResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
    }

    private static ResponseEntity<ExceptionResponse> exceptionResponse(Throwable throwable, HttpStatus httpStatus, String message){
        if(throwable != null){
            logger.error("Error caught: " + message, throwable);
            return response(new ExceptionResponse(httpStatus.value(), httpStatus.getReasonPhrase(), message), httpStatus);
        }else{
            logger.error("Unknown error caught in RESTController, {}", httpStatus);
            return response(null, httpStatus);
        }
    }

    private static <T> ResponseEntity<T> response(T body, HttpStatus httpStatus){
        logger.debug("Responding with a status of {}", httpStatus);
        return new ResponseEntity<T>(body, httpStatus);
    }
}
