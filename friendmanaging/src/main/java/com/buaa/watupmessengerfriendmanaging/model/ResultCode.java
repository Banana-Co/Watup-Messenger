package com.buaa.watupmessengerfriendmanaging.model;

/**
 * @author Cast
 */

public enum ResultCode {
    /**
     * 成功
     */
    success(200, "success"),
    /**
     * 未找到
     */
    forbidden(403, "forbidden"),
    /**
     * 未找到
     */
    notFound(404, "notFound"),
    /**
     * 冲突
     */
    conflict(409, "conflict"),
    /**
     * 其他错误
     */
    error(400, "error");

    int code;
    String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
