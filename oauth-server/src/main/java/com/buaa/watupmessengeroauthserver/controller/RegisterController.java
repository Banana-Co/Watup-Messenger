package com.buaa.watupmessengeroauthserver.controller;

import com.buaa.watupmessengeroauthserver.model.User;
import com.buaa.watupmessengeroauthserver.model.VerifyCode;
import com.buaa.watupmessengeroauthserver.repository.UserRepository;
import com.buaa.watupmessengeroauthserver.repository.VerifyCodeRepository;
import com.buaa.watupmessengeroauthserver.result.Result;
import com.buaa.watupmessengeroauthserver.result.ResultCode;
import com.buaa.watupmessengeroauthserver.result.ResultFactory;
import com.buaa.watupmessengeroauthserver.util.MailUtil;
import com.buaa.watupmessengeroauthserver.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/oauth")
public class RegisterController {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MailUtil mailUtil;

    @Autowired
    private UUIDUtil uuidUtil;

    @Autowired
    private VerifyCodeRepository codeRepository;

    @Autowired
    RestTemplate restTemplate;


    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public Result register(HttpServletRequest req, HttpServletResponse res) {
        String uId = req.getParameter("id");

        if(userRepository.findUserById(uId) != null) {
            System.out.printf(userRepository.findUserById(uId).getEmail());
            return ResultFactory.buildFailResult("用户id已被占用");

        }

        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String email = req.getParameter("email");

        User user = new User(uId, username, new BCryptPasswordEncoder().encode(password),  email);
        user.setAvatarUrl("https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png");
        mongoTemplate.save(user, "user");
        return ResultFactory.buildSuccessResult("注册成功");
    }

//    @PostMapping(value = "/login")
//    public Result login(String id, String password) {
//
//        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
//        map.add("username", id);
//        map.add("password", password);
//        map.add("client_secret", "watup");
//        map.add("client_id", "watup");
//        map.add("grant_type", "password");
//        Map<String,String> resp = restTemplate.postForObject("http://localhost:8080/oauth/token", map, Map.class);
//        if(resp.containsKey("access_token")) {
//            return ResultFactory.buildResult(ResultCode.SUCCESS, "登录成功", resp);
//        }else {
//            return ResultFactory.buildFailResult("用户名或密码错误");
//        }
//    }

    @RequestMapping(value = "/sendCode", method = RequestMethod.POST)
    public Result sendCode(HttpServletRequest req) {
        String email = req.getParameter("email");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
        String subject = "WatupMessemger用户注册";
        String emailTemplate = "registerTemplate";
        String code = uuidUtil.randomCode();
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("email", email);
        dataMap.put("code", code);
        dataMap.put("createTime", sdf.format(new Date()));
        try {
            mailUtil.sendTemplateMail(email, subject, emailTemplate, dataMap);
            mongoTemplate.save(new VerifyCode(email, code), "code");
            return ResultFactory.buildResult(ResultCode.SUCCESS, "验证码发送成功", code);
        } catch (Exception e) {
            return ResultFactory.buildFailResult("验证码发送失败");
        }

    }
    @RequestMapping(value = "/checkCode")
    public Result checkCode(HttpServletRequest req) {
        String code =  req.getParameter("code");
        String email = req.getParameter("email");

        VerifyCode dbCode = codeRepository.findByEmail(email);

        if(dbCode != null && dbCode.getCode().equals(code)) {
            codeRepository.delete(dbCode);
            return ResultFactory.buildSuccessResult("验证成功");
        }else {
            return ResultFactory.buildFailResult("验证码错误");
        }
    }

}
