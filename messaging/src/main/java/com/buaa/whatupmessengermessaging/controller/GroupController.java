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
@CrossOrigin
@RequestMapping("/api")
public class GroupController {
    @Autowired
    GroupService groupService;

    @RequestMapping(value = "/group", method = RequestMethod.POST)
    String addGroup(@RequestParam String id, @RequestParam String name) {
        return groupService.addGroup(id, name);
    }

    @RequestMapping(value = "/group/{groupId}", method = RequestMethod.PUT)
    void modifyGroup(@RequestParam String id, @PathVariable String groupId, @RequestParam String name) {
        groupService.changeName(id, groupId, name);
    }

    @RequestMapping(value = "/group", method = RequestMethod.GET)
    Object getAllGroups(@RequestParam String id, @RequestParam(defaultValue = "true") Boolean detailed) {
        if (!detailed) {
            return groupService.getAllGroups(id);
        } else {
            return groupService.getAllGroupsDetailed(id);
        }
    }

    @RequestMapping(value = "/group/{groupId}", method = RequestMethod.GET)
    Group getGroup(@RequestParam String id, @PathVariable String groupId) {
        Optional<Group> group = groupService.getGroup(id, groupId);
        if (group.isPresent()) {
            return group.get();
        } else {
            throw new ForbiddenException("Not in this group");
        }
    }

    @RequestMapping(value = "/request", method = RequestMethod.POST)
    void addMember(@RequestParam String id, @RequestBody GroupRequest request) {
        groupService.addMember(id, request);
    }

    @RequestMapping(value = "/request", method = RequestMethod.GET)
    List<GroupRequest> getRequests(@RequestParam String id) {
        return groupService.getRequests(id);
    }

    @RequestMapping(value = "/request/{requestId}", method = RequestMethod.DELETE)
    void removeRequest(@RequestParam String id, @PathVariable String requestId) {
        groupService.removeRequest(id, requestId);
    }

    @RequestMapping(value = "/request/{requestId}", method = RequestMethod.PUT)
    void acceptRequest(@RequestParam String id, @PathVariable String requestId) {
        groupService.acceptRequest(id, requestId);
    }

    @RequestMapping(value = "/group/{groupId}", method = RequestMethod.DELETE)
    void remove(@RequestParam String id, @PathVariable String groupId, @RequestParam(required = false) String userId) {
        if (userId == null)
            if (groupService.isManager(id, groupId)) {
                groupService.removeGroup(id, groupId);
            } else {
                groupService.leaveGroup(id, groupId);
            }
        else {
            groupService.removeMember(id, groupId, userId);
        }
    }
}
