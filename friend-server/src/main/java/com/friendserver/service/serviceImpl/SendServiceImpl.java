package com.friendserver.service.serviceImpl;


import com.friendserver.service.SendService;
import com.friendcommon.util.VerifyCodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeUtility;
import java.util.concurrent.TimeUnit;

/**
 * 用户业务实现类
 */
@Service
@Slf4j

public class SendServiceImpl implements SendService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private JavaMailSender javaMailSender;
    @Resource
    private VerifyCodeUtil verifyCodeUtil;


    @Override
    @Async
    public String sendEmail(String email) {
        if (StringUtils.isBlank(email)) {
            throw new RuntimeException("未填写收件人邮箱");
        }
        // 定义Redis的key
        String key = "msg_" + email;

        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        String verifyCode = valueOperations.get(key);
        if (verifyCode == null) {
            // 随机生成一个6位数字型的字符串
            String code = verifyCodeUtil.generateVerifyCode(6);
            // 邮件对象（邮件模板，根据自身业务修改）
            SimpleMailMessage message = new SimpleMailMessage();
            message.setSubject("**游戏注册邮箱验证码");
            message.setText("尊敬的用户您好!\n\n感谢您注册**游戏。\n\n尊敬的: " + email + "您的校验验证码为: " + code + ",有效期5分钟，请不要把验证码信息泄露给其他人,如非本人请勿操作");
            message.setTo(email);

            try {
                message.setFrom("3363009478@qq.com");
                // 对方看到的发送人（发件人的邮箱，根据实际业务进行修改，一般填写的是企业邮箱）
//                message.setFrom(new InternetAddress(MimeUtility.encodeText("**游戏官方") + "<1*********@qq.com>").toString());
                // 发送邮件
                javaMailSender.send(message);
                // 将生成的验证码存入Redis数据库中，并设置过期时间
                valueOperations.set(key, code, 5L, TimeUnit.MINUTES);
                log.info("邮件发送成功");
                return "邮件发送成功";
            } catch (Exception e) {
                log.error("邮件发送出现异常");
                log.error("异常信息为" + e.getMessage());
                log.error("异常堆栈信息为-->");
                return "邮件发送失败";
                //e.printStackTrace();
                //throw new RuntimeException(e);
            }
        } else {
            return "验证码已发送至您的邮箱，请注意查收";
        }
    }

    @Override
    public String getVerifyCodeFromRedis(String key) {
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        return valueOperations.get(key);
    }
}
 
 