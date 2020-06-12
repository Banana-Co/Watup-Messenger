package com.buaa.whatupmessengermessaging.model;

public class GroupRequest {
    String id;
    String groupName;
    String groupId;
    String userId;
    String invitedBy;

    public GroupRequest(String groupName, String groupId, String userId, String invitedBy) {
        this.groupName = groupName;
        this.groupId = groupId;
        this.userId = userId;
        this.invitedBy = invitedBy;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getInvitedBy() {
        return invitedBy;
    }

    public void setInvitedBy(String invitedBy) {
        this.invitedBy = invitedBy;
    }
}
