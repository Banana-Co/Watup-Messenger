package com.buaa.watupmessengeroauthserver.controller;

import com.buaa.watupmessengeroauthserver.model.User;
import com.buaa.watupmessengeroauthserver.model.Code;
import com.buaa.watupmessengeroauthserver.repository.UserRepository;
import com.buaa.watupmessengeroauthserver.repository.VerifyCodeRepository;
import com.buaa.watupmessengeroauthserver.result.Result;
import com.buaa.watupmessengeroauthserver.result.ResultCode;
import com.buaa.watupmessengeroauthserver.result.ResultFactory;
import com.buaa.watupmessengeroauthserver.util.MailUtil;
import com.buaa.watupmessengeroauthserver.util.UUIDUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.token.DefaultAccessTokenRequest;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@CrossOrigin
@RequestMapping("/oauth")
public class AuthController {

    Logger logger = LoggerFactory.getLogger(AuthController.class);

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

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);


    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public Result register(HttpServletRequest req) {

        String userId = req.getParameter("id");
        String code = req.getParameter("code");
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String email = req.getParameter("email");
        logger.info("email", email);

        Code dbCode = codeRepository.findByEmail(email);
        if(dbCode == null) {
            return ResultFactory.buildFailResult("邮箱错误");
        }
        if(!dbCode.getCode().equals(code)) {
            return ResultFactory.buildFailResult("验证码错误");
        }
        if(dbCode.getTimeStamp().plusMinutes(5).compareTo(LocalDateTime.now()) < 0){
            codeRepository.delete(dbCode);
            return  ResultFactory.buildFailResult("验证码过期");
        }
        codeRepository.delete(dbCode);
        User user = new User(userId, username, new BCryptPasswordEncoder().encode(password),  email);
        user.setAvatarUrl("https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png");
        mongoTemplate.save(user, "user");
        return ResultFactory.buildSuccessResult("注册成功");
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Result login(HttpServletRequest req) {
        String username = req.getParameter("id");
        String password = req.getParameter("password");

        ResourceOwnerPasswordResourceDetails details = new ResourceOwnerPasswordResourceDetails();
        //设置请求认证授权的服务器的地址
        details.setAccessTokenUri("http://localhost:8081/oauth/token");
        //下面都是认证信息：所拥有的权限，认证的客户端，具体的用户
        details.setScope(Arrays.asList("all"));
        details.setClientId("watup");
        details.setClientSecret("watup");
        details.setUsername(username);
        details.setPassword(password);

        ResourceOwnerPasswordAccessTokenProvider provider = new ResourceOwnerPasswordAccessTokenProvider();
        OAuth2AccessToken accessToken = null;
        try {
            //获取AccessToken
            // 1、(内部流程简介：根据上述信息，将构造一个前文一中的请求头为 "Basic Base64(username:password)" 的http请求
            //2、之后将向认证授权服务器的 oauth/oauth_token 端点发送请求，试图获取AccessToken
            accessToken = provider.obtainAccessToken(details, new DefaultAccessTokenRequest());
        } catch (NullPointerException e) {
            log.error("授权失败原因：{}", e.getMessage());
            return ResultFactory.buildFailResult("用户名或密码错误");
        }catch (Exception e){
            log.error("授权失败原因：{}", e.getMessage());
            return ResultFactory.buildFailResult("用户名或密码错误");
        }

        return ResultFactory.buildSuccessResult(accessToken);

    }


    @RequestMapping(value = "/checkUserId", method = RequestMethod.POST)
    public Result checkoutUserId(HttpServletRequest req) {
        String userId = req.getParameter("id");
        if(userRepository.findUserById(userId) != null) {
            return ResultFactory.buildFailResult("用户id被占用");

        }
        return ResultFactory.buildSuccessResult("用户id合法");
    }


    @RequestMapping(value = "/sendCode", method = RequestMethod.POST)
    public Result sendCode(HttpServletRequest req) {
        String email = req.getParameter("email");
        User dbUser = userRepository.findUserByEmail(email);
        if(dbUser != null) {
            return ResultFactory.buildFailResult("邮箱已被绑定");
        }
        Code dbCode = codeRepository.findByEmail(email);
        if(dbCode!=null){
            codeRepository.delete(dbCode);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
        String subject = "WatupMessenger用户注册";
        String emailTemplate = "registerTemplate";
        String code = uuidUtil.randomCode();
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("email", email);
        dataMap.put("code", code);
        dataMap.put("createTime", sdf.format(new Date()));
        try {
            mailUtil.sendTemplateMail(email, subject, emailTemplate, dataMap);
            mongoTemplate.save(new Code(email, code), "code");
            return ResultFactory.buildResult(ResultCode.SUCCESS, "验证码发送成功", code);
        } catch (Exception e) {
            return ResultFactory.buildFailResult("验证码发送失败");
        }

    }
    @RequestMapping(value = "/checkCode")
    public Result checkCode(HttpServletRequest req) {
        String code =  req.getParameter("code");
        String email = req.getParameter("email");

        Code dbCode = codeRepository.findByEmail(email);

        if(!dbCode.getCode().equals(code)) {
            codeRepository.delete(dbCode);
            return ResultFactory.buildFailResult("验证码错误");
        }else if (dbCode.getTimeStamp().plusMinutes(5).compareTo(LocalDateTime.now()) < 0){
            codeRepository.delete(dbCode);
            return  ResultFactory.buildFailResult("验证码过期，请再次验证");
        }else {
            return ResultFactory.buildSuccessResult("验证成功");
        }
    }

}
