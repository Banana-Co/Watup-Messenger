package com.buaa.whatupmessengermessaging.service.impl;

import com.buaa.whatupmessengermessaging.service.UserTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@Primary
public class UserTokenServiceImpl implements UserTokenService {
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Override
    public String getId(String token) {
        return stringRedisTemplate.opsForValue().get(String.format("token:%s", token));
    }
}
