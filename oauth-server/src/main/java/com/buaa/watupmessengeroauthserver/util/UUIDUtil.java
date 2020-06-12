package com.buaa.watupmessengeroauthserver.util;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class UUIDUtil {

    public  String randomCode() {
        StringBuilder str = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            str.append(random.nextInt(10));
        }
        return str.toString();
    }
}
