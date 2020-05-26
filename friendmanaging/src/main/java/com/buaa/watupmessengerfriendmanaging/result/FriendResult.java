package com.buaa.watupmessengerfriendmanaging.result;

/**
 * @author Cast
 */
public class FriendResult extends BaseResult {

    public FriendResult(int code, String message, Object data) {
        super(code, message, data);
    }

    public FriendResult(ResultCode resultCode) {
        super(resultCode);
    }
}
