package com.nova.common.core.model.result.avic;

import java.io.Serializable;

/**
 * 返回code枚举
 *
 * @author suo
 * @date 2023/8/23 21:15
 */
public enum ResultCode implements IRespResultCode, Serializable {

    SUCCESS("000000", "操作成功"),

    CURRENT_LIMITING("000001", "操作人数过多请稍后再试！"),

    TOKEN_EXPIRATION("900000", "token失效"),

    NOT_FOUND_USER("901000", "无用户信息"),

    USER_VERIFICATION_CODE_ERROR("901001", "验证码错误"),

    USER_PASSWORD_ERROR("901002", "密码错误"),

    TRIPARTITE_NOT_FOUND_USER("901003", "第三方用户信息不存在"),

    USER_NOT_AUTHORITY("901004", "用户无权限"),

    FAILED("400000", "接口错误"),

    VALIDATE_FAILED("200000", "参数校验失败"),

    ERROR("300000", "未知错误"),

    USER_ERROR("700000", "用户身份异常"),

    DATA_EMPTY("710000", "数据为空"),

    DATA_EXIST("720000", "数据已存在"),

    IM_NO_EXIST("811001", "IM账号未注册"),
    ;

    private final String bizCode;

    private final String bizMessage;

    @Override
    public String getBizCode() {
        return bizCode;
    }

    @Override
    public String getBizMessage() {
        return bizMessage;
    }

    ResultCode(String bizCode, String bizMessage) {
        this.bizCode = bizCode;
        this.bizMessage = bizMessage;
    }
}