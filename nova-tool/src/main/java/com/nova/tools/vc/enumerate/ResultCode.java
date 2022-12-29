package com.nova.tools.vc.enumerate;

/**
 * @description:
 * @author: wangzehui
 * @date: 2022/9/21 17:24
 */
public enum ResultCode {

    SUCCESS("0000", "成功"),
    ERROR("9999", "系统异常"),
    PARAMETER_INCOMPLETENESS("0001", "参数不完整");

    private String code;
    private String message;

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    ResultCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

}
