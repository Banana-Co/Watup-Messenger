package com.buaa.watupmessengerfriendmanaging.service.serviceInterface;

import com.buaa.watupmessengerfriendmanaging.model.User;

import java.util.Optional;

/**
 * @author Cast
 * 仅用来测试
 */
public interface UserService {
    Optional<User> getUserByToken(String token);

    Optional<User> getUserById(String id);

    Optional<User> getUserByNickname(String username);

    void addUser(String id, String username);
}
