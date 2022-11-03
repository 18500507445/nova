package com.nova.pay.enums;


/**
 * @Description: 支付方式枚举类
 * @Author: wangzehui
 * @Date: 2022/9/18 11:29
 */
public enum PayWayEnum {

    DEFAULT(0, "默认"),

    ALI(1, "阿里支付"),

    WECHAT(2, "微信支付"),

    APPLE(3, "苹果支付"),

    YEE_PAY(4, "易宝支付"),

    GOOGLE_PAY(5, "谷歌支付"),

    EXCHANGE(99, "金币兑换");

    /**
     * 支付方式
     */
    private final int payWay;

    /**
     * 支付名称
     */
    private final String name;

    public int getPayWay() {
        return payWay;
    }

    public String getName() {
        return name;
    }

    PayWayEnum(int payWay, String name) {
        this.payWay = payWay;
        this.name = name;
    }

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
            case 99:
                return EXCHANGE;
            default:
                return DEFAULT;
        }
    }
}
