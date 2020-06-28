package com.buaa.watupmessengerusermanaging;

import com.mongodb.client.model.Filters;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.buaa.watupmessengerusermanaging.util.ImgUtil;


@SpringBootTest
class WatupMessengerUserManagingApplicationTests {

    @Value("${file.uploadFolder}")
    private String uploadFolder;
    private String staticAccessPath = "/static/upload/img/";

    @Autowired
    MongoTemplate mongoTemplate;


    @Test
    void contextLoads() {
//        String groupId = "5ef04b64c6849a3aa9ee3567";
//        List<String> list = new ArrayList<>();
//        list.add("wenzhuolin");
//        list.add("CastJo");
//        list.add("19990425");
//        list.add("leon");
//        list.add("114514");
//
//        try {
//            String path = ImgUtil.getCombinationOfHead(list, uploadFolder, groupId.toString());
//        }catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
