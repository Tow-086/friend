package com.friendserver.service;

import com.friendpojo.dto.UserDTO;
import com.friendpojo.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {


    User LoginIn(UserDTO userDTO);


    void Insert(String name, String password);
}
