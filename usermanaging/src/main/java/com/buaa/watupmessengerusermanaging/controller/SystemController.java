package com.buaa.watupmessengerusermanaging.controller;

import com.buaa.watupmessengerusermanaging.result.Result;
import com.buaa.watupmessengerusermanaging.result.ResultCode;
import com.buaa.watupmessengerusermanaging.result.ResultFactory;
import com.buaa.watupmessengerusermanaging.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/api/user")
public class SystemController {

    @Autowired
    private FileService fileService;

    Logger logger = LoggerFactory.getLogger(SystemController.class);

    @RequestMapping(value = "/uploadImg", method = RequestMethod.POST)
    Result uploadImg(@RequestParam(name = "img") MultipartFile file,
                     HttpServletRequest req) {
        String  baseUrl = req.getScheme() + "://" + req.getServerName() + ":" +
                req.getServerPort() + req.getContextPath(); //网页访问路径前缀
        logger.info("baseUrl", baseUrl);
        String imageUrl = fileService.uploadImage(file, baseUrl);
        if(imageUrl == null) {
            return ResultFactory.buildFailResult("图片上传失败");
        }
        logger.info("imgUrl", imageUrl);
        return ResultFactory.buildResult(ResultCode.SUCCESS, "图片上传成功", imageUrl);
    }
}
