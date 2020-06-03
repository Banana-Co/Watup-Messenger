package com.buaa.watupmessengerfriendmanaging.model.exception;

/**
 * @author Cast
 */
public class ConflictException extends RuntimeException{

    public ConflictException() {
    }

    public ConflictException(String message) {
        super(message);
    }
}
