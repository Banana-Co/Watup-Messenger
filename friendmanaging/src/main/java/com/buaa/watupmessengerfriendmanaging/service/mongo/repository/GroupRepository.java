package com.buaa.watupmessengerfriendmanaging.service.mongo.repository;

import com.buaa.watupmessengerfriendmanaging.model.Group;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

/**
 * @author Cast
 */
public interface GroupRepository extends MongoRepository<Group, String> {
    Optional<Group> getById(String id);
}
