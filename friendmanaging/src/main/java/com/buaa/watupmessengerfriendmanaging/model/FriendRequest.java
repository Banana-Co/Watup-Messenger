package com.buaa.watupmessengerfriendmanaging.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * @author Cast
 */
@Document
public class FriendRequest {
    @Id
    private String id;
    private String senderId;
    private String receiverId;
    private String remark;
    private String senderAvatarUrl;
    private LocalDateTime createdDate;

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getSenderAvatarUrl() {
        return senderAvatarUrl;
    }

    public void setSenderAvatarUrl(String senderAvatarUrl) {
        this.senderAvatarUrl = senderAvatarUrl;
    }
}
