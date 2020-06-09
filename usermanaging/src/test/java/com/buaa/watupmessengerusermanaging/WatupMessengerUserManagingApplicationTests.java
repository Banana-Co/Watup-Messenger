package com.buaa.watupmessengerusermanaging;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;

@SpringBootTest
class WatupMessengerUserManagingApplicationTests {


    @Autowired
    UserEntityRepository userEntityRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void contextLoads() {
        if (this.userEntityRepository.findByUsername("dastur") == null) {
            UserEntity user = new UserEntity("Leon Dastur", "dastur", passwordEncoder.encode("rokin123"), Arrays.asList("ADMIN"));
            this.userEntityRepository.save(user);
        }
    }

}
