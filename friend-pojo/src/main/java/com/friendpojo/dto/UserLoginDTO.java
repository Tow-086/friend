package com.friendpojo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

@ApiModel(description = "用户登录信息")
@Data
public class UserLoginDTO implements Serializable {

//    private int userId;
    @ApiModelProperty(value = "邮箱")
    private String userEmail;
    @ApiModelProperty(value = "密码")
    private String userPassword;

}
