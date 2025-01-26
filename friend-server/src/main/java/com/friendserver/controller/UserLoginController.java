package com.friendserver.controller;

import com.friendcommon.result.Result;
import com.friendpojo.dto.UserLoginDTO;
import com.friendpojo.dto.UserRegistDTO;
import com.friendpojo.entity.User;
import com.friendpojo.vo.UserLoginVO;
import com.friendpojo.vo.UserRegistVO;
import com.friendserver.service.SendRegistEmailService;
import com.friendserver.service.UserLoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserLoginController {

    //将Service注入Web层
    @Autowired
    private UserLoginService userLoginService;
    @Autowired
    private SendRegistEmailService sendRegistEmailService;

    // 实现登录
    @PostMapping("/login")
    public Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO) {
        // 记录登录尝试日志
        log.info("User login attempt: {}", userLoginDTO);
        // 调用服务层的登录方法
        User user = userLoginService.login(userLoginDTO);

        // 构建返回的 UserLoginVO 对象
        UserLoginVO userLoginVO = UserLoginVO.builder()
                .userId(user.getUserId())
                .userName(user.getUserName())
                .userEmail(user.getUserEmail())
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
        User user = userLoginService.insert(userRegistDTO);

        // 构建返回的 UserRegistVO 对象
        UserRegistVO userRegistVO = UserRegistVO.builder()
                .userName(user.getUserName())
                .build();
        return Result.success(userRegistVO);
    }
    @GetMapping("/sendVerificationCode")
    public ResponseEntity<String> sendVerificationCode(@RequestParam String email) {
        sendRegistEmailService.sendEmail(email);
        return ResponseEntity.ok("验证码已发送");
    }

}