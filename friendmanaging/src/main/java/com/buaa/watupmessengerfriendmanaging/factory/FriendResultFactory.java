package com.buaa.watupmessengerfriendmanaging.factory;

import com.buaa.watupmessengerfriendmanaging.model.BaseResult;
import com.buaa.watupmessengerfriendmanaging.model.FriendResult;
import com.buaa.watupmessengerfriendmanaging.model.ResultCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * @author Cast
 */
public class FriendResultFactory implements ResultFactory {
    private static final FriendResultFactory instance = new FriendResultFactory();

    private FriendResultFactory() {
    }

    public static FriendResultFactory getInstance() {
        return instance;
    }

    @Override
    public BaseResult produce(int code, String message, Object data) {
        return new FriendResult(code, message, data);
    }

    @Override
    public BaseResult produce(ResultCode resultCode) {
        return new FriendResult(resultCode);
    }

    @Override
    public ResponseEntity<Object> produceSuccess() {
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
    public ResponseEntity<Object> produceSuccess(Object object) {
        return new ResponseEntity<>(object,HttpStatus.ACCEPTED);
    }


    @Override
    public ResponseEntity<Object> produceNotFound() {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<Object> produceForbidden() {
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
    @Override
    public ResponseEntity<Object> produceConflict() {
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @Override
    public ResponseEntity<Object> produceError() {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<Object> produceNotFound(String message){
        return new ResponseEntity<>(message,HttpStatus.NOT_FOUND);
    }
}
