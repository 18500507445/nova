package com.nova.common.core.domain;

/**
 * @description: 基础的返回码对象(基础的返回码)
 * @author: wzh
 * @date: 2022/12/20 11:16
 */
public interface IRespResultCode {

    /**
     * 结果码
     */
    Integer getCode();

    /**
     * 返回消息
     */
    String getMessage();

    /**
     * 详细的错误消息(开发看)
     */
    String getDetailMessage();
}