package com.buaa.watupmessengerfriendmanaging.model.factory;

import com.buaa.watupmessengerfriendmanaging.model.result.BaseResult;
import com.buaa.watupmessengerfriendmanaging.model.result.ResultCode;
import org.springframework.http.ResponseEntity;

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
    ResponseEntity<Object> produceSuccess();
    /**
     * 不被许可
     *
     * @return result
     */
    ResponseEntity<Object> produceForbidden();
    /**
     * 未找到
     *
     * @return result
     */
    ResponseEntity<Object> produceNotFound();

    /**
     * 冲突
     *
     * @return result
     */
    ResponseEntity<Object> produceConflict();

    /**
     * 其他错误
     *
     * @return result
     */
    ResponseEntity<Object> produceError();
}
