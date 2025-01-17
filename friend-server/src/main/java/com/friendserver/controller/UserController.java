package com.friendserver.controller;

import com.friendcommon.result.Result;
import com.friendpojo.dto.UserDTO;
import com.friendpojo.entity.User;
import com.friendpojo.vo.UserVO;
import com.friendserver.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    //将Service注入Web层
    @Autowired
    private UserService userService;

    //实现登录
    @PostMapping("/login")
    public Result<UserVO> login(@RequestBody UserDTO userDTO){
        log.info("用户登录：{}", userDTO);
        User user = userService.LoginIn(userDTO);

        UserVO userVO = UserVO.builder()
                .userName(user.getUserName())
                .userPassword(user.getUserPassword())
                .build();
        return Result.success(userVO);
    }


}