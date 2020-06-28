package com.buaa.watupmessengerusermanaging.controller;

import com.buaa.watupmessengerusermanaging.model.User;
import com.buaa.watupmessengerusermanaging.util.ImgUtil;
import com.buaa.watupmessengerusermanaging.repository.UserRepository;
import com.buaa.watupmessengerusermanaging.result.Result;
import com.buaa.watupmessengerusermanaging.result.ResultCode;
import com.buaa.watupmessengerusermanaging.result.ResultFactory;
import com.buaa.watupmessengerusermanaging.service.FileService;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;

import org.springframework.data.mongodb.core.aggregation.ArrayOperators;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api")
@CrossOrigin
public class UserController {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileService fileService;

    @Value("${file.uploadFolder}")
    private String uploadFolder;

    private String staticAccessPath = "/static/upload/img/";

    @RequestMapping(value = "/test", method = RequestMethod.POST)
    public Result hello() {
        return ResultFactory.buildSuccessResult("hello");
    }

    @RequestMapping(value = "/user/updateUsername", method = RequestMethod.POST)
    public Result updateUsername(@RequestParam("id") String id, @RequestParam("username") String username) {
        User user = userRepository.findUserById(id);
        if(user != null) {
            user.setUsername(username);
            mongoTemplate.save(user, "user");
            return ResultFactory.buildSuccessResult("昵称修改成功");
        }

        return ResultFactory.buildFailResult("昵称修改失败");
    }



    @RequestMapping(value = "/user/updateAvatar", method = RequestMethod.POST)
    public Result updateAvatar(@RequestParam(name = "file") MultipartFile file,
                               @RequestParam(name = "id") String id,
                               HttpServletRequest req) {
        String  baseUrl = req.getScheme() + "://" + req.getServerName() + ":" +
                req.getServerPort() + req.getContextPath(); //网页访问路径前缀
        System.out.println(baseUrl);
        String imageUrl = fileService.uploadImage(file, baseUrl);

        if(imageUrl != null) {
            User dbUser = userRepository.findUserById(id);
            if(dbUser == null) {
                return ResultFactory.buildFailResult("用户id错误");
            }
            dbUser.setAvatarUrl(imageUrl);
            userRepository.save(dbUser);
            return ResultFactory.buildResult(ResultCode.SUCCESS, "头像上传成功", imageUrl);
        }else {
            return ResultFactory.buildFailResult("头像上传失败");
        }
    }

    @RequestMapping(value = "/user/updatePassword", method = RequestMethod.POST)
    public Result updatePassword(@RequestParam(value = "id") String id,
                                 @RequestParam(value = "oldPassword") String oldPassword,
                                 @RequestParam(value = "newPassword") String newPassword) {
        User user = userRepository.findUserById(id);
        if(user == null) {
            return ResultFactory.buildFailResult("用户不存在");
        }
        if(!new BCryptPasswordEncoder().matches(oldPassword, user.getPassword())) {
            return ResultFactory.buildFailResult("密码不正确");
        }
        user.setPassword(new BCryptPasswordEncoder().encode(newPassword));
        userRepository.save(user);
        return ResultFactory.buildSuccessResult("密码修改成功");
    }

    @RequestMapping(value = "/user/updateSign", method = RequestMethod.POST)
    public Result updateSign(@RequestParam(value = "id") String id,
                             @RequestParam(value = "sign") String sign) {
        User user = userRepository.findUserById(id);
        if(user == null) {
            return ResultFactory.buildFailResult("用户不存在");
        }
        user.setSign(sign);
        userRepository.save(user);
        return ResultFactory.buildSuccessResult("签名修改成功");
    }

    @RequestMapping(value = "/user/updateArea", method = RequestMethod.POST)
    public Result updateArea(@RequestParam(value = "id") String id,
                             @RequestParam(value = "area") String area) {
        User user = userRepository.findUserById(id);
        if(user == null) {
            return ResultFactory.buildFailResult("用户不存在");
        }
        user.setArea(area);
        userRepository.save(user);
        return ResultFactory.buildSuccessResult("地区修改成功");
    }
}
