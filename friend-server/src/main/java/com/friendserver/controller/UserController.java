package com.friendserver.controller;

import com.friendcommon.result.Result;
import com.friendpojo.dto.UserLoginDTO;
import com.friendpojo.dto.UserRegistDTO;
import com.friendpojo.entity.User;
import com.friendpojo.vo.UserLoginVO;
import com.friendpojo.vo.UserRegistVO;
import com.friendserver.service.SendService;
import com.friendserver.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    //将Service注入Web层
    @Autowired
    private UserService userService;
    @Autowired
    private SendService sendService;

    // 实现登录
    @PostMapping("/login")
    public Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO) {
        // 记录登录尝试日志
        log.info("User login attempt: {}", userLoginDTO);
        // 调用服务层的登录方法
        User user = userService.login(userLoginDTO);

        // 构建返回的 UserLoginVO 对象
        UserLoginVO userLoginVO = UserLoginVO.builder()
                .userName(user.getUserName())
                .build();
        // 返回成功结果
        return Result.success(userLoginVO);
    }

    //实现注册
    @PostMapping("/regist")
    public Result<UserRegistVO> regist(@RequestBody UserRegistDTO userRegistDTO){
        //记录注册日志
        log.info("User regist attempt: {}", userRegistDTO);
        //调用服务层的注册方法
        User user = userService.insert(userRegistDTO);

        // 构建返回的 UserRegistVO 对象
        UserRegistVO userRegistVO = UserRegistVO.builder()
                .userName(user.getUserName())
                .build();
        return Result.success(userRegistVO);
    }
    @GetMapping("/sendVerificationCode")
    public ResponseEntity<String> sendVerificationCode(@RequestParam String email) {
        sendService.sendEmail(email);
        return ResponseEntity.ok("验证码已发送");
    }

}