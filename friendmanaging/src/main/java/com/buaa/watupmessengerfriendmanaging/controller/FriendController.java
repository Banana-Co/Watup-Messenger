package com.buaa.watupmessengerfriendmanaging.controller;

import com.buaa.watupmessengerfriendmanaging.factory.FriendResultFactory;
import com.buaa.watupmessengerfriendmanaging.model.BaseResult;
import com.buaa.watupmessengerfriendmanaging.model.OtherException;
import com.buaa.watupmessengerfriendmanaging.service.FriendFeignClient;
import com.buaa.watupmessengerfriendmanaging.service.FriendService;
import com.buaa.watupmessengerfriendmanaging.service.UserService;
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
    @Autowired
    FriendFeignClient friendFeignClient;
    @RequestMapping(value = "message/feign", method = RequestMethod.GET)
    public Object getMessages(
            @RequestHeader(name = "Authorization", required = false) String token,
            @RequestParam(name = "sort", defaultValue = "asc") String sort,
            @RequestParam(name = "group", defaultValue = "true") Boolean group) {
        return friendFeignClient.getMessages(token, sort, group);
    }
    @RequestMapping(value = "friend/search", method = RequestMethod.GET)
    public ResponseEntity<Object> getFriend(@RequestParam String token, String username) {
        return friendService.getFriend(token, username);
    }


    @RequestMapping(value = "friend/request", method = RequestMethod.PUT)
    public ResponseEntity<Object> passFriendRequest(@RequestParam String token, String id) {
        return friendService.passFriendRequest(token, id);
    }

    @RequestMapping(value = "friend/request", method = RequestMethod.POST)
    public ResponseEntity<Object> addFriendRequest(@RequestParam String token, String id, String remark) {
        return friendService.addFriendRequest(token, id, remark);
    }

    @RequestMapping(value = "friend/request", method = RequestMethod.DELETE)
    public ResponseEntity<Object> rejectFriendRequest(@RequestParam String token, String id) {
        return friendService.rejectFriendRequest(token, id);
    }

    @RequestMapping(value = "friend", method = RequestMethod.DELETE)
    public ResponseEntity<Object> deleteFriend(@RequestParam String token, String id) {
        return friendService.deleteFriend(token, id);
    }

    @RequestMapping(value = "friends", method = RequestMethod.GET)
    public ResponseEntity<Object> getFriends(@RequestParam String token) {
        return friendService.getFriends(token);
    }
    @RequestMapping(value = "friends/id", method = RequestMethod.GET)
    public List<String> getFriendsSimple(@RequestParam String token) throws OtherException {
        return friendService.getFriendsSimple(token);
    }

    @RequestMapping(value = "friend/nickname", method = RequestMethod.PUT)
    public ResponseEntity<Object> modifyFriendNickname(@RequestParam String token, String id, String nickname) {
        return friendService.modifyFriendNickname(token, id, nickname);
    }

    @RequestMapping(value = "friend/block", method = RequestMethod.PUT)
    public ResponseEntity<Object> blockFriend(@RequestParam String token, String id) {
        return friendService.blockFriend(token, id);
    }

    @RequestMapping(value = "friend/block", method = RequestMethod.DELETE)
    public ResponseEntity<Object> unblockFriend(@RequestParam String token, String id) {
        return friendService.unblockFriend(token, id);
    }
    @RequestMapping(value = "friend/blocks", method = RequestMethod.GET)
    public ResponseEntity<Object> getBlocks(@RequestParam String token) {
        return friendService.getBlocks(token);
    }
    @RequestMapping(value = "friend", method = RequestMethod.GET)
    public ResponseEntity<Object> isFriend(@RequestParam String token, String id) {
        return friendService.isFriend(token, id);
    }
    @RequestMapping(value = "friend/block", method = RequestMethod.GET)
    public ResponseEntity<Object> isBlock(@RequestParam String token, String id) {
        return friendService.isBlock(token, id);
    }
    //仅用于测试
    @RequestMapping(value = "user", method = RequestMethod.POST)
    public ResponseEntity<Object> addUser(@RequestParam String token, String username) {
        userService.addUser(token, username);
        return FriendResultFactory.getInstance().produceSuccess();
    }
}
