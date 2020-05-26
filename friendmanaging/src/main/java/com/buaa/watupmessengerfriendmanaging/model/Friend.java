package com.buaa.watupmessengerfriendmanaging.model;

/**
 * @author Cast
 */
public class Friend {
    private String id;
    private String nickname = "";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
