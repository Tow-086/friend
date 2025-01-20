package com.friendserver.service;

import org.springframework.stereotype.Service;

@Service
public interface SendService {
    // 发送邮件
    String sendEmail(String email);
    // 验证码校验
    String getVerifyCodeFromRedis(String key);
}
