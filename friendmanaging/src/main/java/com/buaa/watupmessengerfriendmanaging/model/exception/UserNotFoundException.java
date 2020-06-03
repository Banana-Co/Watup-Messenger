package com.buaa.watupmessengerfriendmanaging.model.exception;

/**
 * @author Cast
 */
public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String message){
        super(message);
    }
    public UserNotFoundException(){
        super("用户未找到");
    }
}
