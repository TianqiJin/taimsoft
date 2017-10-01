package com.taim.backend.controller.model;

public class ExceptionResponse {
    private int errorCode;
    private String errorDescription;
    private String errorMessage;

    public ExceptionResponse(){}

    public ExceptionResponse(int errorCode, String errorDescription, String errorMessage){
        this.errorCode = errorCode;
        this.errorDescription = errorDescription;
        this.errorMessage = errorMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
