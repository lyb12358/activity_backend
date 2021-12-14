package com.beyond.utils;

public class NormalException extends RuntimeException {
    // 异常信息
    private String message;

    private static final long serialVersionUID = 1L;

    public NormalException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
