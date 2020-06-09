package com.buaa.whatupmessengermessaging.service.impl;

import com.buaa.whatupmessengermessaging.exception.ForbiddenException;
import com.buaa.whatupmessengermessaging.model.Group;
import com.buaa.whatupmessengermessaging.model.GroupRequest;
import com.buaa.whatupmessengermessaging.model.UserGroup;
import com.buaa.whatupmessengermessaging.service.GroupService;
import com.mongodb.client.result.UpdateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Primary
public class GroupServiceImpl implements GroupService {
    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public String addGroup(String managerId, String name) {
        Group group = new Group(name, managerId, new ArrayList<>());
        String groupId =  mongoTemplate.insert(group, "groups").getId();
        addToProfile(managerId, groupId);
        addToGroup(managerId, groupId);

        return groupId;
    }

    @Override
    public void changeName(String managerId, String groupId, String name) {
        Group group = mongoTemplate.findById(groupId, Group.class, "groups");
        if (group == null || !managerId.equals(group.getManagerId())) {
            throw new ForbiddenException("No such group, or you don't have access to change the group.");
        } else {
            group.setName(name);
            mongoTemplate.save(group);
        }
    }

    @Override
    public void addMember(String invitedBy, GroupRequest request) {
        Group group = mongoTemplate.findById(request.getGroupId(), Group.class, "groups");
        if (group == null || !group.getUsersId().contains(invitedBy)) {
            throw new ForbiddenException("No such group, or you don't have access to add member.");
        } else if (group.getUsersId().contains(request.getUserId())) {
            throw new ForbiddenException("The user is already in group.");
        } else {
            request.setGroupName(group.getName());
            request.setInvitedBy(invitedBy);
            mongoTemplate.save(request, "grouprequests");
        }
    }

    @Override
    public List<String> getAllGroups(String userId) {
        return getAllGroupsId(userId);
    }

    @Override
    public List<Group> getAllGroupsDetailed(String userId) {
        List<String> groupsId = getAllGroupsId(userId);
        return groupsId.parallelStream()
                .map(this::getGroupById)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Group> getGroup(String userId, String groupId) {
        Group group = getGroupById(groupId);
        if (group == null || !group.getUsersId().contains(userId)) {
            return Optional.empty();
        } else {
            return Optional.of(group);
        }
    }

    @Override
    public Optional<Group> getGroup(String groupId) {
        Group group = getGroupById(groupId);
        if (group == null) {
            return Optional.empty();
        } else {
            return Optional.of(group);
        }
    }

    private List<String> getAllGroupsId(String userId) {
        UserGroup userGroup = mongoTemplate.findById(userId, UserGroup.class, "usergroup");
        if (userGroup == null) {
            return new ArrayList<>();
        } else {
            return userGroup.getGroupsId();
        }
    }

    @Override
    public Group getGroupById(String groupId) {
        return mongoTemplate.findById(groupId, Group.class, "groups");
    }

    @Override
    public Boolean isMember(String userId, String groupId) {
        Group group = getGroupById(groupId);
        return group != null && group.getUsersId().contains(userId);
    }

    @Override
    public Boolean isManager(String userId, String groupId) {
        Group group = getGroupById(groupId);
        return group != null && group.getManagerId().equals(userId);
    }

    @Override
    public List<GroupRequest> getRequests(String userId) {
        Criteria criteria = Criteria.where("userId").is(userId);
        return mongoTemplate.find(Query.query(criteria), GroupRequest.class, "grouprequests");
    }

    @Override
    public void acceptRequest(String userId, String requestId) {
        GroupRequest groupRequest = mongoTemplate.findById(requestId, GroupRequest.class, "grouprequests");
        if (groupRequest == null || !userId.equals(groupRequest.getUserId())) {
            throw new ForbiddenException("No such request, or you want to accept the request that was not sent to you.");
        } else {
            addToProfile(userId, groupRequest.getGroupId());
            addToGroup(userId, groupRequest.getGroupId());
            _removeRequest(groupRequest.getId());
        }

    }

    @Override
    public void removeRequest(String userId, String requestId) {
        GroupRequest groupRequest = mongoTemplate.findById(requestId, GroupRequest.class, "grouprequests");
        if (groupRequest == null || !userId.equals(groupRequest.getUserId())) {
            throw new ForbiddenException("No such request, or you want to remove the request that was not sent to you.");
        } else {
            _removeRequest(groupRequest.getId());
        }
    }

    @Override
    public void leaveGroup(String userId, String groupId) {
        Group group = mongoTemplate.findById(groupId, Group.class, "groups");
        if (group == null || !group.getUsersId().contains(userId)) {
            throw new ForbiddenException("No such group, or you are currently not in this group.");
        } else {
            removeFromProfile(userId, groupId);
            removeFromGroup(userId, groupId);
        }
    }

    @Override
    public void removeMember(String managerId, String groupId, String userId) {
        Group group = mongoTemplate.findById(groupId, Group.class, "groups");
        if (group == null || !group.getManagerId().equals(managerId) || !group.getUsersId().contains(userId) || userId.equals(managerId)) {
            throw new ForbiddenException("No such group, or you are not the manager of the group, or the user you want to remove is not in this group.");
        } else {
            removeFromProfile(userId, groupId);
            removeFromGroup(userId, groupId);
        }
    }


    @Override
    public void removeGroup(String managerId, String groupId) {
        Group group = mongoTemplate.findById(groupId, Group.class, "groups");
        if (group == null || !group.getManagerId().equals(managerId)) {
            throw new ForbiddenException("No such group, or you are not the manager of the group.");
        } else {
            for (String userId : group.getUsersId())
                removeFromProfile(userId, groupId);
            mongoTemplate.remove(Query.query(Criteria.where("_id").is(groupId)), "groups");
        }
    }

    private void addToProfile(String userId, String groupId) {
        Criteria criteria = Criteria.where("_id").is(userId);
        Update update = new Update().addToSet("groupsId", groupId);
        UpdateResult result = mongoTemplate.updateFirst(Query.query(criteria), update, "usergroup");

        if (result.getModifiedCount() == 0) {
            List<String> groupsId = new ArrayList<>();
            groupsId.add(groupId);
            UserGroup userGroup = new UserGroup(userId, groupsId);
            mongoTemplate.insert(userGroup, "usergroup");
        }
    }

    private void addToGroup(String userId, String groupId) {
        Criteria criteria = Criteria.where("_id").is(groupId);
        Update update = new Update().addToSet("usersId", userId);
        mongoTemplate.updateFirst(Query.query(criteria), update, "groups");
    }

    private void removeFromProfile(String userId, String groupId) {
        Criteria criteria = Criteria.where("_id").is(userId);
        Update update = new Update().pull("groupsId", groupId);
        mongoTemplate.updateFirst(Query.query(criteria), update, "usergroup");
    }

    private void removeFromGroup(String userId, String groupId) {
        Criteria criteria = Criteria.where("_id").is(groupId);
        Update update = new Update().pull("usersId", userId);
        mongoTemplate.updateFirst(Query.query(criteria), update, "groups");
    }

    private void _removeRequest(String requestId) {
        Criteria criteria = Criteria.where("_id").is(requestId);
        mongoTemplate.remove(Query.query(criteria), "grouprequests");
    }
}
