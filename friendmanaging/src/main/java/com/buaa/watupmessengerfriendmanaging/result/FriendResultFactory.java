package com.buaa.watupmessengerfriendmanaging.result;

/**
 * @author Cast
 */
public class FriendResultFactory implements ResultFactory {
    private static volatile FriendResultFactory instance = null;

    private FriendResultFactory() {
    }

    public static FriendResultFactory getInstance() {
        if (instance == null) {
            synchronized (FriendResultFactory.class) {
                if (instance == null) {
                    instance = new FriendResultFactory();
                }
            }
        }
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
}
