package com.friendserver.controller;

import com.friendcommon.result.Result;
import com.friendpojo.entity.ChatMessage;
import com.friendserver.repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// ChatController.java
@RestController
@RequestMapping("/chat")
public class ChatController {
    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @GetMapping("/history")
    public Result<List<ChatMessage>> getChatHistory(
            @RequestParam Integer senderId,
            @RequestParam Integer receiverId,
            @RequestParam(defaultValue = "0") Integer offset,
            @RequestParam(defaultValue = "10") Integer limit
    ) {
        // 创建Pageable对象
        Pageable pageable = PageRequest.of(offset / limit, limit);
        // 直接从MySQL通过JPA查询
        List<ChatMessage> messages = chatMessageRepository.findChatHistory(senderId, receiverId, pageable);
        return Result.success(messages);
    }
}
