package com.buaa.watupmessengerfriendmanaging.factory;

import com.buaa.watupmessengerfriendmanaging.model.BaseResult;
import com.buaa.watupmessengerfriendmanaging.model.ResultCode;

/**
 * @author Cast
 */
public interface ResultFactory {
    /**
     * 基础结果
     *
     * @param code    状态码
     * @param message 提示信息
     * @param data    数据
     * @return result
     */
    BaseResult produce(int code, String message, Object data);

    /**
     * 基于枚举的结果
     *
     * @param resultCode 状态码
     * @return result
     */
    BaseResult produce(ResultCode resultCode);

    /**
     * 成功
     *
     * @return result
     */
    BaseResult produceSuccess();
    /**
     * 不被许可
     *
     * @return result
     */
    BaseResult produceForbidden();
    /**
     * 未找到
     *
     * @return result
     */
    BaseResult produceNotFound();

    /**
     * 冲突
     *
     * @return result
     */
    BaseResult produceConflict();

    /**
     * 其他错误
     *
     * @return result
     */
    BaseResult produceError();
}
