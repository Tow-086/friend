package com.friendserver.mapper;

import com.friendpojo.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserLoginMapper {

    @Select("select * from user where user_name = #{userName}")
    User getByUserName(String userName);
}
