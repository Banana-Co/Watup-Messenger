package com.buaa.watupmessengerusermanaging.controller;

import com.buaa.watupmessengerusermanaging.repository.UserRepository;
import com.buaa.watupmessengerusermanaging.result.Result;
import com.buaa.watupmessengerusermanaging.result.ResultCode;
import com.buaa.watupmessengerusermanaging.result.ResultFactory;
import com.buaa.watupmessengerusermanaging.service.FileService;
import com.buaa.watupmessengerusermanaging.util.ImgUtil;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
public class SystemController {

    @Autowired
    private FileService fileService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private UserRepository userRepository;

    @Value("${file.uploadFolder}")
    private String uploadFolder;

    private String staticAccessPath = "/static/upload/img/";

    Logger logger = LoggerFactory.getLogger(SystemController.class);

    @RequestMapping(value = "/api/user/uploadImg", method = RequestMethod.POST)
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

    @RequestMapping(value = "/getGroupAvatar", method = RequestMethod.POST)
    public String getGroupAvatar(@RequestBody List<String> userIdList,
                                 @RequestParam String groupId, HttpServletRequest req) {

        List<String>avatarUrls = new ArrayList<>();

        for(String id : userIdList) {
            String avatar =userRepository.findUserById(id).getAvatarUrl();
            avatarUrls.add(avatar);
        }

        String  baseUrl = req.getScheme() + "://" + req.getServerName() + ":" +
                req.getServerPort() + req.getContextPath(); //网页访问路径前缀
        List<String> paths = new ArrayList<>();
        for(String s:avatarUrls) {
            paths.add(s);
        }

        try {
            String filePath = ImgUtil.getCombinationOfHead(paths , uploadFolder, groupId );
            String avatarUrl = baseUrl + staticAccessPath + filePath;
            return avatarUrl;

        }catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
