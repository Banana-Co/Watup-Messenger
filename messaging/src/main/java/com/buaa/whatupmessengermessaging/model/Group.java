package com.buaa.whatupmessengermessaging.model;

import java.util.List;

public class Group {
    private String id;
    private String name;
    private String managerId;
    private List<String> usersId;

    public Group(String name, String managerId, List<String> usersId) {
        this.name = name;
        this.managerId = managerId;
        this.usersId = usersId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    public List<String> getUsersId() {
        return usersId;
    }

    public void setUsersId(List<String> usersId) {
        this.usersId = usersId;
    }
}
