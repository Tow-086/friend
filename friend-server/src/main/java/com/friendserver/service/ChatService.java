package com.friendserver.service;

import com.friendpojo.entity.ChatMessage;
import java.util.List;

public interface ChatService {
    ChatMessage sendMessage(ChatMessage chatMessage); // 发送消息
    void saveMessage(ChatMessage chatMessage); // 保存消息到MySQL
    void saveMessageToRedis(ChatMessage chatMessage); // 保存消息到Redis
    List<ChatMessage> getHistoryMessages(Integer receiverId); // 从MySQL拉取历史消息
    List<ChatMessage> getUnreadMessages(Integer receiverId); // 从Redis拉取未读消息
    List<ChatMessage> getChatHistory(Integer senderId,Integer receiverId);
}