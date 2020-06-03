package com.buaa.watupmessengerfriendmanaging.controller;

import com.buaa.watupmessengerfriendmanaging.model.exception.OtherException;
import com.buaa.watupmessengerfriendmanaging.service.FriendFeignClient;
import com.buaa.watupmessengerfriendmanaging.service.FriendService;
import com.buaa.watupmessengerfriendmanaging.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Cast
 */
@RestController
@CrossOrigin
public class FeignController {
    @Autowired
    UserService userService;
    @Autowired
    FriendService friendService;
    @Autowired
    FriendFeignClient friendFeignClient;
    @RequestMapping(value = "friend", method = RequestMethod.GET)
    public Boolean isFriend(@RequestParam String id, String friendId){
        return friendService.isFriendById(id, friendId);
    }
    @RequestMapping(value = "friend/block", method = RequestMethod.GET)
    public Boolean isBlock(@RequestParam String id, String friendId){
        return friendService.isBlockById(id, friendId);
    }
}
