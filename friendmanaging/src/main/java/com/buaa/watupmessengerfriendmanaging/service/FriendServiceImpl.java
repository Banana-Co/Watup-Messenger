package com.buaa.watupmessengerfriendmanaging.service;

import com.buaa.watupmessengerfriendmanaging.model.FriendRequest;
import com.buaa.watupmessengerfriendmanaging.model.User;
import com.buaa.watupmessengerfriendmanaging.model.exception.ConflictException;
import com.buaa.watupmessengerfriendmanaging.model.exception.ForbiddenException;
import com.buaa.watupmessengerfriendmanaging.model.exception.UserNotFoundException;
import com.buaa.watupmessengerfriendmanaging.model.factory.FriendRequestFactory;
import com.buaa.watupmessengerfriendmanaging.model.factory.ResponseEntityFactory;
import com.buaa.watupmessengerfriendmanaging.service.mongo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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

    @Override
    public ResponseEntity<Object> getFriend(String token, String username) {
        Optional<User> user = userService.getUserByToken(token);
        if (user.isEmpty()) {
            throw new UserNotFoundException();
        }
        Map<String, String> friends = user
                .get()
                .getFriends();
        if (friends == null) {
            return ResponseEntityFactory.getInstance().produceSuccess(new ArrayList<>());
        }
        List<User> data = friends
                .keySet()
                .stream()
                .map(u -> userService
                        .getUserById(u)
                        .orElse(new User()))
                .filter(u -> u.getUsername().contains(username))
                .collect(Collectors.toList());
        return ResponseEntityFactory.getInstance().produceSuccess(data);
    }

    @Override
    public ResponseEntity<Object> deleteFriend(String token, String id) {
        Optional<User> userOptional = userService.getUserByToken(token);
        Optional<User> friendOptional = userService.getUserById(id);
        if (userOptional.isEmpty() || friendOptional.isEmpty()) {
            throw new UserNotFoundException();
        }
        User user = userOptional.get();
        User friend = friendOptional.get();
        if (deleteFriend(user, friend.getId()) || deleteFriend(friend, user.getId())) {
            throw new ConflictException("请求已被处理");
        }
        return ResponseEntityFactory.getInstance().produceSuccess();
    }

    @Override
    public ResponseEntity<Object> getFriends(String token) {
        Optional<User> user = userService.getUserByToken(token);
        if (user.isEmpty()) {
            throw new UserNotFoundException();
        }
        Map<String, String> friends = user
                .get()
                .getFriends();
        if (friends == null) {
            return ResponseEntityFactory.getInstance().produceSuccess(new ArrayList<>());
        }
        List<User> data = friends
                .keySet()
                .stream()
                .map(u -> userService
                        .getUserById(u)
                        .orElse(new User()))
                .collect(Collectors.toList());
        return ResponseEntityFactory.getInstance().produceSuccess(data);
    }

    @Override
    public ResponseEntity<Object> blockFriend(String token, String id) {
        Optional<User> userOptional = userService.getUserByToken(token);
        Optional<User> friend = userService.getUserById(id);
        if (userOptional.isEmpty() || friend.isEmpty()) {
            throw new UserNotFoundException();
        }
        User user = userOptional.get();
        if (user.getBlocks() == null) {
            user.setBlocks(new ArrayList<>());
        }
        if (user.getBlocks().contains(id)) {
            throw new ConflictException("用户已被屏蔽");
        }
        user.getBlocks().add(id);
        userRepository.save(user);
        return ResponseEntityFactory.getInstance().produceSuccess();
    }

    @Override
    public ResponseEntity<Object> unblockFriend(String token, String id) {
        Optional<User> userOptional = userService.getUserByToken(token);
        Optional<User> friend = userService.getUserById(id);
        if (userOptional.isEmpty() || friend.isEmpty()) {
            throw new UserNotFoundException();
        }
        User user = userOptional.get();
        if (user.getBlocks() == null || !user.getBlocks().contains(id)) {
            throw new ConflictException("屏蔽已被处理");
        }
        user.getBlocks().remove(id);
        userRepository.save(user);
        return ResponseEntityFactory.getInstance().produceSuccess();
    }

    @Override
    public ResponseEntity<Object> getBlocks(String token) {
        Optional<User> userOptional = userService.getUserByToken(token);
        if (userOptional.isEmpty()) {
            throw new UserNotFoundException();
        }
        List<String> blocks = userOptional
                .get()
                .getBlocks();
        if (blocks == null) {
            return ResponseEntityFactory.getInstance().produceSuccess(new ArrayList<>());
        }
        List<User> data = blocks
                .stream()
                .map(u -> userService
                        .getUserById(u)
                        .orElse(new User()))
                .collect(Collectors.toList());
        return ResponseEntityFactory.getInstance().produceSuccess(data);
    }

    @Override
    public ResponseEntity<Object> isFriend(String token, String id) {
        Optional<User> userOptional = userService.getUserByToken(token);
        Optional<User> friendOptional = userService.getUserById(id);
        if (userOptional.isEmpty() || friendOptional.isEmpty()) {
            throw new UserNotFoundException();
        }
        User user = userOptional.get();
        boolean data = true;
        if (user.getFriends() == null || !user.getFriends().containsKey(id)) {
            data = false;
        }
        return ResponseEntityFactory.getInstance().produceSuccess(data);
    }

    @Override
    public ResponseEntity<Object> isBlock(String token, String id) {
        Optional<User> userOptional = userService.getUserByToken(token);
        Optional<User> friendOptional = userService.getUserById(id);
        if (userOptional.isEmpty() || friendOptional.isEmpty()) {
            throw new UserNotFoundException();
        }
        User user = userOptional.get();
        boolean data = true;
        if (user.getBlocks() == null || !user.getBlocks().contains(id)) {
            data = false;
        }
        return ResponseEntityFactory.getInstance().produceSuccess(data);
    }

    @Override
    public List<String> getFriendsSimple(String token) {
        Optional<User> user = userService.getUserByToken(token);
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
    public ResponseEntity<Object> addFriendRequest(String token, String id, String remark) {
        Optional<User> userOptional = userService.getUserByToken(token);
        Optional<User> friendOptional = userService.getUserById(id);
        if (userOptional.isEmpty() || friendOptional.isEmpty()) {
            throw new UserNotFoundException();
        }
        User user = userOptional.get();
        User friend = friendOptional.get();
        if (friend.getBlocks() != null && friend.getBlocks().contains(user.getId())) {
            throw new ForbiddenException();
        }
        if (friend.getFriendRequestList() == null) {
            friend.setFriendRequestList(new ArrayList<>());
        }
        friend.getFriendRequestList().add(FriendRequestFactory.produce(user.getId(), remark));
        userRepository.save(friend);
        return ResponseEntityFactory.getInstance().produceSuccess();
    }

    @Override
    public ResponseEntity<Object> passFriendRequest(String token, String id) {
        Optional<User> userOptional = userService.getUserByToken(token);
        Optional<User> friendOptional = userService.getUserById(id);
        if (userOptional.isEmpty() || friendOptional.isEmpty()) {
            throw new UserNotFoundException();
        }
        User user = userOptional.get();
        User friend = friendOptional.get();
        List<FriendRequest> friendRequests = user.getFriendRequestList();
        if (friendRequests == null) {
            throw new ConflictException("请求已被处理");
        }
        if (friend.getBlocks() != null && friend.getBlocks().contains(user.getId())) {
            throw new ForbiddenException();
        }
        if (addFriend(user, id, friend.getUsername()) || addFriend(friend, user.getId(), user.getUsername())) {
            throw new ConflictException("请求已被处理");
        }
        addFriend(user, id, friend.getUsername());
        addFriend(friend, user.getId(), user.getUsername());
        friendRequests.removeIf(friendRequest -> friendRequest.getSenderId().equals(id));
        userRepository.save(user);
        return ResponseEntityFactory.getInstance().produceSuccess();
    }

    @Override
    public ResponseEntity<Object> rejectFriendRequest(String token, String id) {
        Optional<User> userOptional = userService.getUserByToken(token);
        Optional<User> friendOptional = userService.getUserById(id);
        if (userOptional.isEmpty() || friendOptional.isEmpty()) {
            throw new UserNotFoundException();
        }
        User user = userOptional.get();
        List<FriendRequest> friendRequests = user.getFriendRequestList();
        if (friendRequests == null) {
            throw new ConflictException("请求已被处理");
        }
        friendRequests.removeIf(friendRequest -> friendRequest.getSenderId().equals(id));
        userRepository.save(user);
        return ResponseEntityFactory.getInstance().produceSuccess();
    }

    @Override
    public ResponseEntity<Object> modifyFriendNickname(String token, String id, String nickname) {
        Optional<User> userOptional = userService.getUserByToken(token);
        Optional<User> friendOptional = userService.getUserById(id);
        if (userOptional.isEmpty() || friendOptional.isEmpty()) {
            throw new UserNotFoundException();
        }
        User user = userOptional.get();
        if (user.getFriends() == null) {
            throw new UserNotFoundException();
        }
        user.getFriends().put(id, nickname);
        userRepository.save(user);
        return ResponseEntityFactory.getInstance().produceSuccess();
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
}
