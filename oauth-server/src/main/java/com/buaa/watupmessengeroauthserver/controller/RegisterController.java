package com.buaa.watupmessengeroauthserver.controller;

import com.buaa.watupmessengeroauthserver.model.User;
import com.buaa.watupmessengeroauthserver.repository.UserRepository;
import com.buaa.watupmessengeroauthserver.result.Result;
import com.buaa.watupmessengeroauthserver.result.ResultFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class RegisterController {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private UserRepository userRepository;


    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public Result register(HttpServletRequest req, HttpServletResponse res) {
        String username = req.getParameter("username");

        if(userRepository.findByUsername(username) != null) {
            return ResultFactory.buildFailResult("用户名已被占用");
        }

        String password = req.getParameter("password");
        String nickname = req.getParameter("nickname");

        System.out.printf(password, password, nickname);

        User user = new User(username, new BCryptPasswordEncoder().encode(password),  nickname);
        mongoTemplate.save(user, "users");
        return ResultFactory.buildSuccessResult("注册成功");
    }


}
