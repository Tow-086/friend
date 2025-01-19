package com.friendserver.service.serviceImpl;

import com.friendpojo.dto.UserDTO;
import com.friendpojo.entity.User;
import com.friendserver.exception.AccountException; // 添加导入
import com.friendserver.mapper.UserLoginMapper;
import com.friendserver.service.UserService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountNotFoundException;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserLoginMapper userLoginMapper;

    @Override
    public User login(UserDTO userDTO) {
        String userName = userDTO.getUserName();
        String userPassword = userDTO.getUserPassword();

        User user = userLoginMapper.getByUserName(userName);
        log.info("用户登录：{}", userDTO);

        if (user == null) {
            throw new AccountException("用户名不存在"); // 修改为 AccountException
        }

        if (!user.getUserPassword().equals(userPassword)) {
            throw new AccountException("密码错误"); // 修改为 AccountException
        }
        return user;
    }

    @Override
    public void Insert(String name, String password) {

    }
}