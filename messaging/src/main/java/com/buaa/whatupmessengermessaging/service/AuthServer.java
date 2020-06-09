package com.buaa.whatupmessengermessaging.service;

import com.fasterxml.jackson.databind.util.JSONPObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("auth-server")
public interface AuthServer {

    @RequestMapping(value = "/oauth/check_token", method = RequestMethod.POST)
    String checkToken(@RequestParam String access_token);
}
