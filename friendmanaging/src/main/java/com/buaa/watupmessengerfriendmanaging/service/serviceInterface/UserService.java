package com.buaa.watupmessengerfriendmanaging.service.serviceInterface;

import com.buaa.watupmessengerfriendmanaging.model.User;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

/**
 * @author Cast
 *
 */
public interface UserService {
    Optional<User> getUserByToken(String token);

    Optional<User> getUserById(String id);

    Optional<User> getUserByUsername(String username);

    void addUser(String id, String username);

    ResponseEntity<?> getFriend(String userId,String friendId);
}
