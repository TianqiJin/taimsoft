package com.taim.backend.configuration;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import com.taim.backend.controller.model.ExceptionResponse;
import com.taim.backend.controller.model.TaimErrorCodes;
import org.hibernate.StaleObjectStateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.hibernate4.HibernateOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class ControllerExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    @ControllerAdvice
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public static class DetailedExceptionHandler{

        @ExceptionHandler(DataIntegrityViolationException.class)
        public ResponseEntity<ExceptionResponse> handleDataIntegrityViolationException
                (DataIntegrityViolationException ex){
            return exceptionResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR, ex.getRootCause().getMessage(),
                    TaimErrorCodes.DB_SAVE_ERROR.getCode(), TaimErrorCodes.DB_SAVE_ERROR.getMessage());
        }

        @ExceptionHandler({StaleObjectStateException.class, HibernateOptimisticLockingFailureException.class})
        public ResponseEntity<ExceptionResponse> handleStaleObjectSateException
                (Exception ex){
            return exceptionResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(),
                    TaimErrorCodes.DB_OPTIMISTIC_LOCKING_ERROR.getCode(), TaimErrorCodes.DB_OPTIMISTIC_LOCKING_ERROR.getMessage());
        }
    }


    @ControllerAdvice
    @Order(Ordered.LOWEST_PRECEDENCE)
    public static class GenericExceptionHandler{
        @ExceptionHandler(Exception.class)

        public ResponseEntity<ExceptionResponse> handleGenericException(Exception ex){
            return exceptionResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(),
                    TaimErrorCodes.GENERAL_ERROR.getCode(), TaimErrorCodes.GENERAL_ERROR.getMessage());
        }
    }

    private static ResponseEntity<ExceptionResponse> exceptionResponse(Throwable throwable, HttpStatus httpStatus,
                                                                       String message, int taimErrorCode, String taimErrorMessage){
        if(throwable != null){
            logger.error("Error caught: " + message, throwable);
            return response(
                    new ExceptionResponse(httpStatus.value(), httpStatus.getReasonPhrase(), message, taimErrorCode, taimErrorMessage),
                    httpStatus);
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
