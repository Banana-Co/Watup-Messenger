package com.buaa.whatupmessengermessaging.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("friend-service")
public interface FriendService {
    @RequestMapping(value = "/api/friend", method = RequestMethod.GET)
    public Boolean isFriend(@RequestHeader(value = "Authorization") String token, @RequestParam(value = "id") String id);

    @RequestMapping(value = "/api/friend/block", method = RequestMethod.GET)
    public Boolean isBlock(@RequestHeader(value = "Authorization") String token, @RequestParam(value = "id") String id);

    @RequestMapping(value = "/api/friends/id", method = RequestMethod.GET)
    public List<String> getFriendsSimple(@RequestHeader(value = "Authorization") String token);

    @RequestMapping(value = "/friend", method = RequestMethod.GET)
    public Boolean isFriendById(@RequestParam(value = "id") String id, @RequestParam(value = "friendId") String friendId);

    @RequestMapping(value = "/friend/block", method = RequestMethod.GET)
    public Boolean isBlockById(@RequestParam(value = "id") String id, @RequestParam(value = "friendId") String friendId);
}
