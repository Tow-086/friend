package com.friendpojo.vo;

import lombok.*;

@Builder
@AllArgsConstructor
@lombok.ToString
@Data
public class UserRegistVO {
    @Getter
    @Setter
    private String userName;
}
