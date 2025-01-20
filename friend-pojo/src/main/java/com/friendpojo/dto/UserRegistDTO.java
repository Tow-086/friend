package com.friendpojo.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

@ApiModel(description = "用户注册信息")
@Data
public class UserRegistDTO implements Serializable {

    //    private int userId;

    @ApiModelProperty(value = "邮箱")
    private String userEmail;
    @ApiModelProperty(value = "用户名")
    private String userName;
    @ApiModelProperty(value = "密码")
    private String userPassword;
    @ApiModelProperty(value = "验证码")
    private String userCode;


}

