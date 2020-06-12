package com.buaa.watupmessengerfriendmanaging.model.factory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * @author Cast
 */
public class ResponseEntityFactory {
    private static final ResponseEntityFactory INSTANCE = new ResponseEntityFactory();

    private ResponseEntityFactory() {
    }

    public static ResponseEntityFactory getInstance() {
        return INSTANCE;
    }


    public ResponseEntity<Object> produceSuccess() {
        return new ResponseEntity<>(HttpStatus.OK);
    }
    public ResponseEntity<Object> produceSuccess(Object object) {
        return new ResponseEntity<>(object,HttpStatus.OK);
    }



    public  ResponseEntity<Object> produceNotFound() {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    public ResponseEntity<Object> produceForbidden() {
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    public ResponseEntity<Object> produceConflict() {
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    public ResponseEntity<Object> produceError() {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<Object> produceNotFound(String message){
        return new ResponseEntity<>(message,HttpStatus.NOT_FOUND);
    }
    public ResponseEntity<Object> produceConflict(String message) {
        return new ResponseEntity<>(message,HttpStatus.CONFLICT);
    }
}
