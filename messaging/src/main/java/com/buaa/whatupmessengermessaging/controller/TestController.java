package com.buaa.whatupmessengermessaging.controller;

import com.buaa.whatupmessengermessaging.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {
    @Autowired
    FriendService friendService;

    @RequestMapping(value = "/friend", method = RequestMethod.GET)
    public Boolean isFriendById(@RequestParam(value = "id") String id, @RequestParam(value = "friendId") String friendId) {
        return friendService.isFriendById(id, friendId);
    }

    @RequestMapping(value = "/friend/block", method = RequestMethod.GET)
    public Boolean isBlockById(@RequestParam(value = "id") String id, @RequestParam(value = "friendId") String friendId) {
        return friendService.isBlockById(id, friendId);
    }

    @RequestMapping(value = "/api/friends/id", method = RequestMethod.GET)
    public List<String> getFriendsSimple(@RequestParam(value = "token") String token) {
        return friendService.getFriendsSimple(token);
    }
}
