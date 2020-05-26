package com.buaa.watupmessengerfriendmanaging.result;

/**
 * @author Cast
 */
public abstract class BaseResult {
    private int code;
    private String message;
    private Object data;

    public BaseResult() {
    }

    public BaseResult(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public BaseResult(ResultCode resultCode) {
        this.code = resultCode.code;
        this.message = resultCode.message;
    }

    public BaseResult(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public BaseResult(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
