package com.friendserver.controller;


import com.friendcommon.result.LoginResult;
import com.friendserver.common.BaseResponse;
import com.friendserver.common.Result;
import com.friendserver.exception.BusinessException;
import com.friendserver.exception.ErrorResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value ="/test")
public class TestController {
    @PostMapping("/test")
    public BaseResponse<String> test(String name) {
        if (name == null) {
            throw new BusinessException("用户名不能为空", 404);
        }
        if (name.equals("李四")) {
            throw new RuntimeException("用户名错误");
        }
        if (!name.equals("张三")) {
            throw new BusinessException("用户非张三", 400);
        }
        return Result.success("张三");
    }
}
