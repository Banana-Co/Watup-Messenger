package com.buaa.watupmessengerfriendmanaging.service;

import com.buaa.watupmessengerfriendmanaging.model.User;
import com.buaa.watupmessengerfriendmanaging.service.mongo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author Cast
 * 逻辑有很多漏洞，仅用来测试
 */
@Service
public class UserServiceImpl implements UserService{
    @Autowired
    UserRepository userRepository;
    @Override
    public Optional<User> getUserByToken(String token) {
        return userRepository.getByToken(token);
    }

    @Override
    public Optional<User> getUserById(String id) {
        return userRepository.getById(id);
    }

    @Override
    public Optional<User> getUserByUserName(String username) {
        return userRepository.getByUsername(username);
    }

    @Override
    public void addUser(String token,String username) {
        User user=new User();
        user.setToken(token);
        user.setUsername(username);
        user.setCreatedDate(LocalDateTime.now());
        userRepository.save(user);
    }
}
