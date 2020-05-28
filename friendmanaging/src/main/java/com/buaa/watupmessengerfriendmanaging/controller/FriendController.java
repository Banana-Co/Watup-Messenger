package com.buaa.watupmessengerfriendmanaging.controller;

import com.buaa.watupmessengerfriendmanaging.factory.FriendResultFactory;
import com.buaa.watupmessengerfriendmanaging.model.BaseResult;
import com.buaa.watupmessengerfriendmanaging.service.FriendService;
import com.buaa.watupmessengerfriendmanaging.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(value = "friend", method = RequestMethod.GET)
    public BaseResult getFriend(@RequestParam String token, String username) {
        return friendService.getFriend(token, username);
    }


    @RequestMapping(value = "friend/request", method = RequestMethod.PUT)
    public BaseResult passFriendRequest(@RequestParam String token, String id) {
        return friendService.passFriendRequest(token, id);
    }

    @RequestMapping(value = "friend/request", method = RequestMethod.POST)
    public BaseResult addFriendRequest(@RequestParam String token, String id, String remark) {
        return friendService.addFriendRequest(token, id, remark);
    }

    @RequestMapping(value = "friend/request", method = RequestMethod.DELETE)
    public BaseResult rejectFriendRequest(@RequestParam String token, String id) {
        return friendService.rejectFriendRequest(token, id);
    }

    @RequestMapping(value = "friend", method = RequestMethod.DELETE)
    public BaseResult deleteFriend(@RequestParam String token, String id) {
        return friendService.deleteFriend(token, id);
    }

    @RequestMapping(value = "friends", method = RequestMethod.GET)
    public BaseResult getFriends(@RequestParam String token) {
        return friendService.getFriends(token);
    }

    @RequestMapping(value = "friend/nickname", method = RequestMethod.PUT)
    public BaseResult modifyFriendNickname(@RequestParam String token, String id, String nickname) {
        return friendService.modifyFriendNickname(token, id, nickname);
    }

    @RequestMapping(value = "friend/block", method = RequestMethod.PUT)
    public BaseResult blockFriend(@RequestParam String token, String id) {
        return friendService.blockFriend(token, id);
    }

    @RequestMapping(value = "friend/block", method = RequestMethod.DELETE)
    public BaseResult unblockFriend(@RequestParam String token, String id) {
        return friendService.unblockFriend(token, id);
    }
    @RequestMapping(value = "friend/blocks", method = RequestMethod.GET)
    public BaseResult getBlocks(@RequestParam String token) {
        return friendService.getBlocks(token);
    }
    //仅用于测试
    @RequestMapping(value = "user", method = RequestMethod.POST)
    public BaseResult addUser(@RequestParam String token, String username) {
        userService.addUser(token, username);
        return FriendResultFactory.getInstance().produceSuccess();
    }
}
