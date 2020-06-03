package com.buaa.watupmessengerfriendmanaging.model.exception;

/**
 * @author Cast
 */
public class GroupNotFoundException extends RuntimeException{

    public GroupNotFoundException() {
        super("群组未找到");
    }

    public GroupNotFoundException(String message) {
        super(message);
    }
}
