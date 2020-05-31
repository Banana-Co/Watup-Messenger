package com.buaa.watupmessengerusermanaging.controller;

import com.buaa.watupmessengerusermanaging.result.Result;
import com.buaa.watupmessengerusermanaging.result.ResultFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
public class TestController {

    @PostMapping("/chat")
    public Result chat(HttpServletRequest request) {
        String str = request.getParameter("str");
        return  ResultFactory.buildSuccessResult(str);
    }
}
