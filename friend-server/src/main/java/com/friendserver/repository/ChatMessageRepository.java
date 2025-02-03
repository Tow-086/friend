package com.friendserver.repository;

import com.friendpojo.entity.ChatMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

// ChatMessageRepository.java
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    // 查询双方历史消息（按时间倒序），支持分页
    @Query("SELECT cm FROM ChatMessage cm " +
            "WHERE (cm.senderId = :senderId AND cm.receiverId = :receiverId) " +
            "OR (cm.senderId = :receiverId AND cm.receiverId = :senderId) " +
            "ORDER BY cm.timestamp DESC")
    List<ChatMessage> findChatHistory(
            @Param("senderId") Integer senderId,
            @Param("receiverId") Integer receiverId,
            Pageable pageable
    );

}
