package com.buaa.watupmessengerfriendmanaging.model;

import java.time.LocalDateTime;

/**
 * @author Cast
 */
public class FriendRequest {
    private String senderId;
    private String remark;
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
}
