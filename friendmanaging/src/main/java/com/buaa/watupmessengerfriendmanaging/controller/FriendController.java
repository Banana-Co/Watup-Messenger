package com.buaa.watupmessengerfriendmanaging.controller;

import com.buaa.watupmessengerfriendmanaging.model.BaseResult;
import com.buaa.watupmessengerfriendmanaging.factory.FriendResultFactory;
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
    public BaseResult addFriendRequest(@RequestParam String token, String id,String remark) {
        return friendService.addFriendRequest(token, id, remark);
    }
    @RequestMapping(value = "friend/request", method = RequestMethod.DELETE)
    public BaseResult rejectFriendRequest(@RequestParam String token, String id) {
        return friendService.rejectFriendRequest(token, id);
    }
    @RequestMapping(value = "friend",method = RequestMethod.DELETE)
    public BaseResult deleteFriend(@RequestParam String token, String id){
        return friendService.deleteFriend(token, id);
    }
    @RequestMapping(value = "friends",method = RequestMethod.GET)
    public BaseResult getFriends(@RequestParam String token){
        return friendService.getFriends(token);
    }
    @RequestMapping(value = "block", method = RequestMethod.PUT)
    public BaseResult addBlock(@RequestParam String token, String id) {
        return friendService.addBlock(token, id);
    }
    //仅用于测试
    @RequestMapping(value = "testBlock", method = RequestMethod.PUT)
    public BaseResult addTestBlock(@RequestParam String name1, String name2) {
        return friendService.addTestBlock(name1, name2);
    }

    //仅用于测试
    @RequestMapping(value = "user", method = RequestMethod.POST)
    public BaseResult addUser(@RequestParam String token, String username) {
        userService.addUser(token, username);
        return FriendResultFactory.getInstance().produceSuccess();
    }
}