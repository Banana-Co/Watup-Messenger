package com.buaa.whatupmessengermessaging.service;

import com.buaa.whatupmessengermessaging.model.CheckTokenResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("auth-server")
public interface AuthServer {

    @RequestMapping(value = "/oauth/check_token", method = RequestMethod.POST)
    CheckTokenResult checkToken(@RequestParam String token);
}
