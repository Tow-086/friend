package com.friendserver.controller;

import com.friendcommon.result.Result;
import com.friendpojo.entity.ChatMessage;
import com.friendpojo.entity.FriendRelation;
import com.friendserver.repository.FriendRelationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/friend")
public class FriendController {

    @Autowired
    private FriendRelationRepository friendRelationRepository;
    @Resource
    private RedisTemplate<String, ChatMessage> chatMessageRedisTemplate;

    @PostMapping("/add")
    public Result<String> addFriend(@RequestParam Integer userId, @RequestParam Integer friendId) {
        // 检查是否已经是好友
        FriendRelation relation = friendRelationRepository.findByUserIdAndFriendId(userId, friendId);
        if (relation != null) {
            return Result.error("Already friends");
        }

        // 创建双向好友关系
        FriendRelation newRelation1 = new FriendRelation();
        newRelation1.setUserId(userId);
        newRelation1.setFriendId(friendId);
        newRelation1.setStatus("accepted");
        friendRelationRepository.save(newRelation1);

        FriendRelation newRelation2 = new FriendRelation();
        newRelation2.setUserId(friendId);
        newRelation2.setFriendId(userId);
        newRelation2.setStatus("accepted");
        friendRelationRepository.save(newRelation2);

        return Result.success("Friend added successfully");
    }

    @DeleteMapping("/delete")
    public String deleteFriend(@RequestParam Integer userId, @RequestParam Integer friendId) {
        // 删除好友关系
        friendRelationRepository.deleteByUserIdAndFriendId(userId, friendId);
        return "Friend deleted successfully";
    }

    @GetMapping("/list")
    public List<FriendRelation> getFriendList(@RequestParam Integer userId) {
        return friendRelationRepository.findByUserId(userId);
    }

    @GetMapping("/history")
    public List<ChatMessage> getChatHistory(@RequestParam Integer senderId, @RequestParam Integer receiverId) {
        String key = "chat:" + senderId + ":" + receiverId;
        return chatMessageRedisTemplate.opsForList().range(key, 0, -1);
    }
}