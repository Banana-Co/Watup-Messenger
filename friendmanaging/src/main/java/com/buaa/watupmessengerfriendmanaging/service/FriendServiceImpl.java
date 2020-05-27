package com.buaa.watupmessengerfriendmanaging.service;

import com.buaa.watupmessengerfriendmanaging.factory.FriendRequestFactory;
import com.buaa.watupmessengerfriendmanaging.model.FriendRequest;
import com.buaa.watupmessengerfriendmanaging.model.User;
import com.buaa.watupmessengerfriendmanaging.model.BaseResult;
import com.buaa.watupmessengerfriendmanaging.factory.FriendResultFactory;
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
    public BaseResult deleteFriend(String token, String id) {
        Optional<User> userOptional = userService.getUserByToken(token);
        Optional<User> friendOptional = userService.getUserById(id);
        if (userOptional.isEmpty()||friendOptional.isEmpty()) {
            return FriendResultFactory.getInstance().produceNotFound();
        }
        User user=userOptional.get();
        User friend=friendOptional.get();
        if (!deleteFriend(user,friend.getId())||!deleteFriend(friend,user.getId())){
            return FriendResultFactory.getInstance().produceNotFound();
        }
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
    @Override
    public BaseResult addFriendRequest(String token, String id, String remark) {
        Optional<User> userOptional = userService.getUserByToken(token);
        Optional<User> friendOptional = userService.getUserById(id);
        if (userOptional.isEmpty()||friendOptional.isEmpty()) {
            return FriendResultFactory.getInstance().produceNotFound();
        }
        User user=userOptional.get();
        User friend=friendOptional.get();
        if(friend.getFriendRequestList()==null){
            friend.setFriendRequestList(new ArrayList<>());
        }
        friend.getFriendRequestList().add(FriendRequestFactory.produce(user.getId(),remark));
        userRepository.save(friend);
        return FriendResultFactory.getInstance().produceSuccess();
    }
    @Override
    public BaseResult passFriendRequest(String token, String id) {
        Optional<User> userOptional = userService.getUserByToken(token);
        Optional<User> friendOptional = userService.getUserById(id);
        if (userOptional.isEmpty()||friendOptional.isEmpty()) {
            return FriendResultFactory.getInstance().produceNotFound();
        }
        User user=userOptional.get();
        User friend=friendOptional.get();
        List<FriendRequest> friendRequests=user.getFriendRequestList();
        if (friendRequests==null){
            return FriendResultFactory.getInstance().produceNotFound("好友申请为空");
        }
        if(!addFriend(user,id,friend.getUsername())||!addFriend(friend,user.getId(),user.getUsername())){
            return FriendResultFactory.getInstance().produceConflict();
        }
        addFriend(user,id,friend.getUsername());
        addFriend(friend,user.getId(),user.getUsername());
        friendRequests.removeIf(friendRequest -> friendRequest.getSenderId().equals(id));
        userRepository.save(user);
        return FriendResultFactory.getInstance().produceSuccess();
    }

    @Override
    public BaseResult rejectFriendRequest(String token, String id) {
        Optional<User> userOptional = userService.getUserByToken(token);
        Optional<User> friendOptional = userService.getUserById(id);
        if (userOptional.isEmpty()||friendOptional.isEmpty()) {
            return FriendResultFactory.getInstance().produceNotFound();
        }
        User user=userOptional.get();
        List<FriendRequest> friendRequests=user.getFriendRequestList();
        if (friendRequests==null){
            return FriendResultFactory.getInstance().produceNotFound("好友申请为空");
        }
        friendRequests.removeIf(friendRequest -> friendRequest.getSenderId().equals(id));
        userRepository.save(user);
        return FriendResultFactory.getInstance().produceSuccess();
    }
    //用于测试而初始化昵称
    private boolean addFriend(User user,String id,String username){
        if (user.getFriends()==null){
            user.setFriends(new HashMap<>(16));
        }
        if (user.getFriends().containsKey(id)) {
            return false;
        }
        user.getFriends().put(id, username);
        userRepository.save(user);
        return true;
    }
    private boolean addFriend(User user,String id){
        if (user.getFriends()==null){
            user.setFriends(new HashMap<>(16));
        }
        if (user.getFriends().containsKey(id)) {
            return false;
        }
        user.getFriends().put(id,"");
        userRepository.save(user);
        return true;
    }
    private boolean deleteFriend(User user,String id){
        Map<String, String> friends = user.getFriends();
        if(friends==null||!friends.containsKey(id)){
            return false;
        }
        friends.remove(id);
        userRepository.save(user);
        return true;
    }
}