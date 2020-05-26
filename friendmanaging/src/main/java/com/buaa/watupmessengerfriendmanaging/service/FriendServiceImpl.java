package com.buaa.watupmessengerfriendmanaging.service;

import com.buaa.watupmessengerfriendmanaging.model.User;
import com.buaa.watupmessengerfriendmanaging.result.BaseResult;
import com.buaa.watupmessengerfriendmanaging.result.FriendResultFactory;
import com.buaa.watupmessengerfriendmanaging.service.MongoRepository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Cast
 */
@Service
public class FriendServiceImpl implements FriendService {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @Override
    public BaseResult getFriend(String token, String username) {
        Optional<User> user = userService.getUserByToken(token);
        if (user.isEmpty()) {
            return FriendResultFactory.getInstance().produceNotFound();
        }
        List<User> data = user
                .get()
                .getFriends()
                .keySet()
                .stream()
                .map(u -> userService
                        .getUserById(u)
                        .orElse(new User()))
                .filter(u -> u.getUsername().contains(username))
                .collect(Collectors.toList());
        BaseResult result = FriendResultFactory.getInstance().produceSuccess();
        result.setData(data);
        return result;
    }

    @Override
    public BaseResult addFriend(String token, String id) {
        Optional<User> user = userService.getUserByToken(token);
        Optional<User> friend = userService.getUserById(id);
        if (user.isEmpty()) {
            return FriendResultFactory.getInstance().produceNotFound();
        }
        if (friend.isEmpty()) {
            return FriendResultFactory.getInstance().produceNotFound();
        }
        Map<String, String> friends = Optional
                .ofNullable(user.get().getFriends())
                .orElse(new HashMap<>(16));
        if (friends.containsKey(id)) {
            return FriendResultFactory.getInstance().produceConflict();
        }
        //用于测试而初始化昵称
        friends.put(id, friend.get().getUsername());
        //friends.put(id,"");
        userRepository.save(user.get());
        return FriendResultFactory.getInstance().produceSuccess();
    }

    @Override
    public BaseResult addTestFriend(String name1, String name2) {
        Optional<User> user = userService.getUserByUserName(name1);
        Optional<User> friend = userService.getUserByUserName(name2);
        if (user.isEmpty()) {
            return FriendResultFactory.getInstance().produceNotFound();
        }
        if (friend.isEmpty()) {
            return FriendResultFactory.getInstance().produceNotFound();
        }
        return addFriend(user.get().getToken(), friend.get().getId());
    }


}
