// ChatMessageRepository.java
package com.friendserver.repository;

import com.friendpojo.entity.ChatMessage;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    /**
     * 根据接收者ID查询历史消息，按时间戳升序排列
     *
     * @param receiverId 接收者ID
     * @return 历史消息列表
     */
    List<ChatMessage> findByReceiverIdOrderByTimestampAsc(Integer receiverId);

    /**
     * 根据发送者ID和接收者ID查询聊天记录
     *
     * @param senderId   发送者ID
     * @param receiverId 接收者ID
     * @return 聊天记录列表
     */
    List<ChatMessage> findBySenderIdAndReceiverIdOrderByTimestampAsc(Integer senderId, Integer receiverId);

    /**
     * 根据发送者ID和接收者ID查询双方的聊天记录
     *
     * @param senderId   发送者ID
     * @param receiverId 接收者ID
     * @return 双方的聊天记录列表
     */
    @Query("SELECT cm FROM ChatMessage cm WHERE (cm.senderId = :senderId AND cm.receiverId = :receiverId) OR (cm.senderId = :receiverId AND cm.receiverId = :senderId) ORDER BY cm.timestamp ASC")
    List<ChatMessage> findMessagesBetweenUsers(@Param("senderId") Integer senderId, @Param("receiverId") Integer receiverId);
}
