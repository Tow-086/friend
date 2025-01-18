package com.friendserver.common;

/**
 * @author 14706
 */
public class Result {
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(200,data);
    }
    public static <T> BaseResponse<T> success(String msg,T data) {
        return new BaseResponse<>(200,msg,data);
    }
    public static <T> BaseResponse<T> fail(String msg) {
        return new BaseResponse<>(500,msg);
    }
    public static <T> BaseResponse<T> fail(String msg,int code) {
        return new BaseResponse<>(code,msg);
    }
}
