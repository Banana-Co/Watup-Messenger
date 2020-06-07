package com.buaa.watupmessengeroauthserver;

import com.buaa.watupmessengeroauthserver.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
class WatupmessengeroauthServerApplicationTests {

    @Autowired
    MongoTemplate mongoTemplate;
    @Test
    void contextLoads() {
        User user = new User("leondastur", "MrLeon", new BCryptPasswordEncoder().encode("12345"));
        mongoTemplate.save(user);
    }
}
