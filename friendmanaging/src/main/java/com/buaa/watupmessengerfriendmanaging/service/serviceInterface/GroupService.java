package com.buaa.watupmessengerfriendmanaging.service.serviceInterface;

import org.springframework.http.ResponseEntity;

/**
 * @author Cast
 */
public interface GroupService {
    /**
     * @param users
     * @return
     */
    ResponseEntity<Object> creatGroup(String token,String[] users);

    ResponseEntity<Object> inviteToGroup(String userId,String groupId);

    ResponseEntity<Object> leaveGroup(String token,String groupId);

    ResponseEntity<Object> getGroups(String token);

    ResponseEntity<Object> getGroup(String token,String keyword);

    ResponseEntity<Object> setGroupName(String groupId,String groupName);

}
