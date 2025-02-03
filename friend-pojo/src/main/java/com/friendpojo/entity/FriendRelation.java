package com.friendpojo.entity;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name = "friend_relation")
public class FriendRelation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer userId; // 用户 ID

    @Column(nullable = false)
    private Integer friendId; // 好友 ID

    @Column(nullable = false)
    private String status; // 好友关系状态（如：pending, accepted）
}