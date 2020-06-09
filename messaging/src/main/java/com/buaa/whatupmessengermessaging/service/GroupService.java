package com.buaa.whatupmessengermessaging.service;

import com.buaa.whatupmessengermessaging.model.Group;
import com.buaa.whatupmessengermessaging.model.GroupRequest;

import java.util.List;
import java.util.Optional;

public interface GroupService {
    String addGroup(String managerId, String name);
    void changeName(String managerId, String groupId, String name);

    List<String> getAllGroups(String userId);
    List<Group> getAllGroupsDetailed(String userId);
    Optional<Group> getGroup(String userId, String groupId);
    Optional<Group> getGroup(String groupId);
    Group getGroupById(String groupId);

    Boolean isMember(String userId, String groupId);
    Boolean isManager(String userId, String groupId);

    void addMember(String invitedBy, GroupRequest request);
    List<GroupRequest> getRequests(String userId);
    void acceptRequest(String userId, String requestId);
    void removeRequest(String userId, String requestId);

    void leaveGroup(String userId, String groupId);
    void removeMember(String managerId, String groupId, String userId);
    void removeGroup(String managerId, String groupId);
}
