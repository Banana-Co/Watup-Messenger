package com.buaa.watupmessengerusermanaging.controller;

import com.buaa.watupmessengerusermanaging.model.UserEntity;
import com.buaa.watupmessengerusermanaging.repository.UserEntityRepository;
import com.buaa.watupmessengerusermanaging.result.Result;
import com.buaa.watupmessengerusermanaging.result.ResultFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/oauth")
public class RegisterController {

    @Autowired
    private UserEntityRepository userEntityRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @PostMapping("/register")
    public Result register(HttpServletRequest req) {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String email = req.getParameter("email");

        UserEntity userEntity = userEntityRepository.findByUsername("username");

        if(userEntity != null) {
            ResultFactory.buildFailResult("该用户名已被占用");
        }

        UserEntity user = new UserEntity(username, password, email);

        mongoTemplate.save(user, "users");

        return ResultFactory.buildSuccessResult("注册成功");

    }

}
