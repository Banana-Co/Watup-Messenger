package com.buaa.watupmessengerusermanaging.controller;

import com.buaa.watupmessengerusermanaging.model.User;
import com.buaa.watupmessengerusermanaging.repository.UserRepository;
import com.buaa.watupmessengerusermanaging.result.Result;
import com.buaa.watupmessengerusermanaging.result.ResultCode;
import com.buaa.watupmessengerusermanaging.result.ResultFactory;
import com.buaa.watupmessengerusermanaging.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(path = "/user")
public class UserController {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileService fileService;

    @RequestMapping(path = "updateUsername", method = RequestMethod.POST)
    public Result updateUsername(HttpServletRequest req) {
        String id = req.getParameter("id");
        String username = req.getParameter("username");
        User user = userRepository.findUserById(id);
        if(user != null) {
            user.setUsername(username);
            mongoTemplate.save(user, "users");
            return ResultFactory.buildSuccessResult("昵称修改成功");
        }

        return ResultFactory.buildFailResult("昵称修改失败");
    }

    @RequestMapping(value = "/updateAvatar", method = RequestMethod.POST)
    public Result updateAvatar(@RequestParam(name = "file") MultipartFile file, HttpServletRequest req) {
        String  baseUrl = req.getScheme() + "://" + req.getServerName() + ":" +
                req.getServerPort() + req.getContextPath(); //网页访问路径前缀

        String imageUrl = fileService.uploadImage(file, baseUrl);

        if(imageUrl != null) {
            return ResultFactory.buildResult(ResultCode.SUCCESS, "头像上传成功", imageUrl);
        }else {
            return ResultFactory.buildFailResult("头像上传失败");
        }
    }

    @RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
    public Result updatePassword(HttpServletRequest req) {
        String id = req.getParameter("id");
        String password = req.getParameter("password");
        User user = userRepository.findUserById(id);
        if(user != null) {
            user.setPassword(new BCryptPasswordEncoder().encode(password));
            mongoTemplate.save(user, "users");
            return ResultFactory.buildSuccessResult("密码修改成功");
        }

        return ResultFactory.buildFailResult("密码修改失败");
    }
    

}
