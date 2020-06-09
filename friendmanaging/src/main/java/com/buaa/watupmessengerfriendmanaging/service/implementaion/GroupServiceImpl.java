package com.buaa.watupmessengerfriendmanaging.service.implementaion;

import com.buaa.watupmessengerfriendmanaging.model.Group;
import com.buaa.watupmessengerfriendmanaging.model.User;
import com.buaa.watupmessengerfriendmanaging.exception.GroupNotFoundException;
import com.buaa.watupmessengerfriendmanaging.exception.UserNotFoundException;
import com.buaa.watupmessengerfriendmanaging.model.factory.ResponseEntityFactory;
import com.buaa.watupmessengerfriendmanaging.service.serviceInterface.GroupService;
import com.buaa.watupmessengerfriendmanaging.service.serviceInterface.UserService;
import com.buaa.watupmessengerfriendmanaging.service.mongo.repository.GroupRepository;
import com.buaa.watupmessengerfriendmanaging.service.mongo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @author Cast
 */
@Service
public class GroupServiceImpl implements GroupService {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GroupRepository groupRepository;
    private void joinGroup(String userId,String groupId){
        Optional<User> userOptional = userService.getUserById(userId);
        if(userOptional.isEmpty()){
            throw new UserNotFoundException();
        }
        User user=userOptional.get();
        if(user.getGroups()==null){
            user.setGroups(new ArrayList<>());
        }
        user.getGroups().add(groupId);
        userRepository.save(user);
    }
    @Override
    public ResponseEntity<Object> creatGroup(String token,String[] users) {
        Optional<User> user = userService.getUserByToken(token);
        if (user.isEmpty()) {
            throw new UserNotFoundException();
        }
        List<String> users2=new ArrayList<>(Arrays.asList(users));
        users2.add(user.get().getId());
        Group group=new Group();
        group.setMembers(new ArrayList<>(users2));
        group.setCreatedDate(LocalDateTime.now());
        for (String id:users){
            joinGroup(id,group.getId());
        }
        groupRepository.save(group);
        return ResponseEntityFactory.getInstance().produceSuccess();
    }

    @Override
    public ResponseEntity<Object> inviteToGroup(String userId, String groupId) {
        Optional<Group> groupOptional=groupRepository.getById(groupId);
        Optional<User> userOptional = userService.getUserById(userId);
        if (groupOptional.isEmpty()){
            throw new GroupNotFoundException();
        }
        if(userOptional.isEmpty()){
            throw new UserNotFoundException();
        }
        User user=userOptional.get();
        Group group=groupOptional.get();
        group.getMembers().add(user.getId());
        joinGroup(userId,groupId);
        groupRepository.save(group);
        return ResponseEntityFactory.getInstance().produceSuccess();
    }

    @Override
    public ResponseEntity<Object> leaveGroup(String token, String groupId) {
        Optional<Group> groupOptional=groupRepository.getById(groupId);
        Optional<User> userOptional = userService.getUserByToken(token);
        if (groupOptional.isEmpty()){
            throw new GroupNotFoundException();
        }
        if(userOptional.isEmpty()){
            throw new UserNotFoundException();
        }
        User user=userOptional.get();
        Group group=groupOptional.get();
        group.getMembers().remove(user.getId());
        user.getGroups().remove(groupId);
        userRepository.save(user);
        groupRepository.save(group);
        return ResponseEntityFactory.getInstance().produceSuccess();
    }

    @Override
    public ResponseEntity<Object> getGroups(String token) {
        Optional<User> userOptional = userService.getUserByToken(token);
        if(userOptional.isEmpty()){
            throw new UserNotFoundException();
        }
        User user=userOptional.get();
        if(user.getGroups()==null){
            return ResponseEntityFactory.getInstance().produceSuccess(new ArrayList<>());
        }
        return ResponseEntityFactory.getInstance().produceSuccess(user.getGroups());
    }

    @Override
    public ResponseEntity<Object> getGroup(String token, String keyword) {
        return null;
    }

    @Override
    public ResponseEntity<Object> setGroupName(String groupId, String groupName) {
        Optional<Group> groupOptional=groupRepository.getById(groupId);
        if (groupOptional.isEmpty()){
            throw new GroupNotFoundException();
        }
        Group group=groupOptional.get();
        group.setId(groupId);
        groupRepository.save(group);
        return ResponseEntityFactory.getInstance().produceSuccess();
    }
}
