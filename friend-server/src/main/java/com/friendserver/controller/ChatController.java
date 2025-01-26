package com.friendserver.controller;

import com.friendcommon.result.Result;
import com.friendpojo.entity.ChatMessage;
import com.friendserver.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @PostMapping("/send")
    public Result<ChatMessage> sendMessage(@RequestBody ChatMessage chatMessage) {
        try {
            ChatMessage savedChatMessage = chatService.sendMessage(chatMessage);
            return Result.success(savedChatMessage);
        } catch (Exception e) {
            // 返回错误信息
            return Result.error("消息发送失败: " + e.getMessage());
        }
    }

    // 获取历史消息
    @GetMapping("/history/{userId}")
    public Result<List<ChatMessage>> getHistoryMessages(@PathVariable Integer userId) {
        List<ChatMessage> messages = chatService.getHistoryMessages(userId);
        return Result.success(messages);
    }

    // 获取未读消息
    @GetMapping("/unread/{userId}")
    public Result<List<ChatMessage>> getUnreadMessages(@PathVariable Integer userId) {
        List<ChatMessage> messages = chatService.getUnreadMessages(userId);
        return Result.success(messages);
    }


    @GetMapping("/history/{senderId}/{receiverId}")
    public Result<List<ChatMessage>> getChatHistory(
            @PathVariable Integer senderId,
            @PathVariable Integer receiverId) {
        List<ChatMessage> messages = chatService.getChatHistory(senderId, receiverId);
        return Result.success(messages);
    }
}