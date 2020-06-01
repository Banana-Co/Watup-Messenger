package com.buaa.watupmessengerfriendmanaging.service;

import com.buaa.watupmessengerfriendmanaging.model.BaseResult;

/**
 * @author Cast
 */
public interface FriendService {
    /**
     * 根据关键字查找当前用户的好友，返回好友列表
     *
     * @param token    当前用户token
     * @param username 关键字
     * @return 好友列表
     */
    BaseResult getFriend(String token, String username);


    /**
     * 根据用户token获取好友列表
     *
     * @param token 当前用户token
     * @return 好友列表
     */
    BaseResult getFriends(String token);

    /**
     * 根据用户id发送好友申请
     *
     * @param token  当前用户token
     * @param id     好友id
     * @param remark 备注
     * @return 结果
     */
    BaseResult addFriendRequest(String token, String id, String remark);

    /**
     * 通过指定id的所有好友申请
     *
     * @param token 当前用户token
     * @param id    好友id
     * @return 结果
     */
    BaseResult passFriendRequest(String token, String id);

    /**
     * 拒绝指定id的所有好友申请
     *
     * @param token 当前用户token
     * @param id    好友id
     * @return 结果
     */
    BaseResult rejectFriendRequest(String token, String id);

    /**
     * 根据用户id删除好友,自己也将从对方的好友列表中移除
     *
     * @param token 当前用户token
     * @param id    好友id
     * @return 结果
     */
    BaseResult deleteFriend(String token, String id);


    /**
     * 修改指定id的好友的备注
     *
     * @param token    当前用户token
     * @param id       好友id
     * @param nickname 好友备注
     * @return 结果
     */
    BaseResult modifyFriendNickname(String token, String id, String nickname);

    /**
     * 根据用户id屏蔽好友
     *
     * @param token 当前用户token
     * @param id    好友id
     * @return 结果
     */
    BaseResult blockFriend(String token, String id);
    /**
     * 根据用户id撤销屏蔽
     *
     * @param token 当前用户token
     * @param id    好友id
     * @return 结果
     */
    BaseResult unblockFriend(String token, String id);

    /**
     * 根据用户token获取黑名单
     *
     * @param token 当前用户token
     * @return 结果
     */
    BaseResult getBlocks(String token);
    /**
     * 根据id判断是否是用户的好友
     *
     * @param token 当前用户token
     * @param id    好友id
     * @return 结果
     */
    BaseResult isFriend(String token, String id);
    /**
     * 根据id判断是否已被用户屏蔽
     *
     * @param token 当前用户token
     * @param id    好友id
     * @return 结果
     */
    BaseResult isBlock(String token, String id);
}
