package com.nova.shopping.common.constant.result;

/**
 * 通用返回码对象(主要放各个模块的通用错误码)
 * @author wzh
 */
public enum RespResultEnum {

    /**
     * 成功
     */
    OK(0, "成功", "成功"),

    /**
     * socket超时异常
     */
    SOCKET_TIMEOUT_EXCEPTION(-2, "socket 超时异常", "socket 超时异常"),

    /**
     * io异常
     */
    IO_EXCEPTION(-3, "io 异常", "io 异常"),

    /**
     * 系统异常
     */
    SYS_EXCEPTION(-1, "服务发生了一点小故障，请稍后重试", "系统异常"),

    /**
     * 参数不合法
     */
    ERR_PARAM_NOT_LEGAL(100000, "参数不合法", "参数不合法"),

    /**
     * 请勿重复操作
     */
    REPEATED_OPERATE(100001, "请勿重复操作", "请勿重复操作"),

    /**
     * 请求太频繁，请稍后重试
     */
    REPEATED_BUSY(100002, "请求太频繁，请稍后重试", "请求繁忙，请稍后重试"),

    /**
     * 远程接口频繁异常，系统熔断
     */
    EXTERNAL_SERVICE_EXCEPTION(100003, "远程接口频繁异常，系统熔断", "远程接口频繁异常，系统熔断"),

    /**
     * 数据库有重复的记录
     */
    ERR_CONSTRAINT_VIOLATION(100004, "数据库记录重复", "数据库记录重复"),

    /**
     * 购买条件不成立
     */
    BUY_NOT_ESTABLISHED(100005, "购买条件不成立", "购买条件不成立"),

    /**
     * 订单排队中
     */
    ORDER_QUEUING(1000006, "订单排队中，请等待结果", "订单排队中，请等待结果");

    /**
     * 错误码
     */
    private final Integer code;

    /**
     * 错误消息
     */
    private final String message;

    /**
     * 详细的错误消息(开发看)
     */
    private final String detailMessage;

    RespResultEnum(Integer code, String message, String detailMessage) {
        this.code = code;
        this.message = message;
        this.detailMessage = detailMessage;
    }

}
