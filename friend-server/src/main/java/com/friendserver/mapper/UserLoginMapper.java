package com.friendserver.mapper;

import com.friendpojo.dto.UserRegistDTO;
import com.friendpojo.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserLoginMapper {

    @Select("select * from user where user_name = #{userName}")
    User getByUserName(String userName);
    @Select("select * from user where user_email = #{userEmail}")
    User getByUserEmail(String userEmail);
    @Insert("insert into user(user_name,user_password,user_email) values(#{userName},#{userPassword},#{userEmail})")
    int insert(UserRegistDTO userRegistDTO);
}
