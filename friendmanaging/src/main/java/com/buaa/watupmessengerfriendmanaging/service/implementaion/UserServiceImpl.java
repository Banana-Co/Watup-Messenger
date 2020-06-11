package com.buaa.watupmessengerfriendmanaging.service.implementaion;

import com.buaa.watupmessengerfriendmanaging.model.Friend;
import com.buaa.watupmessengerfriendmanaging.model.User;
import com.buaa.watupmessengerfriendmanaging.model.factory.ResponseEntityFactory;
import com.buaa.watupmessengerfriendmanaging.service.serviceInterface.FriendService;
import com.buaa.watupmessengerfriendmanaging.service.serviceInterface.UserService;
import com.buaa.watupmessengerfriendmanaging.service.mongo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Cast
 *
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    FriendService friendService;
    @Autowired
    RedisTemplate<String,String> redisTemplate;
    @Override
    public Optional<User> getUserByToken(String token) {
        return getUserById(redisTemplate.opsForValue().get(token));
    }

    @Override
    public Optional<User> getUserById(String id) {
        return userRepository.getById(id);
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        return userRepository.getByUsername(username);
    }

    @Override
    public ResponseEntity<?> getFriend(String id) {
        Friend friend=friendService
                .friendByUser(userRepository
                        .getById(id)
                        .orElse(new User()));
        return ResponseEntityFactory
                .getInstance()
                .produceSuccess(friend);
    }

    @Override
    public void addUser(String id,String username) {
        User user=new User();
        user.setId(id);
        user.setUsername(username);
        user.setCreatedDate(LocalDateTime.now());
        userRepository.save(user);
    }


}
