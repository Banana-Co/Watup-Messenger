package com.buaa.whatupmessengermessaging.model;

import com.bol.secure.Encrypted;

import java.time.LocalDateTime;

public class GroupMessage {
    private String type;
    @Encrypted
    private String senderId;
    @Encrypted
    private String groupId;
    private String receiverId;
    @Encrypted
    private String content;
    private LocalDateTime timestamp;

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Message{" +
                "type='" + type + '\'' +
                ", id='" + senderId + '\'' +
                ", groupId='" + groupId + '\'' +
                ", content='" + content + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
