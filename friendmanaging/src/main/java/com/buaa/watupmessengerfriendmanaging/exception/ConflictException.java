package com.buaa.watupmessengerfriendmanaging.exception;

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
