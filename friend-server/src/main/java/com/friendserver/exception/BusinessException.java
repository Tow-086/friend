package com.friendserver.exception;

/**
 * @author 14706
 */
public class BusinessException extends RuntimeException {
    //    private final String message;
    private final int code;
    public BusinessException(String message, int code) {
        super(message);
        this.code = code;
    }
    public BusinessException(String message, Throwable cause, int code) {
        super(message, cause);
        this.code = code;
    }
    public BusinessException(Throwable cause, int code) {
        super(cause);
        this.code = code;
    }
    public Integer getCode() {
        return code;
    }
}
