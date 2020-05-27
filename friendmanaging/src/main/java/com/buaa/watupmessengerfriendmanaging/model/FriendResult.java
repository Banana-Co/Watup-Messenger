package com.buaa.watupmessengerfriendmanaging.model;

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
    public FriendResult(ResultCode resultCode,String message){
        super(resultCode, message);
    }
}
