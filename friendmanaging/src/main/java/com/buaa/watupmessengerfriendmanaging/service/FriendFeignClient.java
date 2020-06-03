package com.buaa.watupmessengerfriendmanaging.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * @author Cast
 */
@FeignClient(value = "MESSAGING-SERVICE")
public interface FriendFeignClient {
    @RequestMapping(value = "api/message",method = RequestMethod.GET)
    Object getMessages(@RequestHeader(name = "Authorization", required = false) String token,
                       @RequestParam(name = "sort", defaultValue = "asc") String sort,
                       @RequestParam(name = "group", defaultValue = "true") Boolean group);
//    @RequestMapping(value = "api/friends",method = RequestMethod.GET)
//    BaseResult getFriends(String token);
}
