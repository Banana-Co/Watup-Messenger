package com.buaa.watupmessengerusermanaging;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
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

    @Test
    void contextLoads() {
        Integer groupId = 0;
        for(int i = 1; i <= 10; i++) {
            List<String> list = new ArrayList<>();
            for(int j = 0; j < i ; j++) {

                list.add(uploadFolder + "2.png");
            }

            try {

                String path = ImgUtil.getCombinationOfHead(list, uploadFolder, groupId.toString());
                groupId++;
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
