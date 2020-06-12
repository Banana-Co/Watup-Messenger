package com.buaa.watupmessengeroauthserver.repository;

import com.buaa.watupmessengeroauthserver.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface UserRepository extends MongoRepository<User, String> {

    User findByUsername(String username);


    User findUserById(String id);

    User findUserByEmail(String email);

}
