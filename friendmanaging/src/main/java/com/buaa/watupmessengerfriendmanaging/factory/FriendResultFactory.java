package com.buaa.watupmessengerfriendmanaging.factory;

import com.buaa.watupmessengerfriendmanaging.model.BaseResult;
import com.buaa.watupmessengerfriendmanaging.model.FriendResult;
import com.buaa.watupmessengerfriendmanaging.model.ResultCode;

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
    public BaseResult produceSuccess() {
        return new FriendResult(ResultCode.success);
    }

    @Override
    public BaseResult produceNotFound() {
        return new FriendResult(ResultCode.notFound);
    }

    @Override
    public BaseResult produceConflict() {
        return new FriendResult(ResultCode.conflict);
    }

    @Override
    public BaseResult produceError() {
        return new FriendResult(ResultCode.error);
    }

    public BaseResult produceNotFound(String message){
        return new FriendResult(ResultCode.notFound,message);
    }
}
