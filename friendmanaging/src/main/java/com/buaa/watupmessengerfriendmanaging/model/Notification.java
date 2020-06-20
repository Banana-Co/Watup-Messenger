package com.buaa.watupmessengerfriendmanaging.model;

/**
 * @author Cast
 */
public class Notification {
    String type;
    String notificationType;
    Object content;

    public Notification(String notificationType, String content) {
        this.notificationType = notificationType;
        this.content = content;
    }

    public Notification(String type, String notificationType, String content) {
        this.type = type;
        this.notificationType = notificationType;
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }
}
