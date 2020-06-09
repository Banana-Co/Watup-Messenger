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
    Boolean isFriend(@RequestParam String id, @RequestParam(value = "id") String friendId);

    @RequestMapping(value = "/api/friend/block", method = RequestMethod.GET)
    Boolean isBlock(@RequestParam String id, @RequestParam(value = "id") String friendId);

    @RequestMapping(value = "/api/friends/id", method = RequestMethod.GET)
    List<String> getFriendsSimple(@RequestParam String id);
}
