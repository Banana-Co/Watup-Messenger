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
     * 根据用户id添加好友
     *
     * @param token 当前用户token
     * @param id    好友id
     * @return 结果
     */
    //BaseResult addFriend(String token, String id);

    /**
     * 根据用户id发送好友申请
     * @param token 当前用户token
     * @param id 好友id
     * @param remark 备注
     * @return 结果
     */
    BaseResult addFriendRequest(String token, String id, String remark);
    /**
     * 仅用来测试，根据用户名添加好友
     *
     * @param name1 当前用户名
     * @param name2 好友用户名
     * @return 结果
     */
    //BaseResult addTestFriend(String name1, String name2);

    /**
     * 根据用户id删除好友
     * @param token 当前用户token
     * @param id 好友id
     * @return 结果
     */
    BaseResult deleteFriend(String token,String id);

    /**
     * 根据用户token获取好友列表
     * @param token 当前用户token
     * @return 好友列表
     */
    BaseResult getFriends(String token);

    /**
     * 根据用户id添加黑名单
     * @param token 当前用户token
     * @param id 好友id
     * @return 结果
     */
    BaseResult addBlock(String token, String id);
    /**
     * 仅用来测试，根据用户名添加黑名单
     *
     * @param name1 当前用户名
     * @param name2 好友用户名
     * @return 结果
     */
    BaseResult addTestBlock(String name1, String name2);

    /**
     * 通过指定id的所有好友申请
     * @param token 当前用户token
     * @param id 好友id
     * @return 结果
     */
    BaseResult passFriendRequest(String token,String id);

    /**
     * 拒绝指定id的所有好友申请
     * @param token 当前用户token
     * @param id 好友id
     * @return 结果
     */
    BaseResult rejectFriendRequest(String token,String id);
}
