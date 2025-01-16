package com.friendpojo.vo;


import lombok.*;

import java.io.Serializable;

@Builder
@AllArgsConstructor
@lombok.ToString
@Data
public class UserVO implements Serializable {
    @Getter
    @Setter
    private String userName;
    private String userPassword;

}

