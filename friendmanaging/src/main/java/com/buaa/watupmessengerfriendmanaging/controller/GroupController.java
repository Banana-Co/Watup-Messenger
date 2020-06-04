package com.buaa.watupmessengerfriendmanaging.controller;

import com.buaa.watupmessengerfriendmanaging.service.face.FriendService;
import com.buaa.watupmessengerfriendmanaging.service.face.GroupService;
import com.buaa.watupmessengerfriendmanaging.service.face.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Cast
 */
@RestController
@CrossOrigin
@RequestMapping(value = "/api/")
public class GroupController {
    @Autowired
    UserService userService;
    @Autowired
    FriendService friendService;
    @Autowired
    GroupService groupService;
    @RequestMapping(value = "groups", method = RequestMethod.GET)
    public ResponseEntity<Object> getGroups(@RequestHeader(name = "Authorization") String token) {
        return groupService.getGroups(token);
    }
    @RequestMapping(value = "group", method = RequestMethod.POST)
    public ResponseEntity<Object> createGroup(@RequestHeader(name = "Authorization") String token, @RequestParam(name = "users",defaultValue = "",required = false) String[] users) {
        return groupService.creatGroup(token, users);
    }
    @RequestMapping(value = "group/member", method = RequestMethod.POST)
    public ResponseEntity<Object> inviteToGroup(@RequestHeader(name = "Authorization") String token
            , @RequestParam String userId
            ,String groupId) {
        return groupService.inviteToGroup(userId, groupId);
    }
    @RequestMapping(value = "group/member", method = RequestMethod.DELETE)
    public ResponseEntity<Object> leaveGroup(@RequestHeader(name = "Authorization") String token
            , @RequestParam String groupId) {
        return groupService.leaveGroup(token, groupId);
    }
    @RequestMapping(value = "group/name", method = RequestMethod.PUT)
    public ResponseEntity<Object> setGroupName(@RequestHeader(name = "Authorization") String token
            ,@RequestParam String groupId,String groupName) {
        return groupService.setGroupName(groupId, groupName);
    }
}
