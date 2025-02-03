package com.friendpojo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

@ApiModel(description = "用户登录信息")
@Data
public class UserLoginDTO implements Serializable {

//    private int userId;
    @ApiModelProperty(value = "id")
    private int userId;
    @ApiModelProperty(value = "邮箱")
    private String userEmail;
    @ApiModelProperty(value = "用户名")
    private String userName;
    @ApiModelProperty(value = "密码")
    private String userPassword;

}
