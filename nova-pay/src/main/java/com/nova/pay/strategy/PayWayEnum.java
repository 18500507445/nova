package com.nova.pay.strategy;


/**
 * @Description: 支付方式
 * @Author: wangzehui
 * @Date: 2022/9/18 11:29
 */
public enum PayWayEnum {

    ALI(1, "苹果支付"),

    WECHAT(2, "微信支付"),

    APPLE(3, "苹果支付"),

    YEE_PAY(4, "易宝支付");

    /**
     * 支付方式
     */
    private Integer payWay;

    /**
     * 支付名称
     */
    private String name;

    public Integer getPayWay() {
        return payWay;
    }

    public String getName() {
        return name;
    }

    PayWayEnum(Integer payWay, String name) {
        this.payWay = payWay;
        this.name = name;
    }

    public static PayWayEnum valuesOf(Integer payWay) {
        switch (payWay) {
            case 1:
                return ALI;
            default:
                return WECHAT;
        }
    }
}
