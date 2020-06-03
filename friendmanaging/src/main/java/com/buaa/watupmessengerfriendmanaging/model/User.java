package com.buaa.watupmessengerfriendmanaging.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author Cast
 */
@Document
public class User {
    @Id
    private String id;
    private String username;
    private String token;
    private LocalDateTime createdDate;
    private Map<String, String> friends;
    private List<String> blocks;
    private List<FriendRequest> friendRequestList;
    private List<String> groups;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getBlocks() {
        return blocks;
    }

    public void setBlocks(List<String> blocks) {
        this.blocks = blocks;
    }

    public Map<String, String> getFriends() {
        return friends;
    }

    public void setFriends(Map<String, String> friends) {
        this.friends = friends;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public List<FriendRequest> getFriendRequestList() {
        return friendRequestList;
    }

    public void setFriendRequestList(List<FriendRequest> friendRequestList) {
        this.friendRequestList = friendRequestList;
    }

    public List<String> getGroups() {
        return groups;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }
}
