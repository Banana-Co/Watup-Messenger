package com.buaa.whatupmessengermessaging.controller;

import com.buaa.whatupmessengermessaging.exception.ForbiddenException;
import com.buaa.whatupmessengermessaging.model.Group;
import com.buaa.whatupmessengermessaging.model.GroupRequest;
import com.buaa.whatupmessengermessaging.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class GroupController {
    @Autowired
    GroupService groupService;

    @RequestMapping(value = "/group", method = RequestMethod.POST)
    String addGroup(@RequestHeader(value = "Authorization") String access_token, @RequestParam String name) {
        return groupService.addGroup(access_token, name);
    }

    @RequestMapping(value = "/group/{groupId}", method = RequestMethod.PUT)
    void modifyGroup(@RequestHeader(value = "Authorization") String access_token, @PathVariable String groupId, @RequestParam String name) {
        groupService.changeName(access_token, groupId, name);
    }

    @RequestMapping(value = "/group", method = RequestMethod.GET)
    Object getAllGroups(@RequestHeader(value = "Authorization") String access_token, @RequestParam(defaultValue = "true") Boolean detailed) {
        if (!detailed) {
            return groupService.getAllGroups(access_token);
        } else {
            return groupService.getAllGroupsDetailed(access_token);
        }
    }

    @RequestMapping(value = "/group/{groupId}", method = RequestMethod.GET)
    Group getGroup(@RequestHeader(value = "Authorization") String access_token, @PathVariable String groupId) {
        Optional<Group> group = groupService.getGroup(access_token, groupId);
        if (group.isPresent()) {
            return group.get();
        } else {
            throw new ForbiddenException("Not in this group");
        }
    }

    @RequestMapping(value = "/request", method = RequestMethod.POST)
    void addMember(@RequestHeader(value = "Authorization") String access_token, @RequestBody GroupRequest request) {
        groupService.addMember(access_token, request);
    }

    @RequestMapping(value = "/request", method = RequestMethod.GET)
    List<GroupRequest> getRequests(@RequestHeader(value = "Authorization") String access_token) {
        return groupService.getRequests(access_token);
    }

    @RequestMapping(value = "/request/{requestId}", method = RequestMethod.DELETE)
    void removeRequest(@RequestHeader(value = "Authorization") String access_token, @PathVariable String requestId) {
        groupService.removeRequest(access_token, requestId);
    }

    @RequestMapping(value = "/request/{requestId}", method = RequestMethod.PUT)
    void acceptRequest(@RequestHeader(value = "Authorization") String access_token, @PathVariable String requestId) {
        groupService.acceptRequest(access_token, requestId);
    }

    @RequestMapping(value = "/group/{groupId}", method = RequestMethod.DELETE)
    void remove(@RequestHeader(value = "Authorization") String access_token, @PathVariable String groupId, @RequestParam(required = false) String userId) {
        if (userId == null)
            if (groupService.isManager(access_token, groupId)) {
                groupService.removeGroup(access_token, groupId);
            } else {
                groupService.leaveGroup(access_token, groupId);
            }
        else {
            groupService.removeMember(access_token, groupId, userId);
        }
    }
}
