package com.taim.backend.controller.model;

public enum TaimErrorCodes {
    GENERAL_ERROR(0, "Something went wrong. Please try again later"),
    DB_OPTIMISTIC_LOCKING_ERROR(1, "Unable to update database because it was updated by another transaction"),
    DB_SAVE_ERROR(2, "Failed to update database since it violates database integrity");

    private int code;
    private String message;

    TaimErrorCodes(int code, String message){
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
