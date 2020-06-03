package com.buaa.watupmessengerfriendmanaging.model.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.IOException;

/**
 * @author Cast
 */
@ResponseStatus(code = HttpStatus.BAD_REQUEST,reason = "other exception")
public class OtherException extends RuntimeException {
    public OtherException(){}
    public OtherException(String message){
        super(message);
    }
}
