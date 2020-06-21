package com.buaa.watupmessengeroauthserver.sercurity;

import com.buaa.watupmessengeroauthserver.controller.AuthController;
import com.buaa.watupmessengeroauthserver.model.User;
import com.buaa.watupmessengeroauthserver.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;



public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User dbUser;

        if(username.contains("@")) {
            dbUser = userRepository.findUserByEmail(username);
        } else {
            dbUser = userRepository.findUserById(username);
        }

        if(dbUser != null) {
            logger.info(dbUser.getAvatarUrl());
            return dbUser;
        } else {
            throw new UsernameNotFoundException(String.format("User '%s' not found", username));
        }
    }
}
