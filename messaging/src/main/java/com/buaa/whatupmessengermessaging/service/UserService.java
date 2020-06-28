package com.buaa.whatupmessengermessaging.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("user-server")
public interface UserService {
    @RequestMapping(value = "/getGroupAvatar", method = RequestMethod.POST)
    public String getGroupAvatar(@RequestBody List<String> userId, @RequestParam String groupId);
}
