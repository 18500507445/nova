package com.nova.common.enums;

import cn.hutool.core.util.EnumUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @description:
 * @author: wzh
 * @date: 2022/9/21 17:24
 */
@Getter
@AllArgsConstructor
public enum ResultCode {

    SUCCESS("0000", "成功"),

    ERROR("9999", "系统异常"),

    PARAMETER_INCOMPLETENESS("0001", "参数不完整");

    private final String code;

    private final String message;

    /**
     * 获取返回code枚举类
     */
    public static ResultCode getResultCode(String code) {
        return EnumUtil.getBy(ResultCode::getCode, code, SUCCESS);
    }

}
