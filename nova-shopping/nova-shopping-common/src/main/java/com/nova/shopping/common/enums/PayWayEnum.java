package com.nova.shopping.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @description: 支付方式枚举
 * @author: wzh
 * @date: 2023/4/14 18:31
 */
@Getter
@AllArgsConstructor
public enum PayWayEnum {

    DEFAULT(0, "默认"),

    ALI(1, "阿里支付"),

    WECHAT(2, "微信支付"),

    APPLE(3, "苹果支付"),

    YEE_PAY(4, "易宝支付"),

    GOOGLE_PAY(5, "谷歌支付"),

    KS_PAY(6, "快手支付"),

    HUA_WEI_PAY(7, "华为支付");

    /**
     * 支付方式
     */
    private final int payWay;

    /**
     * 支付名称
     */
    private final String name;

    public static PayWayEnum valuesOf(int payWay) {
        switch (payWay) {
            case 1:
                return ALI;
            case 2:
                return WECHAT;
            case 3:
                return APPLE;
            case 4:
                return YEE_PAY;
            case 5:
                return GOOGLE_PAY;
            case 6:
                return KS_PAY;
            case 7:
                return HUA_WEI_PAY;
            default:
                return DEFAULT;
        }
    }
}
