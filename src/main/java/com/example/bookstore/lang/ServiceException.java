package com.example.bookstore.lang;

public class ServiceException extends RuntimeException {
    private final String errorCode;

    public ServiceException(String errorCode, String msg, Exception e) {
        super(msg, e);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
