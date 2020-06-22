package com.buaa.watupmessengerfriendmanaging.controller;

import com.buaa.watupmessengerfriendmanaging.model.factory.ResponseEntityFactory;
import com.buaa.watupmessengerfriendmanaging.service.serviceInterface.FriendService;
import com.buaa.watupmessengerfriendmanaging.service.serviceInterface.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Cast
 */
@RestController
@CrossOrigin
@RequestMapping(value = "/api/")
public class FriendController {
    @Autowired
    UserService userService;
    @Autowired
    FriendService friendService;

    @RequestMapping(value = "friend/user", method = RequestMethod.GET)
    public ResponseEntity<?> getUser(
            @RequestParam String id
            , @RequestParam String keyword) {
        return userService.getFriend(id, keyword);
    }
    @RequestMapping(value = "friend/search/id", method = RequestMethod.GET)
    public ResponseEntity<?> getFriendById(
            @RequestParam String id
            , @RequestParam String friendId) {
        return friendService.getFriendById(id,friendId);
    }

    @RequestMapping(value = "friend/search/username", method = RequestMethod.GET)
    public ResponseEntity<Object> getFriendByUsername(
            @RequestParam String id
            , @RequestParam String username) {
        return friendService.getFriendByUsername(id, username);
    }

    @RequestMapping(value = "friend/request", method = RequestMethod.GET)
    public ResponseEntity<Object> getFriendRequest(@RequestParam String id) {
        return friendService.getFriendRequest(id);
    }

    @RequestMapping(value = "friend/request", method = RequestMethod.PUT)
    public ResponseEntity<Object> passFriendRequest(
            @RequestParam String id
            , @RequestParam String requestId) {
        return friendService.passFriendRequest(id, requestId);
    }

    @RequestMapping(value = "friend/request", method = RequestMethod.POST)
    public ResponseEntity<Object> addFriendRequest(
            @RequestParam String id
            , @RequestParam String friendId
            , @RequestParam String remark) {
        return friendService.addFriendRequest(id, friendId, remark);
    }

    @RequestMapping(value = "friend/request", method = RequestMethod.DELETE)
    public ResponseEntity<Object> rejectFriendRequest(
            @RequestParam String id
            , @RequestParam String requestId) {
        return friendService.rejectFriendRequest(id,requestId);
    }

    @RequestMapping(value = "friend", method = RequestMethod.DELETE)
    public ResponseEntity<Object> deleteFriend(@RequestParam String id
            , @RequestParam String friendId) {
        return friendService.deleteFriend(id, friendId);
    }

    @RequestMapping(value = "friends", method = RequestMethod.GET)
    public ResponseEntity<Object> getFriends(@RequestParam String id) {
        return friendService.getFriends(id);
    }

    @RequestMapping(value = "friends/id", method = RequestMethod.GET)
    public List<String> getFriendsSimple(@RequestParam String id) {
        return friendService.getFriendsSimple(id);
    }

    @RequestMapping(value = "friend/nickname", method = RequestMethod.PUT)
    public ResponseEntity<Object> modifyFriendNickname(@RequestParam String id
            , @RequestParam String friendId
            , @RequestParam String nickname) {
        return friendService.modifyFriendNickname(id,friendId, nickname);
    }

    @RequestMapping(value = "friend/block", method = RequestMethod.PUT)
    public ResponseEntity<Object> blockFriend(@RequestParam String id
            , @RequestParam String friendId) {
        return friendService.blockFriend(id, friendId);
    }

    @RequestMapping(value = "friend/block", method = RequestMethod.DELETE)
    public ResponseEntity<Object> unblockFriend(@RequestParam String id
            , @RequestParam String friendId) {
        return friendService.unblockFriend(id, friendId);
    }

    @RequestMapping(value = "friend/blocks", method = RequestMethod.GET)
    public ResponseEntity<Object> getBlocks(@RequestParam String id) {
        return friendService.getBlocks(id);
    }

    @RequestMapping(value = "friend", method = RequestMethod.GET)
    public ResponseEntity<Object> isFriend(@RequestParam String id
            , @RequestParam String friendId) {
        return friendService.isFriend(id, friendId);
    }

    @RequestMapping(value = "friend/block", method = RequestMethod.GET)
    public ResponseEntity<Object> isBlock(@RequestParam String id
            , @RequestParam String friendId) {
        return friendService.isBlock(id, friendId);
    }

    //仅用于测试
    @RequestMapping(value = "friend/user", method = RequestMethod.POST)
    public ResponseEntity<Object> addUser(@RequestParam String id, @RequestParam String nickname) {
        userService.addUser(id, nickname);
        return ResponseEntityFactory.getInstance().produceSuccess();
    }
}
