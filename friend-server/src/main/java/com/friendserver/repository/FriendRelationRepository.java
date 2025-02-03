package com.friendserver.repository;

import com.friendpojo.entity.FriendRelation;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FriendRelationRepository extends JpaRepository<FriendRelation, Long> {

    // 根据用户 ID 查找好友关系
//    @Select("SELECT * FROM friend_relation WHERE user_id = #{userId}")
    List<FriendRelation> findByUserId(Integer userId);

    // 根据用户 ID 和好友 ID 查找好友关系
//    @Select("SELECT * FROM friend_relation WHERE user_id = #{userId} AND friend_id = #{friendId}")
    FriendRelation findByUserIdAndFriendId(Integer userId, Integer friendId);

    // 删除好友关系
//    @Select("DELETE FROM friend_relation WHERE user_id = #{userId} AND friend_id = #{friendId}")
    void deleteByUserIdAndFriendId(Integer userId, Integer friendId);
}