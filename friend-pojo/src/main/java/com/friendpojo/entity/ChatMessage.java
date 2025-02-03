package com.friendpojo.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "chat_message")
public class ChatMessage implements Serializable {


    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer senderId; // 发送者 ID

    @Column(nullable = false)
    private String senderName; // 发送者名称（用于展示）

    @Column(nullable = false)
    private Integer receiverId; // 接收者 ID

    @Column(nullable = false)
    private String content; // 消息内容

    @Column(nullable = false)
    private Date timestamp = new Date(); // 消息时间戳
}