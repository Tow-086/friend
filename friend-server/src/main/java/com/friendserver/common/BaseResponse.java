package com.friendserver.common;


import lombok.Data;

import java.io.Serializable;

/**
 * 请求封装
 * @author 奇量
 */
@Data
public class BaseResponse<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    private int code;
    private String msg;
    private T data;
    public BaseResponse(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
    public BaseResponse(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    public BaseResponse(String msg) {
        this.msg = msg;
    }
    public BaseResponse(int code, T data) {
        this.code = code;
        this.data = data;
    }
}
