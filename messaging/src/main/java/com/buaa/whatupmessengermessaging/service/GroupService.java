package com.buaa.whatupmessengermessaging.service;

import com.buaa.whatupmessengermessaging.model.Group;
import com.buaa.whatupmessengermessaging.model.GroupRequest;

import java.util.List;
import java.util.Optional;

public interface GroupService {
    String addGroup(String token, String name);
    void changeName(String token, String groupId, String name);
    void addMember(String token, GroupRequest request);

    List<String> getAllGroups(String token);
    List<Group> getAllGroupsDetailed(String token);
    Optional<Group> getGroup(String token, String groupId);

    Group getGroupById(String groupId);

    Boolean isMember(String token, String groupId);

    Boolean isManager(String token, String groupId);

    List<GroupRequest> getRequests(String token);
    void removeRequest(String token, String requestId);
    void acceptRequest(String token, String groupId);

    void leaveGroup(String token, String groupId);
    void removeMember(String token, String groupId, String userId);
    void removeGroup(String token, String groupId);
}
