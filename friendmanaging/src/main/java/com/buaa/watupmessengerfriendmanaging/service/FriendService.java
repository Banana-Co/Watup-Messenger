package com.buaa.watupmessengerfriendmanaging.service;

import com.buaa.watupmessengerfriendmanaging.result.BaseResult;

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
    BaseResult addFriend(String token, String id);

    /**
     * 仅用来测试，根据用户名添加好友
     *
     * @param name1 当前用户名
     * @param name2 好友用户名
     * @return 结果
     */
    BaseResult addTestFriend(String name1, String name2);
}
