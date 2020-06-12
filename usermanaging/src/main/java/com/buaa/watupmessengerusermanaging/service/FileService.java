package com.buaa.watupmessengerusermanaging.service;

import org.springframework.web.multipart.MultipartFile;


public interface FileService {

    public String uploadImage(MultipartFile file, String baseUrl);
}
