package com.buaa.watupmessengerusermanaging.service.impl;

import com.buaa.watupmessengerusermanaging.service.FileService;
import com.buaa.watupmessengerusermanaging.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@Primary
public class FileServiceImpl implements FileService {

    @Value("${file.uploadFolder}")
    private String uploadFolder;

    private String staticAccessPath = "/static/upload/img/";

    @Override
    public String uploadImage(MultipartFile file, String baseUrl) {

        FileUtil fileUtil = new FileUtil();

        File uploadDirectory = new File(uploadFolder); //存储目录
        if (uploadDirectory.exists()) {
            if (!uploadDirectory.isDirectory()) {
                uploadDirectory.delete();
            }
        } else {
            uploadDirectory.mkdirs();
        }

        if (file != null) {
                String oldFileName = file.getOriginalFilename();
                System.out.println(oldFileName);
                //判断是否有文件且是否为图片文件
                if (oldFileName != null &&
                        !"".equalsIgnoreCase(oldFileName.trim()) &&
                        fileUtil.isImageFile(oldFileName)) {
                    //生成新的文件名
                    String newFileName = UUID.randomUUID().toString() + fileUtil.getFileType(oldFileName);
                    try {
                        file.transferTo(new File(uploadDirectory, newFileName));
                        String imageUrl = baseUrl + staticAccessPath + newFileName; //图片url
                        return imageUrl;
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                }
        }
        return null;
    }
}
