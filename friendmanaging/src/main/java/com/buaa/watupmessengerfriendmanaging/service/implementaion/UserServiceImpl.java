package com.buaa.watupmessengerfriendmanaging.service.implementaion;

import com.buaa.watupmessengerfriendmanaging.exception.UserNotFoundException;
import com.buaa.watupmessengerfriendmanaging.model.Friend;
import com.buaa.watupmessengerfriendmanaging.model.User;
import com.buaa.watupmessengerfriendmanaging.model.factory.ResponseEntityFactory;
import com.buaa.watupmessengerfriendmanaging.service.mongo.repository.UserRepository;
import com.buaa.watupmessengerfriendmanaging.service.serviceInterface.FriendService;
import com.buaa.watupmessengerfriendmanaging.service.serviceInterface.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

/**
 * @author Cast
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    FriendService friendService;
    @Autowired
    UserService userService;
    @Autowired
    RedisTemplate<String, String> redisTemplate;

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
    public ResponseEntity<?> getFriend(String userId, String friendId) {
        Optional<User> user = userService.getUserById(userId);
        Optional<User> friend = userService.getUserById(friendId);
        if (user.isEmpty() || friend.isEmpty()) {
            throw new UserNotFoundException();
        }
        Friend data = friendService.friendByUser(friend.get());
        Map<String, String> friends = user
                .get()
                .getFriends();
        if (friends != null && friends.containsKey(friendId) && friends.get(friendId) != null) {
            data.setNickname(friends.get(friendId));
        }
        return ResponseEntityFactory
                .getInstance()
                .produceSuccess(data);
    }

    @Override
    public void addUser(String id, String username) {
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setCreatedDate(LocalDateTime.now());
        userRepository.save(user);
    }


}
