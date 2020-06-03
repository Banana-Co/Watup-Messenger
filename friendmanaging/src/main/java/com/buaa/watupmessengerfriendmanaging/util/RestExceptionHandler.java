package com.buaa.watupmessengerfriendmanaging.util;

import com.buaa.watupmessengerfriendmanaging.model.exception.ConflictException;
import com.buaa.watupmessengerfriendmanaging.model.exception.ForbiddenException;
import com.buaa.watupmessengerfriendmanaging.model.exception.UserNotFoundException;
import com.buaa.watupmessengerfriendmanaging.model.factory.ResponseEntityFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * @author Cast
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(UserNotFoundException.class)
    protected ResponseEntity<?> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntityFactory.getInstance().produceNotFound(ex.getMessage());
    }
    @ExceptionHandler(ConflictException.class)
    protected ResponseEntity<?> handleConflictException(ConflictException ex) {
        return ResponseEntityFactory.getInstance().produceConflict(ex.getMessage());
    }
    @ExceptionHandler(ForbiddenException.class)
    protected ResponseEntity<?> handleForbiddenException(ForbiddenException ex) {
        return ResponseEntityFactory.getInstance().produceConflict(ex.getMessage());
    }
}
