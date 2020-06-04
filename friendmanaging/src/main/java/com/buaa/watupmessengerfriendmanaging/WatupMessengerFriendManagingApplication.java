package com.buaa.watupmessengerfriendmanaging;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

/**
 * @author Cast
 */
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WatupMessengerFriendManagingApplication {

    public static void main(String[] args) {
        SpringApplication.run(WatupMessengerFriendManagingApplication.class, args);
    }

}
