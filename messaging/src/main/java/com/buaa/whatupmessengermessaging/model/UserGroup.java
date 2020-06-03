package com.buaa.whatupmessengermessaging.model;

import java.util.List;

public class UserGroup {
    String id;
    List<String> groupsId;

    public UserGroup(String id, List<String> groupsId) {
        this.id = id;
        this.groupsId = groupsId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getGroupsId() {
        return groupsId;
    }

    public void setGroupsId(List<String> groupsId) {
        this.groupsId = groupsId;
    }
}
