package com.buaa.watupmessengerusermanaging;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@EnableDiscoveryClient
@SpringBootApplication
public class WatupMessengerUserManagingApplication {


    public static void main(String[] args) {
        SpringApplication.run(WatupMessengerUserManagingApplication.class, args);
    }


}
