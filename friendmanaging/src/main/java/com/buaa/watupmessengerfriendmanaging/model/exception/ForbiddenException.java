package com.buaa.watupmessengerfriendmanaging.model.exception;

/**
 * @author Cast
 */
public class ForbiddenException extends RuntimeException{
    public ForbiddenException() {
        super("请求被拒绝");
    }

    public ForbiddenException(String message) {
        super(message);
    }
}
