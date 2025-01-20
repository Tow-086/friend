package com.friendserver.service;

import com.friendpojo.dto.UserLoginDTO;
import com.friendpojo.dto.UserRegistDTO;
import com.friendpojo.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {


    User login(UserLoginDTO userLoginDTO);


    User insert(UserRegistDTO userRegistDTO);
}
