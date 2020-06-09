package com.buaa.watupmessengerfriendmanaging.controller;

import com.buaa.watupmessengerfriendmanaging.service.serviceInterface.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Cast
 */
@RestController
@CrossOrigin
public class FeignController {
    @Autowired
    FriendService friendService;
    @RequestMapping(value = "friend", method = RequestMethod.GET)
    public Boolean isFriend(@RequestParam String id, String friendId){
        return friendService.isFriendById(id, friendId);
    }
    @RequestMapping(value = "friend/block", method = RequestMethod.GET)
    public Boolean isBlock(@RequestParam String id, String friendId){
        return friendService.isBlockById(id, friendId);
    }

    @RequestMapping(value = "friends/id", method = RequestMethod.GET)
    public List<String> getFriendsSimple(@RequestParam String id) {
        return friendService.getFriendsSimple(id);
    }
}
