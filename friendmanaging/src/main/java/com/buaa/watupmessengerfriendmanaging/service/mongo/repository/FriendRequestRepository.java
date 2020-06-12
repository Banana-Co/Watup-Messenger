package com.buaa.watupmessengerfriendmanaging.service.mongo.repository;

import com.buaa.watupmessengerfriendmanaging.model.FriendRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author Cast
 */
public interface FriendRequestRepository extends MongoRepository<FriendRequest,String> {
    Optional<FriendRequest> getById(String id);
    List<FriendRequest> getByReceiverId(String id);
}
