package com.friendserver.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.friendpojo.entity.ChatMessage;
import com.friendserver.repository.ChatMessageRepository;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.logging.Logger;
import org.mybatis.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Map<Integer, WebSocketSession> sessions = new HashMap<>(); // 存储用户会话
    private static final Logger logger = LoggerFactory.getLogger(ChatWebSocketHandler.class); // 初始化 logger

    @Autowired
    private ChatMessageRepository chatMessageRepository; // JPA Repository

    @Autowired
    private RedisTemplate<String, ChatMessage> redisTemplate; // Redis操作模板

    private static final String UNREAD_MESSAGES_KEY = "unread_messages:"; // Redis中未读消息的Key前缀

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String userIdParam = session.getUri().getQuery();
        if (userIdParam == null || !userIdParam.startsWith("userId=")) {
            session.close(CloseStatus.BAD_DATA);
            return;
        }
        String userIdStr = userIdParam.split("=")[1];
        try {
            Integer userId = Integer.parseInt(userIdStr);
            sessions.put(userId, session); // 存储用户会话
            log.info("WebSocket连接已建立，用户ID: {}", userId);
        } catch (NumberFormatException e) {
            session.close(CloseStatus.BAD_DATA);
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // 获取消息内容
        String payload = message.getPayload();
        Map<String, Object> messageMap = objectMapper.readValue(payload, Map.class);

        // 根据消息类型处理
        String type = (String) messageMap.get("type");
        if (type == null) {
            // 如果消息类型为空，关闭连接
            session.close(CloseStatus.BAD_DATA);
            return;
        }

        switch (type) {
            case "login":
                // 处理登录消息
                Integer userId = (Integer) messageMap.get("userId");
                if (userId != null) {
                    sessions.put(userId, session); // 将用户ID和会话关联
                }
                break;
            case "chat":
                // 处理聊天消息
                ChatMessage chatMessage = new ChatMessage();
                chatMessage.setSenderId((Integer) messageMap.get("senderId"));
                chatMessage.setSenderName((String) messageMap.get("senderName")); // 设置senderName
                chatMessage.setReceiverId((Integer) messageMap.get("receiverId"));
                chatMessage.setContent((String) messageMap.get("content"));
                chatMessage.setTimestamp(new Date()); // 设置时间戳

                // 保存消息到MySQL
                ChatMessage savedMessage = chatMessageRepository.save(chatMessage);
                log.info("消息已保存到MySQL，消息ID: {}", savedMessage.getId());

                // 保存消息到Redis
                String key = UNREAD_MESSAGES_KEY + chatMessage.getReceiverId();
                redisTemplate.opsForList().rightPush(key, savedMessage);
                log.info("消息已保存到Redis，Key: {}", key);

                // 将消息发送给目标用户
                WebSocketSession targetSession = sessions.get(chatMessage.getReceiverId());
                if (targetSession != null && targetSession.isOpen()) {
                    targetSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(chatMessage)));
                } else {
                    log.info("用户 {} 当前不在线，消息已存储为未读消息", chatMessage.getReceiverId());
                }
                break;
            default:
                // 未知消息类型，关闭连接
                session.close(CloseStatus.BAD_DATA);
                break;
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // 从URL参数中获取用户ID
        String query = session.getUri().getQuery(); // 获取URL参数
        if (query == null || !query.startsWith("userId=")) {
            return;
        }

        // 提取userId并进行更严格的检查
        String[] params = query.split("=");
        if (params.length < 2 || params[1].isEmpty()) {
            // 如果userId为空或格式不对，直接返回
            return;
        }

        String userIdStr = params[1];
        try {
            int userId = Integer.parseInt(userIdStr); // 尝试转换为整数
            // 根据userId执行相应的逻辑，例如移除会话
            sessions.remove(userId);
        } catch (NumberFormatException e) {
            // 处理转换失败的情况，可以记录日志或忽略
            logger.warn(() -> "Invalid user ID format: " + userIdStr);
        }
    }
}