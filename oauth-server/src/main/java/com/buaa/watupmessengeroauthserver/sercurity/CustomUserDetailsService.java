package com.buaa.watupmessengeroauthserver.sercurity;

import com.buaa.watupmessengeroauthserver.model.User;
import com.buaa.watupmessengeroauthserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;



public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User dbUser;

        if(username.contains("@")) {
            dbUser = userRepository.findUserByEmail(username);
        } else {
            dbUser = userRepository.findUserById(username);
        }

        if(dbUser != null) {
            return dbUser;
        } else {
            throw new UsernameNotFoundException(String.format("User '%s' not found", username));
        }
    }
}
