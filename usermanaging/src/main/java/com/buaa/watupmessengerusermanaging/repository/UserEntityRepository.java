package com.buaa.watupmessengerusermanaging.repository;

import com.buaa.watupmessengerusermanaging.model.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface UserEntityRepository extends MongoRepository<UserEntity, String> {

    UserEntity findByUsername(String username);
}

