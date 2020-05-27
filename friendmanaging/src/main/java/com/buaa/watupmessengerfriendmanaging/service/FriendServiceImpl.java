package com.buaa.watupmessengerfriendmanaging.service;

import com.buaa.watupmessengerfriendmanaging.model.User;
import com.buaa.watupmessengerfriendmanaging.result.BaseResult;
import com.buaa.watupmessengerfriendmanaging.result.FriendResultFactory;
import com.buaa.watupmessengerfriendmanaging.service.MongoRepository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
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
        Optional<User> userOptional = userService.getUserByToken(token);
        Optional<User> friend = userService.getUserById(id);
        if (userOptional.isEmpty()||friend.isEmpty()) {
            return FriendResultFactory.getInstance().produceNotFound();
        }
        User user=userOptional.get();
        if (user.getFriends()==null){
            user.setFriends(new HashMap<>(16));
        }
        if (user.getFriends().containsKey(id)) {
            return FriendResultFactory.getInstance().produceConflict();
        }
        //用于测试而初始化昵称
        user.getFriends().put(id, friend.get().getUsername());
        //friends.put(id,"");
        userRepository.save(user);
        return FriendResultFactory.getInstance().produceSuccess();
    }

    @Override
    public BaseResult addTestFriend(String name1, String name2) {
        Optional<User> user = userService.getUserByUserName(name1);
        Optional<User> friend = userService.getUserByUserName(name2);
        if (user.isEmpty()||friend.isEmpty()) {
            return FriendResultFactory.getInstance().produceNotFound();
        }
        return addFriend(user.get().getToken(), friend.get().getId());
    }

    @Override
    public BaseResult deleteFriend(String token, String id) {
        Optional<User> user = userService.getUserByToken(token);
        Optional<User> friend = userService.getUserById(id);
        if (user.isEmpty()||friend.isEmpty()) {
            return FriendResultFactory.getInstance().produceNotFound();
        }
        Map<String, String> friends = user.get().getFriends();
        if(friends==null||!friends.containsKey(id)){
            return FriendResultFactory.getInstance().produceNotFound();
        }
        friends.remove(id);
        userRepository.save(user.get());
        return FriendResultFactory.getInstance().produceSuccess();
    }

    @Override
    public BaseResult getFriends(String token) {
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
                .collect(Collectors.toList());
        BaseResult result = FriendResultFactory.getInstance().produceSuccess();
        result.setData(data);
        return result;
    }

    @Override
    public BaseResult addBlock(String token, String id) {
        Optional<User> userOptional = userService.getUserByToken(token);
        Optional<User> friend = userService.getUserById(id);
        if (userOptional.isEmpty()||friend.isEmpty()) {
            return FriendResultFactory.getInstance().produceNotFound();
        }
        User user=userOptional.get();
        if (user.getBlocks()==null){
            user.setBlocks(new ArrayList<>());
        }
        if (user.getBlocks().contains(id)) {
            return FriendResultFactory.getInstance().produceConflict();
        }
        user.getBlocks().add(id);
        userRepository.save(user);
        return FriendResultFactory.getInstance().produceSuccess();
    }

    @Override
    public BaseResult addTestBlock(String name1, String name2) {
        Optional<User> user = userService.getUserByUserName(name1);
        Optional<User> friend = userService.getUserByUserName(name2);
        if (user.isEmpty()||friend.isEmpty()) {
            return FriendResultFactory.getInstance().produceNotFound();
        }
        return addBlock(user.get().getToken(), friend.get().getId());
    }

}
