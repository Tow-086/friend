package com.friendpojo.entity;

import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@Getter
@Setter
@lombok.ToString
@NoArgsConstructor
@Data
public class User implements Serializable {
    @Getter
    @Setter
    private int userId;
    private String userName;
    private String userPassword;

}
