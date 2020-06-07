package com.buaa.watupmessengerfriendmanaging.service.serviceInterface;

import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * @author Cast
 */
public interface FriendService {
    /**
     * 根据关键字查找当前用户的好友，返回好友列表
     *
     * @param id    当前用户token
     * @param username 关键字
     * @return 好友列表
     */
    ResponseEntity<Object> getFriend(String id, String username);


    /**
     * 根据用户token获取好友列表
     *
     * @param token 当前用户token
     * @return 好友列表
     */
    ResponseEntity<Object> getFriends(String id);

    /**
     * 根据用户id发送好友申请
     *
     * @param token  当前用户token
     * @param id     好友id
     * @param remark 备注
     * @return 结果
     */
    ResponseEntity<Object> addFriendRequest(String userId, String friendId, String remark);

    /**
     * 通过指定id的好友申请
     *
     * @param token 当前用户token
     * @param id    好友id
     * @return 结果
     */
    ResponseEntity<Object> passFriendRequest(String userId, String id);

    /**
     * 拒绝指定id的好友申请
     *
     * @param token 当前用户token
     * @param id    好友id
     * @return 结果
     */
    ResponseEntity<Object> rejectFriendRequest(String id);

    /**
     * 根据用户id删除好友,自己也将从对方的好友列表中移除
     *
     * @param token 当前用户token
     * @param id    好友id
     * @return 结果
     */
    ResponseEntity<Object> deleteFriend(String userId, String friendId);


    /**
     * 修改指定id的好友的备注
     *
     * @param token    当前用户token
     * @param id       好友id
     * @param nickname 好友备注
     * @return 结果
     */
    ResponseEntity<Object> modifyFriendNickname(String userId, String friendId, String nickname);

    /**
     * 根据用户id屏蔽好友
     *
     * @param token 当前用户token
     * @param id    好友id
     * @return 结果
     */
    ResponseEntity<Object> blockFriend(String userId, String friendId);
    /**
     * 根据用户id撤销屏蔽
     *
     * @param token 当前用户token
     * @param id    好友id
     * @return 结果
     */
    ResponseEntity<Object> unblockFriend(String userId, String friendId);

    /**
     * 根据用户token获取黑名单
     *
     * @param token 当前用户token
     * @return 结果
     */
    ResponseEntity<Object> getBlocks(String id);
    /**
     * 根据id判断是否是用户的好友
     *
     * @param token 当前用户token
     * @param id    好友id
     * @return 结果
     */
    ResponseEntity<Object> isFriend(String userId, String friendId);
    /**
     * 根据id判断是否已被用户屏蔽
     *
     * @param token 当前用户token
     * @param id    好友id
     * @return 结果
     */
    ResponseEntity<Object> isBlock(String userId, String friendId);

    /**
     * 根据用户token获取好友id列表
     * @param token 当前用户token
     * @return 好友id列表
     */
    List<String> getFriendsSimple(String id);

    /**
     * 根据id判断是否是用户的好友
     * @param id 当前用户id
     * @param friendId 好友id
     * @return 结果
     */
    Boolean isFriendById(String id,String friendId);

    /**
     * 根据id判断是否已被用户屏蔽
     * @param id 当前用户id
     * @param friendId 好友id
     * @return 结果
     */
    Boolean isBlockById(String id,String friendId);

    /**
     * 根据用户token获取好友申请列表
     *
     * @param token 当前用户token
     * @return 结果
     */
    ResponseEntity<Object> getFriendRequest(String id);
}
