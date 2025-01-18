package com.friendserver.exception;

import com.friendcommon.result.LoginResult;
import com.friendserver.common.BaseResponse;
import com.friendserver.common.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice  //springboot的注解
public class GlobalException {

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse<?> runtimeExceptionHandler(RuntimeException e) {
        return Result.fail(e.getMessage());
    }

    @ExceptionHandler(BusinessException.class)
    public BaseResponse<?> businessExceptionHandler(BusinessException e) {
        return Result.fail(e.getMessage(),e.getCode());
    }
//    @ExceptionHandler(RuntimeException.class)
//    @ResponseBody
//    public
}