package com.friendserver.service.serviceImpl;

import com.friendpojo.dto.UserLoginDTO;
import com.friendpojo.dto.UserRegistDTO;
import com.friendpojo.entity.User;
import com.friendserver.exception.AccountException; // 添加自定义异常类的导入
import com.friendserver.mapper.UserLoginMapper;
import com.friendserver.service.SendService;
import com.friendserver.service.UserService;
import com.friendcommon.util.VerifyCodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Import(VerifyCodeUtil.class)
public class UserServiceImpl implements UserService {

    @Autowired
    private UserLoginMapper userLoginMapper;
    @Autowired
    private SendService sendService;
    @Autowired
    private VerifyCodeUtil verifyCodeUtil;

    @Override
    public User login(UserLoginDTO userLoginDTO) {
        String userEmail = userLoginDTO.getUserEmail();
        String userName = userLoginDTO.getUserName();
        String userPassword = userLoginDTO.getUserPassword();

        User user = userLoginMapper.getByUserEmail(userEmail);
        log.info("User login attempt: {}", userLoginDTO);

        if (user == null) {
            throw new AccountException("用户不存在");
        }

        if (!user.getUserPassword().equals(userPassword)) {
            throw new AccountException("密码错误");
        }
        return user;
    }

    @Override
    public User insert(UserRegistDTO userRegistDTO) {
        String userName = userRegistDTO.getUserName();
        String userPassword = userRegistDTO.getUserPassword();
        String userEmail = userRegistDTO.getUserEmail();
        String userCode = userRegistDTO.getUserCode();

        User user = userLoginMapper.getByUserEmail(userEmail);
        log.info("User regist attempt: {}", userRegistDTO);

        //发送邮件
//        sendService.sendEmail(userEmail);

        if (user!=null) {
            throw new AccountException("邮箱已存在");
        }
        if (userName == null || userPassword == null || userEmail == null ) {
            throw new AccountException("请填写完整信息");
        }

        int verifyResult = verifyCodeUtil.checkVerifyCode(userEmail, userCode);
        if (verifyResult == 0) {
            throw new AccountException("验证码错误");
        } else if (verifyResult == 2) {
            throw new AccountException("验证码已过期");
        }

        // 插入用户信息
        int rowsAffected = userLoginMapper.insert(userRegistDTO);

        if (rowsAffected > 0) {
            // 插入成功后，可以根据需要查询新插入的用户信息
            // 例如，通过主键查询
            User newUser = userLoginMapper.getByUserEmail(userRegistDTO.getUserEmail());
            return newUser;
        } else {
            throw new RuntimeException("用户注册失败");
        }
    }
}
