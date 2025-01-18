package com.friendserver.exception;

import lombok.Data;

import java.io.Serializable;


@Data
public class ErrorResponse<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    private int code;
    private String msg;
    private T data;
    public ErrorResponse(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
    public ErrorResponse(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    public ErrorResponse(String msg) {
        this.msg = msg;
    }
    public ErrorResponse(int code,T data) {
        this.code = code;
        this.data = data;
    }
}
