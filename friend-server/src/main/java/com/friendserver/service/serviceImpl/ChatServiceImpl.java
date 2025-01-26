package com.friendserver.service.serviceImpl;

import com.friendpojo.entity.ChatMessage;
import com.friendserver.repository.ChatMessageRepository;
import com.friendserver.service.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
@Slf4j
@Service
public class ChatServiceImpl implements ChatService {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    @Qualifier("chatMessageRedisTemplate")
    private RedisTemplate<String, ChatMessage> redisTemplate;

    private static final String UNREAD_MESSAGES_KEY = "unread_messages:"; // Redis中未读消息的键前缀

    @Override
    public void saveMessage(ChatMessage chatMessage) {
        // 保存消息到MySQL
        chatMessageRepository.save(chatMessage);
    }

    @Override
    public ChatMessage sendMessage(ChatMessage chatMessage) {
        // 设置消息的时间戳
        chatMessage.setTimestamp(new Date());

        // 保存消息到MySQL
        ChatMessage savedMessage = chatMessageRepository.save(chatMessage);

        // 保存消息到Redis
        String key = UNREAD_MESSAGES_KEY + chatMessage.getReceiverId();
        redisTemplate.opsForList().rightPush(key, savedMessage);

        return savedMessage;
    }

    @Override
    public void saveMessageToRedis(ChatMessage chatMessage) {
        String key = UNREAD_MESSAGES_KEY + chatMessage.getReceiverId();
        redisTemplate.opsForList().rightPush(key, chatMessage); // 存储到Redis
        log.info("消息已保存到Redis: {}", chatMessage);
    }

    @Override
    public List<ChatMessage> getHistoryMessages(Integer receiverId) {
        // 从MySQL拉取历史消息
        return chatMessageRepository.findByReceiverIdOrderByTimestampAsc(receiverId);
    }

    @Override
    public List<ChatMessage> getUnreadMessages(Integer receiverId) {
        // 从Redis拉取未读消息
        String key = UNREAD_MESSAGES_KEY + receiverId;
        List<ChatMessage> unreadMessages = redisTemplate.opsForList().range(key, 0, -1);
        redisTemplate.delete(key); // 拉取后清除未读消息
        return unreadMessages;
    }
    @Override
    public List<ChatMessage> getChatHistory(Integer senderId, Integer receiverId) {
        return chatMessageRepository.findMessagesBetweenUsers(senderId, receiverId);
    }
}