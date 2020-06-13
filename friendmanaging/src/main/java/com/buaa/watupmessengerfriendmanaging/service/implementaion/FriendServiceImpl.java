package com.buaa.watupmessengerfriendmanaging.service.implementaion;

import com.buaa.watupmessengerfriendmanaging.exception.ConflictException;
import com.buaa.watupmessengerfriendmanaging.exception.ForbiddenException;
import com.buaa.watupmessengerfriendmanaging.exception.UserNotFoundException;
import com.buaa.watupmessengerfriendmanaging.model.Friend;
import com.buaa.watupmessengerfriendmanaging.model.FriendRequest;
import com.buaa.watupmessengerfriendmanaging.model.User;
import com.buaa.watupmessengerfriendmanaging.model.factory.FriendRequestFactory;
import com.buaa.watupmessengerfriendmanaging.model.factory.ResponseEntityFactory;
import com.buaa.watupmessengerfriendmanaging.service.mongo.repository.FriendRequestRepository;
import com.buaa.watupmessengerfriendmanaging.service.mongo.repository.UserRepository;
import com.buaa.watupmessengerfriendmanaging.service.serviceInterface.FriendService;
import com.buaa.watupmessengerfriendmanaging.service.serviceInterface.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    @Autowired
    private FriendRequestRepository friendRequestRepository;

    @Override
    public ResponseEntity<Object> getFriendById(String id, String friendId) {
        Optional<User> user = userService.getUserById(id);
        if (user.isEmpty()) {
            throw new UserNotFoundException();
        }
        Map<String, String> friends = user
                .get()
                .getFriends();
        if (friends == null||!friends.containsKey(friendId)) {
            return ResponseEntityFactory
                    .getInstance()
                    .produceNotFound("未找到好友");
        }
        Optional<User> friend=userService.getUserById(friendId);
        if (friend.isEmpty()) {
            throw new UserNotFoundException();
        }
        Friend data=friendByUser(friend.get());
        if (friends.get(friendId)!=null){
            data.setNickname(friends.get(friendId));
        }
        return ResponseEntityFactory
                .getInstance()
                .produceSuccess(data);
    }

    @Override
    public ResponseEntity<Object> getFriendByUsername(String id, String username) {
        Optional<User> user = userService.getUserById(id);
        if (user.isEmpty()) {
            throw new UserNotFoundException();
        }
        Map<String, String> friends = user
                .get()
                .getFriends();
        if (friends == null) {
            return ResponseEntityFactory
                    .getInstance()
                    .produceSuccess(new ArrayList<>());
        }
        List<Friend> data = friends
                .keySet()
                .stream()
                .map(u -> friendByUser(userService
                                .getUserById(u)
                                .orElse(new User())
                        , friends.get(u)))
                .filter(u -> u
                        .getUsername()
                        .contains(username))
                .collect(Collectors.toList());
        return ResponseEntityFactory
                .getInstance()
                .produceSuccess(data);
    }

    @Override
    public ResponseEntity<Object> deleteFriend(String userId, String friendId) {
        Optional<User> userOptional = userService.getUserById(userId);
        Optional<User> friendOptional = userService.getUserById(friendId);
        if (userOptional.isEmpty() || friendOptional.isEmpty()) {
            throw new UserNotFoundException();
        }
        User user = userOptional.get();
        User friend = friendOptional.get();
        if (deleteFriend(user, friendId) || deleteFriend(friend, userId)) {
            throw new ConflictException("请求已被处理");
        }
        return ResponseEntityFactory
                .getInstance()
                .produceSuccess();
    }

    @Override
    public ResponseEntity<Object> getFriends(String id) {
        Optional<User> user = userService.getUserById(id);
        if (user.isEmpty()) {
            throw new UserNotFoundException();
        }
        Map<String, String> friends = user
                .get()
                .getFriends();
        if (friends == null) {
            return ResponseEntityFactory
                    .getInstance()
                    .produceSuccess(new ArrayList<>());
        }
        List<Friend> data = friends
                .keySet()
                .stream()
                .map(u -> friendByUser(userService
                                .getUserById(u)
                                .orElse(new User())
                        , friends.get(u)))
                .collect(Collectors.toList());
        return ResponseEntityFactory
                .getInstance()
                .produceSuccess(data);
    }

    @Override
    public ResponseEntity<Object> blockFriend(String userId, String friendId) {
        Optional<User> userOptional = userService.getUserById(userId);
        Optional<User> friend = userService.getUserById(friendId);
        if (userOptional.isEmpty() || friend.isEmpty()) {
            throw new UserNotFoundException();
        }
        User user = userOptional.get();
        if (user.getBlocks() == null) {
            user.setBlocks(new ArrayList<>());
        }
        if (user.getBlocks().contains(friendId)) {
            throw new ConflictException("用户已被屏蔽");
        }
        user.getBlocks().add(friendId);
        userRepository.save(user);
        return ResponseEntityFactory
                .getInstance()
                .produceSuccess();
    }

    @Override
    public ResponseEntity<Object> unblockFriend(String userId, String friendId) {
        Optional<User> userOptional = userService.getUserById(userId);
        Optional<User> friend = userService.getUserById(friendId);
        if (userOptional.isEmpty() || friend.isEmpty()) {
            throw new UserNotFoundException();
        }
        User user = userOptional.get();
        if (user.getBlocks() == null || !user.getBlocks().contains(friendId)) {
            throw new ConflictException("屏蔽已被处理");
        }
        user.getBlocks().remove(friendId);
        userRepository.save(user);
        return ResponseEntityFactory
                .getInstance()
                .produceSuccess();
    }

    @Override
    public ResponseEntity<Object> getBlocks(String id) {
        Optional<User> userOptional = userService.getUserById(id);
        if (userOptional.isEmpty()) {
            throw new UserNotFoundException();
        }
        List<String> blocks = userOptional
                .get()
                .getBlocks();
        if (blocks == null) {
            return ResponseEntityFactory
                    .getInstance()
                    .produceSuccess(new ArrayList<>());
        }
        List<Friend> data = blocks
                .stream()
                .map(u -> friendByUser(userService
                        .getUserById(u)
                        .orElse(new User())))
                .collect(Collectors.toList());
        return ResponseEntityFactory
                .getInstance()
                .produceSuccess(data);
    }

    @Override
    public ResponseEntity<Object> isFriend(String userId, String friendId) {
        Optional<User> userOptional = userService.getUserById(userId);
        Optional<User> friendOptional = userService.getUserById(friendId);
        if (userOptional.isEmpty() || friendOptional.isEmpty()) {
            throw new UserNotFoundException();
        }
        User user = userOptional.get();
        boolean data = true;
        if (user.getFriends() == null || !user.getFriends().containsKey(friendId)) {
            data = false;
        }
        return ResponseEntityFactory
                .getInstance()
                .produceSuccess(data);
    }

    @Override
    public ResponseEntity<Object> isBlock(String userId, String friendId) {
        Optional<User> userOptional = userService.getUserById(userId);
        Optional<User> friendOptional = userService.getUserById(friendId);
        if (userOptional.isEmpty() || friendOptional.isEmpty()) {
            throw new UserNotFoundException();
        }
        User user = userOptional.get();
        boolean data = true;
        if (user.getBlocks() == null || !user.getBlocks().contains(friendId)) {
            data = false;
        }
        return ResponseEntityFactory
                .getInstance()
                .produceSuccess(data);
    }

    @Override
    public List<String> getFriendsSimple(String id) {
        Optional<User> user = userService.getUserById(id);
        if (user.isEmpty()) {
            throw new UserNotFoundException();
        }
        Map<String, String> friends = user
                .get()
                .getFriends();
        if (friends == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(friends.keySet());
    }

    @Override
    public Boolean isFriendById(String id, String friendId) {
        Optional<User> userOptional = userService.getUserById(id);
        Optional<User> friendOptional = userService.getUserById(friendId);
        if (userOptional.isEmpty() || friendOptional.isEmpty()) {
            throw new UserNotFoundException();
        }
        User user = userOptional.get();
        boolean data = true;
        if (user.getFriends() == null || !user.getFriends().containsKey(friendId)) {
            data = false;
        }
        return data;
    }

    @Override
    public Boolean isBlockById(String id, String friendId) {
        Optional<User> userOptional = userService.getUserById(id);
        Optional<User> friendOptional = userService.getUserById(friendId);
        if (userOptional.isEmpty() || friendOptional.isEmpty()) {
            throw new UserNotFoundException();
        }
        User user = userOptional.get();
        boolean data = true;
        if (user.getBlocks() == null || !user.getBlocks().contains(friendId)) {
            data = false;
        }
        return data;
    }

    @Override
    public ResponseEntity<Object> getFriendRequest(String id) {
        Optional<User> userOptional = userService.getUserById(id);
        if (userOptional.isEmpty()) {
            throw new UserNotFoundException();
        }
        List<FriendRequest> friendRequests = friendRequestRepository.getByReceiverId(id);
        return ResponseEntityFactory
                .getInstance()
                .produceSuccess(friendRequests);
    }


    @Override
    public ResponseEntity<Object> addFriendRequest(String userId, String friendId, String remark) {
        Optional<User> userOptional = userService.getUserById(userId);
        Optional<User> friendOptional = userService.getUserById(friendId);
        if (userOptional.isEmpty() || friendOptional.isEmpty()) {
            throw new UserNotFoundException();
        }
        User friend = friendOptional.get();
        if (friend.getBlocks() != null && friend.getBlocks().contains(userId)) {
            throw new ForbiddenException();
        }
        FriendRequest friendRequest = FriendRequestFactory.produce(userId, friendId, remark,friend.getAvatarUrl());
        friendRequestRepository.save(friendRequest);
        return ResponseEntityFactory
                .getInstance()
                .produceSuccess();
    }

    @Override
    public ResponseEntity<Object> passFriendRequest(String userId, String id) {
        Optional<User> userOptional = userService.getUserById(userId);
        if (userOptional.isEmpty()) {
            throw new UserNotFoundException();
        }
        User user = userOptional.get();
        Optional<FriendRequest> friendRequest = friendRequestRepository.getById(id);
        if (friendRequest.isEmpty()) {
            throw new ConflictException("请求已被处理");
        }
        Optional<User> friendOptional = userService
                .getUserById(friendRequest.get().getSenderId());
        if (friendOptional.isEmpty()) {
            throw new UserNotFoundException();
        }
        User friend = friendOptional.get();
        if (addFriend(user, friend.getId()) || addFriend(friend, userId)) {
            throw new ConflictException("请求已被处理");
        }
        friendRequestRepository.delete(friendRequest.get());
        return ResponseEntityFactory
                .getInstance()
                .produceSuccess();
    }

    @Override
    public ResponseEntity<Object> rejectFriendRequest(String id) {
        Optional<FriendRequest> friendRequest = friendRequestRepository.getById(id);
        if (friendRequest.isEmpty()) {
            throw new ConflictException("请求已被处理");
        }
        friendRequestRepository.delete(friendRequest.get());
        return ResponseEntityFactory
                .getInstance()
                .produceSuccess();
    }

    @Override
    public ResponseEntity<Object> modifyFriendNickname(String userId, String friendId, String nickname) {
        Optional<User> userOptional = userService.getUserById(userId);
        Optional<User> friendOptional = userService.getUserById(friendId);
        if (userOptional.isEmpty() || friendOptional.isEmpty()) {
            throw new UserNotFoundException();
        }
        User user = userOptional.get();
        if (user.getFriends() == null) {
            throw new UserNotFoundException();
        }
        user.getFriends().put(friendId, nickname);
        userRepository.save(user);
        return ResponseEntityFactory
                .getInstance()
                .produceSuccess();
    }

    @Override
    public Friend friendByUser(User user, String nickname) {
        Friend friend = friendByUser(user);
        friend.setNickname(nickname);
        return friend;
    }

    @Override
    public Friend friendByUser(User user) {
        return produceFriend(user);
    }
    //用于测试而初始化昵称
    private boolean addFriend(User user, String id, String username) {
        if (user.getFriends() == null) {
            user.setFriends(new HashMap<>(16));
        }
        if (user.getFriends().containsKey(id)) {
            return true;
        }
        user.getFriends().put(id, username);
        userRepository.save(user);
        return false;
    }

    private boolean addFriend(User user, String id) {
        if (user.getFriends() == null) {
            user.setFriends(new HashMap<>(16));
        }
        if (user.getFriends().containsKey(id)) {
            return true;
        }
        user.getFriends().put(id, "");
        userRepository.save(user);
        return false;
    }

    private boolean deleteFriend(User user, String id) {
        Map<String, String> friends = user.getFriends();
        if (friends == null || !friends.containsKey(id)) {
            return true;
        }
        friends.remove(id);
        userRepository.save(user);
        return false;
    }

    private Friend produceFriend(User user) {
        if (user.getId() == null) {
            throw new UserNotFoundException();
        }
        Friend friend = new Friend();
        friend.setId(user.getId());
        friend.setUsername(user.getUsername());
        friend.setAvatarUrl(user.getAvatarUrl());
        friend.setCreatedDate(user.getCreatedDate());
        friend.setArea(user.getArea());
        friend.setEmail(user.getEmail());
        friend.setSign(user.getSign());
        return friend;
    }


}
