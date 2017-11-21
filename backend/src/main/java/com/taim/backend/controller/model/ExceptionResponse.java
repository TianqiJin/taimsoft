package com.taim.backend.controller.model;

public class ExceptionResponse {
    private int errorCode;
    private String errorDescription;
    private String errorMessage;
    private int taimErrorCode;
    private String taimErrorMessage;

    public ExceptionResponse(){}

    public ExceptionResponse(int errorCode, String errorDescription, String errorMessage, int taimErrorCode, String taimErrorMessage){
        this.errorCode = errorCode;
        this.errorDescription = errorDescription;
        this.errorMessage = errorMessage;
        this.taimErrorCode = taimErrorCode;
        this.taimErrorMessage = taimErrorMessage;
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

    public int getTaimErrorCode() {
        return taimErrorCode;
    }

    public void setTaimErrorCode(int taimErrorCode) {
        this.taimErrorCode = taimErrorCode;
    }

    public String getTaimErrorMessage() {
        return taimErrorMessage;
    }

    public void setTaimErrorMessage(String taimErrorMessage) {
        this.taimErrorMessage = taimErrorMessage;
    }
}
